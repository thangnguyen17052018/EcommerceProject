package com.nminhthang.admin.product.controller;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.product.ProductNotFoundException;
import com.nminhthang.admin.product.ProductService;
import com.nminhthang.admin.product.exporter.ProductCSVExporter;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Product;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

        long startCount = (long) (pageNum - 1) * productService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + productService.PRODUCT_PER_PAGE - 1;

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
                .build();

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("mod", "new");

        return "/product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(name = "fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            product.setMainImage(fileName);
            Product savedProduct = productService.save(product);

            String uploadDir = "../" + FileUploadUtil.PRODUCT_DIR_NAME + savedProduct.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            product.setMainImage("product-image.png");
            productService.save(product);
        }

        redirectAttributes.addFlashAttribute("message", "The product was saved successfully");

        return getRedirectURLToAffectedProduct(product);
    }

    private String getRedirectURLToAffectedProduct(Product product) {
        return "redirect:/products/page/1?sortField=id&sortDir=asc&keyword=" + product.getId();
    }

    @GetMapping("/products/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("mod", "edit");

            return "/product/product_form";
        } catch (ProductNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/products";
        }

    }

    @GetMapping("/products/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
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
