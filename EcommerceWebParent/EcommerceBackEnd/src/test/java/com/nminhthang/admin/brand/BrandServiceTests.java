package com.nminhthang.admin.brand;

import com.nminhthang.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

    @MockBean
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate() {
        Integer id = null;
        String name = "Acer";

        Brand brand = Brand.builder()
                            .name(name)
                            .categories(new HashSet<>())
                            .build();

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkBrandUnique(id, name);
        assertThat(result).isEqualTo("Duplicate brand name");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "Acer";

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result = brandService.checkBrandUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate() {
        Integer id = 1;
        String name = "Cannon";

        Brand brand = Brand.builder()
                .id(5)
                .name(name)
                .categories(new HashSet<>())
                .build();

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkBrandUnique(id, name);
        assertThat(result).isEqualTo("Duplicate brand name");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String name = "Cannon";

        Brand brand = Brand.builder()
                .id(1)
                .name(name)
                .categories(new HashSet<>())
                .build();

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result = brandService.checkBrandUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

}
