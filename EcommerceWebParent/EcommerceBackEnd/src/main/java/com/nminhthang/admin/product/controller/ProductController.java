package com.nminhthang.admin.product.controller;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.product.ProductNotFoundException;
import com.nminhthang.admin.product.ProductService;
import com.nminhthang.admin.product.exporter.ProductCSVExporter;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Product;
import com.nminhthang.common.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @GetMapping("/products")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(model, 1, sortDir, null);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<Product> listProductsPage = productService.listByPage(pageNum, sortDir, keyword);
        List<Product> listProducts = listProductsPage.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;

        if (endCount > listProductsPage.getTotalElements()) {
            endCount = listProductsPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", listProductsPage.getTotalElements());
        model.addAttribute("totalPages", listProductsPage.getTotalPages());
        model.addAttribute("sortField", "name");

        return "product/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = Product.builder()
                .inStock(true)
                .enabled(true)
                .images(new HashSet<>())
                .details(new ArrayList<>())
                .build();

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("mod", "new");
        Integer numberOfExistingExtraImages = product.getImages().size();
        model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

        return "/product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(name = "fileImage") MultipartFile mainImage,
                              @RequestParam(name = "extraImage") MultipartFile[] extraImages,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIds", required = false) String[] imageIds,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames) throws IOException {
        setMainImage(mainImage, product);
        setExistingExtraImageNames(imageIds, imageNames, product);
        setNewExtraImages(extraImages, product);
        setProductDetails(detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        saveUploadedImages(mainImage, extraImages, savedProduct);

        redirectAttributes.addFlashAttribute("message", "The product was saved successfully");

        return getRedirectURLToAffectedProduct(product);
    }

    private void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
        if (imageIds == null || imageIds.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIds.length; count++) {
            Integer id = Integer.parseInt(imageIds[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];

            if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    private void saveUploadedImages(MultipartFile mainImage, MultipartFile[] extraImages, Product savedProduct) throws IOException {
        if (!mainImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImage.getOriginalFilename()));
            String uploadDir = "../" + FileUploadUtil.PRODUCT_DIR_NAME + savedProduct.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImage);
        }

        if (extraImages.length > 0) {
            String uploadDir = "../" + FileUploadUtil.PRODUCT_DIR_NAME + savedProduct.getId() + "/extras";

            for (MultipartFile extraImage : extraImages) {
                if (extraImage.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImage.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, fileName, extraImage);
            }
        }
    }


    private void setNewExtraImages(MultipartFile[] extraImageMultiparts, Product product) {
        if (extraImageMultiparts.length > 0) {
            for (MultipartFile extraImageMultipart : extraImageMultiparts) {
                if (!extraImageMultipart.isEmpty()) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(extraImageMultipart.getOriginalFilename()));

                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    private void setMainImage(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImageMultipart.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

    private String getRedirectURLToAffectedProduct(Product product) {
        return "redirect:/products/page/1?sortField=id&sortDir=asc&keyword=" + product.getId();
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();

            model.addAttribute("listBrands", listBrands);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product (ID: " + product.getId() + ")");
            model.addAttribute("mod", "edit");

            Integer numberOfExistingExtraImages = product.getImages().size();
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


            return "/product/product_form";
        } catch (ProductNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            String extraImagesDir = "../product-images/" + id + "/extras";
            FileUploadUtil.removeDir(extraImagesDir);
            String productDir = "../product-images/" + id;
            FileUploadUtil.removeDir(productDir);

            redirectAttributes.addFlashAttribute("message", "Product by ID = " + id + " has been deleted");
        } catch (ProductNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") boolean enabled,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String status = (enabled == true) ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been " + status);
        return "redirect:/products";
    }

    @GetMapping("/products/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Product> listProducts = productService.listAll();
        ProductCSVExporter exporter = new ProductCSVExporter();
        exporter.export(listProducts, response);
    }

}
