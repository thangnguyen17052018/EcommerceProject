package com.nminhthang.site.category;

import com.nminhthang.category.CategoryRepository;
import com.nminhthang.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategories() {
        List<Category> listCategories = categoryRepository.findAllEnabled();
        listCategories.forEach(category -> {
            System.out.println(category.getName() + " - status: " + category.isEnabled());
        });
    }

}
