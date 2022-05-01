package com.nminhthang.admin.product;

import com.nminhthang.admin.brand.BrandRepository;
import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
import com.nminhthang.common.entity.product.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 10);
        Category category = entityManager.find(Category.class, 5);

        Product product = Product.builder()
                                .name("Harley bed")
                                .alias("harley-bed")
                                .shortDescription("A good bed for sleeping")
                                .fullDescription("This is a very good bed compatible for your sleppiness")
                                .brand(brand)
                                .category(category)
                                .createdTime(new Date())
                                .updatedTime(new Date())
                                .price(123)
                                .build();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProduct1() {
        Brand brand = entityManager.find(Brand.class, 8);
        Category category = entityManager.find(Category.class, 2);

        Product product = Product.builder()
                .name("Kymdan chair")
                .alias("kymdan-chair")
                .shortDescription("A good chair")
                .fullDescription("This is a very good chair compatible for your sleppiness")
                .brand(brand)
                .category(category)
                .createdTime(new Date())
                .updatedTime(new Date())
                .enabled(true)
                .quantityInStock(1)
                .price(456)
                .build();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProduct2() {
        Brand brand = entityManager.find(Brand.class, 5);
        Category category = entityManager.find(Category.class, 4);

        Product product = Product.builder()
                .name("Comfort table")
                .alias("comfort-table")
                .shortDescription("A good table for sleeping")
                .fullDescription("This is a very good table compatible for your sleppiness")
                .brand(brand)
                .category(category)
                .createdTime(new Date())
                .updatedTime(new Date())
                .enabled(false)
                .quantityInStock(0)
                .price(133)
                .build();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProduct3() {
        Brand brand = entityManager.find(Brand.class, 14);
        Category category = entityManager.find(Category.class, 9);

        Product product = Product.builder()
                .name("Tom chair")
                .alias("tom-chair")
                .shortDescription("A good bridge for sleeping")
                .fullDescription("This is a very good bridge compatible for your sleppiness")
                .brand(brand)
                .category(category)
                .createdTime(new Date())
                .updatedTime(new Date())
                .price(1555)
                .build();

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProduct() {
        Iterable<Product> listProducts = productRepository.findAll();

        listProducts.forEach(System.out::println);
    }

    @Test
    public void testGetAProduct() {
        Integer id = 2;
        Optional<Product> product = productRepository.findById(id);
        System.out.println(product);

        assertThat(product.isPresent()).isTrue();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 1;
        Product product = productRepository.findById(id).get();

        product.setPrice(789);
        productRepository.save(product);

        Product savedProduct = entityManager.find(Product.class, id);

        assertThat(savedProduct.getPrice()).isEqualTo(789);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 3;

        productRepository.deleteById(id);

        Optional<Product> product = productRepository.findById(id);

        assertThat(product.isPresent()).isFalse();
    }

    @Test
    public void testSaveProductWithImages() {
        Integer productId = 12;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image.png");
        product.addExtraImage("extra image 1.png");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra-image-3.png");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isGreaterThan(0);

    }

    @Test
    public void testSaveProductWithDetails() {
        Integer productId = 11;
        Product product = productRepository.findById(productId).get();

        product.addDetail("Device Memory", "128 GB");
        product.addDetail("CPU Model", "MediaTeck");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isGreaterThan(0);

    }
}
