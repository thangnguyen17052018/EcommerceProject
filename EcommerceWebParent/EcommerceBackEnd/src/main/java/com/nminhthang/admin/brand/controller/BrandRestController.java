package com.nminhthang.admin.brand.controller;

import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {

    @Autowired
    BrandService brandService;

    @PostMapping("/brands/check_brand")
    public String checkDuplicateBrand(@Param("id") Integer id, @Param("name") String name) {
        System.out.println(brandService.checkBrandUnique(id, name));
        return brandService.checkBrandUnique(id, name);
    }

}
