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
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(null);
        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("Duplicate category name");

    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias(){
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";

        Category category = Category.builder()
                .name(name)
                .id(id)
                .alias(alias)
                .build();

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(category);
        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("Duplicate category alias");

    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "NameABC";
        String alias = "aliasABC";

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(null);
        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");

    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName(){
        Integer id = 1;
        String name = "Computer";
        String alias = "abc";

        Category category = Category.builder()
                .name(name)
                .id(2)
                .alias(alias)
                .build();

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(null);
        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("Duplicate category name");

    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";

        Category category = Category.builder()
                .name(name)
                .id(2)
                .alias(alias)
                .build();

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(category);
        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("Duplicate category alias");

    }

    @Test
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "aliasABC";

        Category category = Category.builder()
                .name(name)
                .id(id)
                .alias(alias)
                .build();

        Mockito.when(categoryRepository.findCategoryByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findCategoryByAlias(alias)).thenReturn(category);

        String result = categoryService.isCategoryUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");

    }

}
