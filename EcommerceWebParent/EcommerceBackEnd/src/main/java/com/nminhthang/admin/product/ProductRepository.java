package com.nminhthang.admin.product;

import com.nminhthang.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p")
    Page<Product> findAllBy(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.id, ' ', p.name, ' ', p.alias, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.category.name, ' ', p.brand.name) LIKE %?1%")
    Page<Product> findAllBy(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%")
    Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) AND (CONCAT(p.id, ' ', p.name, ' ', p.alias, ' ', p.shortDescription, ' ', p.fullDescription, ' ', p.category.name, ' ', p.brand.name) LIKE %?3%)")
    Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword, Pageable pageable);

    Long countById(Integer id);

    Product findByName(String name);

    Product findByAlias(String alias);

    @Modifying
    @Query("UPDATE Product p SET p.enabled= :enabled WHERE p.id = :id")
    void updateEnabledStatus(Integer id, boolean enabled);
}

