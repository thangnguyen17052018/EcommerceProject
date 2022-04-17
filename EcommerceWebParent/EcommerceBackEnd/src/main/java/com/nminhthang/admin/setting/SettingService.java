package com.nminhthang.admin.setting;

import com.nminhthang.common.entity.Setting;
import com.nminhthang.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    SettingRepository settingRepository;

    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    public void saveAll(Iterable<Setting> listSettings) {
        settingRepository.saveAll(listSettings);
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> generalSettings =  new ArrayList<>();
        List<Setting> generalSettingCategory = settingRepository.findAllBySettingCategory(SettingCategory.GENERAL);
        List<Setting> currencySettingCategory = settingRepository.findAllBySettingCategory(SettingCategory.CURRENCY);

        generalSettings.addAll(generalSettingCategory);
        generalSettings.addAll(currencySettingCategory);

        return new GeneralSettingBag(generalSettings);
    }

    public List<Setting> getMailServerSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.MAIL_TEMPLATE);
    }

}
