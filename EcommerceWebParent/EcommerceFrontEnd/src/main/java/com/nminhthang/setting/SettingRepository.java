package com.nminhthang.setting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nminhthang.common.entity.setting.Setting;
import com.nminhthang.common.entity.setting.SettingCategory;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    List<Setting> findAllBySettingCategory(SettingCategory settingCategory);

    @Query("SELECT s FROM Setting s WHERE s.settingCategory=?1 OR s.settingCategory=?2")
    List<Setting> findByTwoCategories(SettingCategory category1, SettingCategory category2);

}
