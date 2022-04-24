package com.nminhthang.category;

import com.nminhthang.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.enabled=true ORDER BY c.name ASC")
    List<Category> findAllEnabled();

    @Query("SELECT c FROM Category c WHERE c.enabled=true AND c.alias=?1")
    Category findByAliasAndEnabled(String alias);

}
