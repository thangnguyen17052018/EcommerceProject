package com.nminhthang.admin.setting;

import com.nminhthang.common.entity.Setting;
import com.nminhthang.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    SettingRepository settingRepository;

    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    public void saveAll(List<Setting> listSettings) {
        settingRepository.saveAll(listSettings);
    }

    public List<Setting> getGeneralSettings() {
        return settingRepository.findAllBySettingCategory(SettingCategory.GENERAL);
    }

}
