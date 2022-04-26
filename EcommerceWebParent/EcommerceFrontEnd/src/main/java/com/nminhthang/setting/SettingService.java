package com.nminhthang.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nminhthang.common.entity.setting.Setting;
import com.nminhthang.common.entity.setting.SettingCategory;

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

    public List<Setting> getSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings() {
        List<Setting> settings = settingRepository.findAllBySettingCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findAllBySettingCategory(SettingCategory.MAIL_TEMPLATE));

        return new EmailSettingBag(settings);
    }

}
