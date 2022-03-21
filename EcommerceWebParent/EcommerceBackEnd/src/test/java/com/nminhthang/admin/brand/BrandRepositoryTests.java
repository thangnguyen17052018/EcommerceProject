package com.nminhthang.admin.brand;

import com.nminhthang.common.entity.Brand;
import com.nminhthang.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateBrandWithOneCategory() {
        Brand brand = Brand.builder()
                    .name("Acer")
                    .logo("brand-logo.png")
                    .categories(new HashSet<>())
                    .build();

        Category category1 = entityManager.find(Category.class, 1);

        brand.addCategory(category1);

        Brand savedBrand = brandRepository.save(brand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrandWithTwoCategories() {
        Brand brand = Brand.builder()
                .name("Apple")
                .logo("brand-logo.png")
                .categories(new HashSet<>())
                .build();

        Category category1 = entityManager.find(Category.class, 3);
        Category category2 = entityManager.find(Category.class, 4);

        brand.addCategory(category1);
        brand.addCategory(category2);


        Brand savedBrand = brandRepository.save(brand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrandWithThreeCategories() {
        Brand brand = Brand.builder()
                .name("Samsung")
                .logo("brand-logo.png")
                .categories(new HashSet<>())
                .build();

        Category category1 = entityManager.find(Category.class, 5);
        Category category2 = entityManager.find(Category.class, 6);
        Category category3 = entityManager.find(Category.class, 8);

        brand.addCategory(category1);
        brand.addCategory(category2);
        brand.addCategory(category3);

        Brand savedBrand = brandRepository.save(brand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllBrand() {
        Iterable<Brand> listBrands = brandRepository.findAll();

        listBrands.forEach(System.out::println);

        assertThat(listBrands.iterator().hasNext()).isNotNull();
    }

    @Test
    public void testGetBrandById() {
        Optional<Brand> brand = brandRepository.findById(1);

        assertThat(brand.isPresent()).isTrue();
    }

    @Test
    public void testUpdateBrand() {
        Brand brand = brandRepository.findById(3).get();

        brand.setName("Samsung Electronics");
        Brand updatedBrand = brandRepository.save(brand);

        assertThat(updatedBrand.getName()).isEqualTo("Samsung Electronics");
    }

    @Test
    public void testDeleteBrand() {
        brandRepository.deleteById(3);

        Optional<Brand> brandSamsung = brandRepository.findById(3);

        assertThat(brandSamsung.isPresent()).isFalse();
    }


}
