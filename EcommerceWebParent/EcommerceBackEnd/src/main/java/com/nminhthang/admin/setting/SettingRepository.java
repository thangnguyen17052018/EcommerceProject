package com.nminhthang.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.nminhthang.common.entity.setting.Setting;
import com.nminhthang.common.entity.setting.SettingCategory;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findAllBySettingCategory(SettingCategory settingCategory);

}
