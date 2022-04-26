package com.nminhthang.product;

import com.nminhthang.common.entity.product.Product;
import com.nminhthang.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 8;
    public static final int SEARCH_RESULTS_PER_PAGE = 12;

    @Autowired
    ProductRepository productRepository;

    public Page<Product> listByCategory(Integer categoryId, int pageNum) {
        String categoryIdMatch = "-" + categoryId + "-";
        System.out.println(categoryIdMatch);
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Product getProductByAlias(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with alias: " + alias);
        }
        return product;
    }

    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
        return productRepository.search(keyword, pageable);
    }

}
