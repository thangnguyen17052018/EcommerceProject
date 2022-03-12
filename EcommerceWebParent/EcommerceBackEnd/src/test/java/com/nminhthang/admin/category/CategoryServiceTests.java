package com.nminhthang.admin.category;

import com.nminhthang.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Computer";
        String alias = "abc";

        Category category = Category.builder()
                                    .name(name)
                                    .id(id)
                                    .alias(alias)
                                    .build();

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(category);

        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("Duplicate category name");

    }

}
