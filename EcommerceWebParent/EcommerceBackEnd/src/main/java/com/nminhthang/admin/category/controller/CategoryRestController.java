package com.nminhthang.admin.category.controller;

import com.nminhthang.admin.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/categories/check_name_alias")
    public String checkDuplicateEmail(Integer id, String name, String alias) {
        return categoryService.isCategoryUnique(id, name, alias);
    }

}
