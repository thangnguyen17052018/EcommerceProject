package com.nminhthang.product;

import com.nminhthang.category.CategoryService;
import com.nminhthang.common.entity.Category;
import com.nminhthang.common.entity.Product;
import com.nminhthang.common.exception.CategoryNotFoundException;
import com.nminhthang.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                     Model model) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                               @PathVariable("pageNum") int pageNum,
                               Model model) throws CategoryNotFoundException {
        try {
            Category category = categoryService.getCategoryByAlias(alias);

            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            Page<Product> listProductsByCategory = productService.listByCategory(category.getId(), pageNum);
            List<Product> listProducts = listProductsByCategory.getContent();
            System.out.println(listProducts.size());
            long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

            if (endCount > listProductsByCategory.getTotalElements()) {
                endCount = listProductsByCategory.getTotalElements();
            }

            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", listProductsByCategory.getTotalElements());
            model.addAttribute("totalPages", listProductsByCategory.getTotalPages());
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("category", category);
            return "product/product_by_category";
        } catch (CategoryNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
        try {
            Product product = productService.getProductByAlias(alias);

            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getShortName());

            return "product/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model) {
        return searchByPage(keyword, model,1);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword, Model model,
                         @PathVariable("pageNum") int pageNum) {
        Page<Product> searchProductResults = productService.search(keyword, pageNum);
        List<Product> listProductResults = searchProductResults.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;

        if (endCount > searchProductResults.getTotalElements()) {
            endCount = searchProductResults.getTotalElements();
        }

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", searchProductResults.getTotalElements());
        model.addAttribute("totalPages", searchProductResults.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProductResults", listProductResults);
        model.addAttribute("pageTitle",keyword + " - Search Results");

        return "product/search_result";
    }

}
