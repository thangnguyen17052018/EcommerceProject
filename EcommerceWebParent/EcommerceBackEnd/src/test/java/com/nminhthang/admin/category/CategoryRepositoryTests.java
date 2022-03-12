package com.nminhthang.admin.category;

import com.nminhthang.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory() {
        Category category = Category.builder().name("Electronic").alias("electronic").image("default.png").enabled(true).build();
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parentCategory = Category.builder().id(1).build();
        Category subCategory = Category.builder().name("Desktop").alias("desktop").image("default.png").enabled(true).parent(parentCategory).build();
        Category savedCategory = categoryRepository.save(subCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategories(){
        Category parentCategory = Category.builder().id(1).build();
        Category laptopCategory = Category.builder().name("Laptop").alias("laptop").image("default.png").enabled(true).parent(parentCategory).build();
        Category componentCategory = Category.builder().name("Component").alias("component").image("default.png").enabled(true).parent(parentCategory).build();

        Iterable<Category> savedCategory = categoryRepository.saveAll(List.of(laptopCategory, componentCategory));

        assertThat(savedCategory.iterator().hasNext()).isTrue();
    }

    @Test
    public void testCreateAnotherSubCategories(){
        Category parentCategory = Category.builder().id(4).build();
        Category cameraCategory = Category.builder().name("Camera").alias("camera").image("default.png").enabled(true).parent(parentCategory).build();
        Category smartphoneCategory = Category.builder().name("Smartphone").alias("smartphone").image("default.png").enabled(true).parent(parentCategory).build();

        Iterable<Category> savedCategory = categoryRepository.saveAll(List.of(cameraCategory, smartphoneCategory));

        assertThat(savedCategory.iterator().hasNext()).isTrue();
    }

    @Test
    public void testCreateSubCategoryOfSubCategory(){
        Category parentCategory = Category.builder().id(7).build();
        Category memoryCategory = Category.builder().name("Memory").alias("memory").image("default.png").enabled(true).parent(parentCategory).build();

        Category savedCategory = categoryRepository.save(memoryCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateAnotherSubCategoryOfSubCategory(){
        Category parentCategory = Category.builder().id(6).build();
        Category gamingLaptopCategory = Category.builder().name("Gaming Laptops").alias("gaming-laptops").image("default.png").enabled(true).parent(parentCategory).build();

        Category savedCategory = categoryRepository.save(gamingLaptopCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        Category category = categoryRepository.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        children.forEach(child -> System.out.println(child.getName()));

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testGetCategoryOfSubCategory(){
        Category category = categoryRepository.findById(4).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        children.forEach(child -> System.out.println(child.getName()));

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories){
            if (category.getParent() == null){
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                children.forEach(subCategory -> {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                });
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children){
            IntStream.range(0, newSubLevel)
                    .forEach(i -> System.out.print("--"));
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }

    }

    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = categoryRepository.listRootCategories();
        rootCategories.forEach(rootCategory -> System.out.println(rootCategory.getName()));
    }

    @Test
    public void testFindCategoryByName(){
        String nameCategory = "Computer";

        Category category = categoryRepository.findCategoryByName(nameCategory);

        assertThat(category.getId()).isGreaterThan(0);
        assertThat(category.getName()).isEqualTo(nameCategory);
    }

    @Test
    public void testFindCategoryByAlias(){
        String aliasCategory = "desktop";

        Category category = categoryRepository.findCategoryByAlias(aliasCategory);

        assertThat(category.getId()).isGreaterThan(0);
        assertThat(category.getAlias()).isEqualTo(aliasCategory);
    }

}
