package com.nminhthang.admin.setting;

import com.nminhthang.common.entity.Setting;
import com.nminhthang.common.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findAllBySettingCategory(SettingCategory settingCategory);

}
