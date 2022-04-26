package com.nminhthang.site.setting;

import com.nminhthang.common.entity.setting.Setting;
import com.nminhthang.common.entity.setting.SettingCategory;
import com.nminhthang.setting.SettingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

    @Autowired
    SettingRepository settingRepository;

    @Test
    public void testFindByTwoCategories() {
        List<Setting> listSettings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);

        listSettings.forEach(System.out::println);

        Assertions.assertThat(listSettings.size()).isGreaterThan(0);
    }

}
