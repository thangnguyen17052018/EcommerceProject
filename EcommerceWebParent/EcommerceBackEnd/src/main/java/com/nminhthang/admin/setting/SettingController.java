package com.nminhthang.admin.setting;

import com.nminhthang.admin.FileUploadUtil;
import com.nminhthang.admin.currency.CurrencyRepository;
import com.nminhthang.common.entity.Currency;
import com.nminhthang.common.entity.setting.Setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    SettingService settingService;

    @Autowired
    CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> listSettings = settingService.listAllSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listSettings", listSettings);
        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "setting/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam(name = "fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) throws IOException {

        GeneralSettingBag generalSettingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, generalSettingBag);
        saveCurrencySymbol(request, generalSettingBag);
        updateSettingValueFromForm(request, generalSettingBag.list());

        redirectAttributes.addFlashAttribute("message", "General settings have been saved");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            generalSettingBag.updateSiteLogo(value);
            String uploadDir = "../site-logo/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));

        Optional<Currency> currencyFoundById = currencyRepository.findById(currencyId);

        if (currencyFoundById.isPresent()) {
            Currency currency = currencyFoundById.get();
            generalSettingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValueFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }

        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateSettingValueFromForm(request, mailServerSettings);

        redirectAttributes.addFlashAttribute("message", "Mail server settings has been saved");

        return "redirect:/settings#mailServer";
    }

    @PostMapping("/settings/save_mail_template")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
        updateSettingValueFromForm(request, mailTemplateSettings);

        redirectAttributes.addFlashAttribute("message", "Mail template settings has been saved");

        return "redirect:/settings#mailTemplates";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValueFromForm(request, paymentSettings);

        redirectAttributes.addFlashAttribute("message", "Payment settings has been saved");

        return "redirect:/settings#payment";
    }

}
