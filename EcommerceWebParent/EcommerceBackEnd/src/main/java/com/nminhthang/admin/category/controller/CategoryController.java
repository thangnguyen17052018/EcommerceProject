package com.nminhthang.admin.category.controller;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.category.CategoryNotFoundException;
import com.nminhthang.admin.category.CategoryPageInfo;
import com.nminhthang.admin.category.CategoryService;
import com.nminhthang.admin.category.exporter.CategoryCSVExporter;
import com.nminhthang.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(model, 1, sortDir);
    }

//    public String listAll(@Param("sortDir") String sortDir, Model model) {
//
//        if (sortDir == null || sortDir.isEmpty()) {
//            sortDir = "asc";
//        }
//
//        List<Category> listCategories = categoryService.listAll();
//        String reverseSortDir = (sortDir.equals("asc")) ? "desc" : "asc";
//
//        model.addAttribute("listCategories", listCategories);
//        model.addAttribute("reverseSortDir", reverseSortDir);
//
//        return "/category/categories";
//    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();

        List<Category> listCategories = categoryService.listByPage(categoryPageInfo, pageNum, sortDir);

        long startCount = (long) (pageNum - 1) * CategoryService.CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;

        if (endCount > categoryPageInfo.getTotalElements())
            endCount = categoryPageInfo.getTotalElements();

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
//        model.addAttribute("keyword", keyword);

        return "/category/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Create New Category");
        model.addAttribute("mod", "new");

        return "/category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveUser(Category category, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);
            Category savedCategory = categoryService.save(category);
            String uploadDir = "../" + FileUploadUtil.CATEGORY_DIR_NAME + savedCategory.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            category.setImage("default.png");
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category was saved successfully");

        return getRedirectURLToAffectedCategory(category);
    }

    private String getRedirectURLToAffectedCategory(Category category) {
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + category.getId();
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listAll();
            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
            model.addAttribute("mod", "edit");

            return "/category/category_form";
        } catch (CategoryNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/categories";
        }

    }

    @GetMapping("/categories/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "Category by ID = " + id + " has been deleted");
        } catch (CategoryNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") boolean enabled,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = (enabled) ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been " + status);
        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listAll();
        CategoryCSVExporter exporter = new CategoryCSVExporter();
        exporter.export(listCategories, response);
    }

}
