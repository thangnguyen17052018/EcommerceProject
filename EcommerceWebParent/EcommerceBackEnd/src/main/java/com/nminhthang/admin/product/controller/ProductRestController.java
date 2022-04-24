package com.nminhthang.admin.product.controller;

import com.nminhthang.admin.brand.BrandService;
import com.nminhthang.admin.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    @Autowired
    ProductService productService;

    @PostMapping("/products/check_product")
    public String checkDuplicateProduct(Integer id, String name, String alias) {
        return productService.checkProductUnique(id, name, alias);
    }

}
