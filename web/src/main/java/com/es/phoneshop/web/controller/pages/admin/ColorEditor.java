package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.service.phone.PhoneService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;

public class ColorEditor extends PropertyEditorSupport {

    @Resource
    private PhoneService phoneService;

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(phoneService.getColor(Long.parseLong(text)).get());
        }
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }
}
