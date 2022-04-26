package com.nminhthang.site.product;

import com.nminhthang.common.entity.product.Product;
import com.nminhthang.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindProductByAlias() {
        String alias = "Amein-Accent-Cabinet";
        Product product = productRepository.findByAlias(alias);

        Assertions.assertThat(product).isNotNull();
    }

}
