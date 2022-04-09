package com.nminhthang.setting;

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

    public List<Setting> getSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

}
