package com.nminhthang.category;

import com.nminhthang.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("")
//    public String viewHomePage(Model model) {
//        List<Category> listCategories = categoryService.listNoChildrenCategories();
//
//        model.addAttribute("listCategories", listCategories);
//        return "index";
//    }
}
