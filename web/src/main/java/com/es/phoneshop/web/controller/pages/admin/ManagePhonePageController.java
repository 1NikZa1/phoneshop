package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.SavePhoneRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ManagePhonePageController {

    @Resource
    private PhoneService phoneService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setPhoneService(phoneService);
        binder.registerCustomEditor(Color.class, colorEditor);
    }

    @GetMapping(value = {"/admin/addPhone", "/admin/addPhone/{phoneId}"})
    public String getUpdatePhonePage(@PathVariable(required = false) Long phoneId,
                                     Model model) {

        model.addAttribute("colors", phoneService.getColors());
        model.addAttribute("brands", phoneService.getAllBrands());
        model.addAttribute("deviceTypes", phoneService.getAllDeviceTypes());
        model.addAttribute("opSystems", phoneService.getAllOpSystems());

        if (phoneId == null) {
            model.addAttribute("isNewPhone", true);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", new SavePhoneRequest());
            }
        } else {
            Phone phone = phoneService.getPhone(phoneId).orElseThrow(PhoneNotFoundException::new);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", phone);
            }
        }

        return "admin/addPhone";
    }

    @GetMapping("/admin/deletePhone/{phoneId}")
    public String deleteBrand(@PathVariable Long phoneId, RedirectAttributes redirectAttributes) {

        Phone phone = phoneService.getPhone(phoneId).orElseThrow(PhoneNotFoundException::new);
        phoneService.delete(phone);
        redirectAttributes.addFlashAttribute("msg", phone.getModel()+" was deleted");

        return "redirect:/productList";
    }

    @PostMapping(value = {"/admin/addPhone", "/admin/addPhone/{phoneId}"})
    public String savePhone(@PathVariable(required = false) Long phoneId,
                            @Valid @ModelAttribute("request") SavePhoneRequest request,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) throws ParseException {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            if (phoneId == null) {
                return "redirect:/admin/addPhone";
            }
            return "redirect:/admin/addPhone/" + phoneId;
        }
        Phone phone;
        if (phoneId == null) {
            phone = new Phone();
        } else {
            phone = phoneService.getPhone(phoneId).orElseThrow(PhoneNotFoundException::new);
        }
        populatePhone(phone, request);
        phoneService.savePhone(phone);

        return "redirect:/productDetails/" + phone.getId();
    }

    private void populatePhone(Phone phone, SavePhoneRequest request) throws ParseException {
        phone.setBrand(request.getBrand());
        phone.setModel(request.getModel());
        phone.setPrice(new BigDecimal(request.getPrice()));
        phone.setDisplaySizeInches(new BigDecimal(request.getDisplaySizeInches()));
        phone.setWeightGr(Integer.valueOf(request.getWeightGr()));
        phone.setLengthMm(new BigDecimal(request.getLengthMm()));
        phone.setWidthMm(new BigDecimal(request.getWidthMm()));
        phone.setHeightMm(new BigDecimal(request.getHeightMm()));
        phone.setAnnounced(new SimpleDateFormat("yyyy-MM-dd").parse(request.getAnnounced()));
        phone.setDeviceType(request.getDeviceType());
        phone.setOs(request.getOs());
        phone.setColors(request.getColors());
        phone.setDisplayResolution(request.getDisplayResolution());
        phone.setPixelDensity(Integer.valueOf(request.getPixelDensity()));
        phone.setDisplayTechnology(request.getDisplayTechnology());
        phone.setBackCameraMegapixels(new BigDecimal(request.getBackCameraMegapixels()));
        phone.setFrontCameraMegapixels(new BigDecimal(request.getFrontCameraMegapixels()));
        phone.setRamGb(new BigDecimal(request.getRamGb()));
        phone.setInternalStorageGb(new BigDecimal(request.getInternalStorageGb()));
        phone.setBatteryCapacityMah(Integer.valueOf(request.getBatteryCapacityMah()));
        phone.setBluetooth(request.getBluetooth());
        phone.setPositioning(request.getPositioning());
        phone.setImageUrl(request.getImageUrl());
        phone.setDescription(request.getDescription());
        phone.setStockRequested(Integer.valueOf(request.getStockRequested()));
    }
}

