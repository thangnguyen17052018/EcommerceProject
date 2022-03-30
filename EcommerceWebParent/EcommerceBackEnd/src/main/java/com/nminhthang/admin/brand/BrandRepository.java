package com.nminhthang.admin.brand;

import com.nminhthang.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    @Query("SELECT b FROM Brand b")
    Page<Brand> findAllBy(Pageable pageable);

    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id, ' ', b.name) LIKE %?1%")
    Page<Brand> findAllBy(String keyword, Pageable pageable);

    Long countById(Integer id);

    Brand findByName(String name);

    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    List<Brand> findAll();

}
