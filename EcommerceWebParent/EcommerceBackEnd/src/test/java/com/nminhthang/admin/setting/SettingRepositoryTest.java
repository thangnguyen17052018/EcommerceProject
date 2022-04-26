package com.nminhthang.admin.setting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.nminhthang.common.entity.setting.Setting;
import com.nminhthang.common.entity.setting.SettingCategory;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

    @Autowired
    SettingRepository settingRepository;

    @Test
    public void testCreateGeneralSettingsSiteName() {
        Setting siteName = new Setting("SITE_NAME", "EcommerceNMinhThang", SettingCategory.GENERAL);
        Setting savedSiteName = settingRepository.save(siteName);

        Assertions.assertThat(savedSiteName).isNotNull();
        Assertions.assertThat(savedSiteName.getKey()).isEqualTo("SITE_NAME");
    }

    @Test
    public void testCreateGeneralSettingsSiteLogo() {
        Setting siteLogo = new Setting("SITE_LOGO", "logo.jpg", SettingCategory.GENERAL);
        Setting savedSiteLogo = settingRepository.save(siteLogo);

        Assertions.assertThat(savedSiteLogo).isNotNull();
        Assertions.assertThat(savedSiteLogo.getKey()).isEqualTo("SITE_LOGO");
    }

    @Test
    public void testCreateGeneralSettingsCopyright() {
        Setting copyright = new Setting("COPYRIGHT", "Copyright (T) 2022 NMINHTHANG", SettingCategory.GENERAL);
        Setting savedCopyright = settingRepository.save(copyright);

        Assertions.assertThat(savedCopyright).isNotNull();
        Assertions.assertThat(savedCopyright.getKey()).isEqualTo("COPYRIGHT");
    }

    @Test
    public void testCreateCurrencySettings() {
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        List<Setting> listCurrencySettings = List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType);
        Iterable<Setting> savedListCurrencySettings = settingRepository.saveAll(listCurrencySettings);

        Assertions.assertThat(savedListCurrencySettings).isNotEmpty();
    }

    @Test
    public void testListAllGeneralSettings() {
        List<Setting> listGeneralSettings = settingRepository.findAllBySettingCategory(SettingCategory.GENERAL);

        Assertions.assertThat(listGeneralSettings.size()).isGreaterThan(0);
        listGeneralSettings.forEach(System.out::println);
    }

}
