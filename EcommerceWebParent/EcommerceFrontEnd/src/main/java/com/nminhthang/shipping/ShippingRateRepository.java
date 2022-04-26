package com.nminhthang.shipping;

import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.ShippingRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    @Query("SELECT s FROM ShippingRate s WHERE s.country=:country AND s.state LIKE %:state%")
    ShippingRate findByCountryAndState(Country country, String state);

}
