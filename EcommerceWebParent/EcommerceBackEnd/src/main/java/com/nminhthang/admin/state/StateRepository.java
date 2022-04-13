package com.nminhthang.admin.state;

import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);

}
