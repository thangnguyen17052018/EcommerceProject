package com.nminhthang.admin.product;


import com.nminhthang.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    public static final int PRODUCT_PER_PAGE = 8;

    @Autowired
    ProductRepository productRepository;

    public List<Product> listAll() {
        Sort sort = Sort.by("name").ascending();

        return (List<Product>) productRepository.findAll(sort);
    }

    public Page<Product> listByPage(int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);

        if (keyword != null) {
            return productRepository.findAllBy(keyword, pageable);
        } else {
            return productRepository.findAllBy(pageable);
        }

    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ProductNotFoundException("Could not find any product with id = " + id);
        }
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long productCountById = productRepository.countById(id);

        if (productCountById == null || productCountById == 0)
            throw new ProductNotFoundException("Could not find any product with id = " + id);

        productRepository.deleteById(id);
    }

    public String checkProductUnique(Integer id, String name, String alias) {
        Product productByName = productRepository.findByName(name);
        Product productByAlias = productRepository.findByAlias(alias);

        boolean isCreatingNew = (id == null || id == 0);

        if (isCreatingNew) {
            if (productByName != null)
                return "Duplicate product name";
            else if (productByAlias != null)
                return "Duplicate product alias";
        } else {
            if (productByName != null && productByName.getId() != id) {
                return "Duplicate product name";
            }
            if (productByAlias != null && productByAlias.getId() != id) {
                return "Duplicate product alias";
            }
        }

        return "OK";
    }

    @Transactional
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

}
