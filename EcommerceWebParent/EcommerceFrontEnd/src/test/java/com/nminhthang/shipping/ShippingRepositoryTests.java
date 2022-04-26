package com.nminhthang.shipping;

import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.ShippingRate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRepositoryTests {

    @Autowired private ShippingRateRepository repository;

    @Test
    public void testFindByCountryAndState() {
        Country usa = new Country(234);
        String state = "New York";

        ShippingRate shippingRate = repository.findByCountryAndState(usa, state);

        Assertions.assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }

}
