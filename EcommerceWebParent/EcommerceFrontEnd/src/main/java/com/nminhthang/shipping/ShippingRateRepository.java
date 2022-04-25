package com.nminhthang.shipping;

import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    ShippingRate findByCountryAndState(Country country, String state);

}
