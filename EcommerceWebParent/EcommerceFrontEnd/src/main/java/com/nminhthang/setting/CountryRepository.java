package com.nminhthang.setting;

import com.nminhthang.common.entity.Country;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();
    
    @Query("SELECT c FROM Country c WHERE c.code = ?1")
    Country findByCode(String code);
}
