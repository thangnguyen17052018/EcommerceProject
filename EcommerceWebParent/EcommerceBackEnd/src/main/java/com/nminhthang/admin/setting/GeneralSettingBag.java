package com.nminhthang.admin.setting;

import com.nminhthang.common.entity.Setting;
import com.nminhthang.common.entity.SettingBag;

import java.util.List;

public class GeneralSettingBag extends SettingBag {

    public GeneralSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String logoPath) {
        super.update("SITE_LOGO", logoPath);
    }
}
