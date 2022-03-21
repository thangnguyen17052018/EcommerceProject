package com.nminhthang.admin.brand;

import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    @Query("SELECT b FROM Brand b")
    Page<Brand> findAllBy(Pageable pageable);

    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id, ' ', b.name) LIKE %?1%")
    Page<Brand> findAllBy(String keyword, Pageable pageable);

    Long countById(Integer id);

    Brand findByName(String name);
}
