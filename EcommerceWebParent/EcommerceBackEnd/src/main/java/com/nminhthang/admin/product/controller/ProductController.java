package com.nminhthang.admin.product.controller;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.category.CategoryService;
import com.nminhthang.common.exception.ProductNotFoundException;
import com.nminhthang.admin.product.ProductSaveHelper;
import com.nminhthang.admin.product.ProductService;
import com.nminhthang.admin.product.exporter.ProductCSVExporter;
import com.nminhthang.admin.security.UserDetailsImp;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
import com.nminhthang.common.entity.product.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(model, 1, sortDir, null, 0, null);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId,
                             @Param("listOutOfStock") Integer listOutOfStock) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<Product> listProductsPage = productService.listByPage(pageNum, sortDir, keyword, categoryId, listOutOfStock);
        List<Product> listProducts = listProductsPage.getContent();
        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;

        if (endCount > listProductsPage.getTotalElements()) {
            endCount = listProductsPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listOutOfStock", listOutOfStock);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", listProductsPage.getTotalElements());
        model.addAttribute("totalPages", listProductsPage.getTotalPages());
        model.addAttribute("sortField", "name");
        model.addAttribute("module", "products");
        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        
        return "product/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = Product.builder()
                .quantityInStock(0)
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
                              @RequestParam(name = "fileImage", required = false) MultipartFile mainImage,
                              @RequestParam(name = "extraImage", required = false) MultipartFile[] extraImages,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIds,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIds,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal UserDetailsImp loggedUser) throws IOException {

        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Sales")) {
                productService.saveProductPrice(product);
                redirectAttributes.addFlashAttribute("Product have been saved successfully");
                return getRedirectURLToAffectedProduct(product);
            }
        }

        ProductSaveHelper.setMainImage(mainImage, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIds, imageNames, product);
        ProductSaveHelper.setNewExtraImages(extraImages, product);
        ProductSaveHelper.setProductDetails(detailIds, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImage, extraImages, savedProduct);

        ProductSaveHelper.deleteExtraImagesWeredRemovedFrom(product);

        redirectAttributes.addFlashAttribute("message", "The product was saved successfully");

        return getRedirectURLToAffectedProduct(product);
    }

    private String getRedirectURLToAffectedProduct(Product product) {
        return "redirect:/products/page/1?sortField=id&sortDir=asc&keyword=" + product.getName();
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetailsImp loggedUser){
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean isReadOnlyForSales = false;

            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Sales")) {
                    isReadOnlyForSales = true;
                }
            }

            model.addAttribute("isReadOnlyForSales", isReadOnlyForSales);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product (ID: " + product.getId() + ")");
            model.addAttribute("mod", "edit");
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

    @GetMapping("/products/export/outOfStock")
    public void exportToOutOfStock(HttpServletResponse response) throws IOException {
        List<Product> listProducts = productService.listOutOfStock();
        ProductCSVExporter exporter = new ProductCSVExporter();
        exporter.export(listProducts, response);
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable(name = "id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            model.addAttribute("product", product);


            return "/product/product_detail_modal";
        } catch (ProductNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/products";
        }
    }


}
