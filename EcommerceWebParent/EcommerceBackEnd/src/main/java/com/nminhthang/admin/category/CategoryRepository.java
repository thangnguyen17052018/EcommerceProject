package com.nminhthang.admin.category;

import com.nminhthang.common.entity.Category;
import com.nminhthang.common.entity.User;
import org.apache.catalina.startup.Catalina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is null")
    Page<Category> findRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
    Page<Category> findCategoriesWithKeyword(String keyword, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parent.id is null")
    List<Category> listRootCategories(Sort sort);

    Category findCategoryByName(String name);

    Category findCategoryByAlias(String alias);

    Long countById(Integer id);

    @Modifying
    @Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :id")
    void updateEnabledStatus(Integer id, boolean enabled);
}
