package com.nminhthang.admin.brand.controller;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.brand.BrandNotFoundException;
import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.brand.exporter.BrandCSVExporter;
import com.nminhthang.admin.category.CategoryService;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
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
import java.util.List;
import java.util.Objects;

@Controller
public class BrandController {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(model, 1, sortDir, null);
    }

    @GetMapping("/brands/page/{pageNum}")
    private String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<Brand> listBrandsPage = brandService.listByPage(pageNum, sortDir, keyword);
        List<Brand> listBrands = listBrandsPage.getContent();

        long startCount = (long) (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1;
        long endCount = startCount + BrandService.BRAND_PER_PAGE - 1;

        if (endCount > listBrandsPage.getTotalElements()) {
            endCount = listBrandsPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", listBrandsPage.getTotalElements());
        model.addAttribute("totalPages", listBrandsPage.getTotalPages());
        model.addAttribute("sortField", "name");

        return "brand/brands";
    }

    @GetMapping("brands/new")
    public String newBrand(Model model) {

        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Brand");

        return "brand/brand_form";
    }

    @PostMapping("brands/save")
    public String saveBrand(Brand brand, RedirectAttributes redirectAttributes,
                            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);

            String uploadDir = "../" + FileUploadUtil.BRAND_DIR_NAME + savedBrand.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (brand.getId() == null) {
                brand.setLogo("brand-logo.png");
            }
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand was saved successfully");

        return getRedirectURLToAffectBrand(brand);
    }

    private String getRedirectURLToAffectBrand(Brand brand) {
        return "redirect:/brands/page/1?sortField=id&sortDir=asc&keyword=" + brand.getId();
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
            model.addAttribute("listCategories", listCategories);

            return "brand/brand_form";
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/brands";
        }
    }


    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            String brandDir = "../brand-logos/" + id;
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message", "Brand by ID = " + id + " has been deleted");
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/brands";
    }

    @GetMapping("/brands/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Brand> listBrand = brandService.listAll();
        BrandCSVExporter exporter = new BrandCSVExporter();
        exporter.export(listBrand, response);
    }

}
