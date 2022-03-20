package com.es.phoneshop.web.controller.pages.supplier;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.SavePhoneRequest;
import com.es.phoneshop.web.request.SupplyPhoneRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.Optional;

@Controller
public class SupplyPhonesPageController {

    @Resource
    private PhoneService phoneService;

    @GetMapping("/supplier/supply/{phoneId}")
    public String getSupplyPhonePage(@PathVariable Long phoneId,
                                     Model model) {
        Optional<Phone> optionalPhone = phoneService.getPhone(phoneId);
        Phone phone = optionalPhone.orElseThrow(PhoneNotFoundException::new);
        SupplyPhoneRequest request = new SupplyPhoneRequest();
        request.setId(phoneId);
        request.setStockRequested(phone.getStockRequested().toString());
        request.setStock(phone.getStock().toString());
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", request);
        }
        return "supplier/supplyPhone";
    }

    @PostMapping("/supplier/supply/{phoneId}")
    public String supplyPhone(@PathVariable Long phoneId,
                              @Valid @ModelAttribute("request") SupplyPhoneRequest request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        Optional<Phone> optionalPhone = phoneService.getPhone(phoneId);
        Phone phone = optionalPhone.orElseThrow(PhoneNotFoundException::new);
        request.setStockRequested(phone.getStockRequested().toString());
        if (result.hasErrors() || Integer.parseInt(request.getStock()) > phone.getStockRequested()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            if (Integer.parseInt(request.getStock()) > phone.getStockRequested()) {
                errors.put("stock", "must be no more than " + phone.getStockRequested());
            }
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            return "redirect:/supplier/supply/" + phoneId;
        }


        phone.setStock(phone.getStock() + Integer.parseInt(request.getStock()));
        phone.setStockRequested(phone.getStockRequested() - Integer.parseInt(request.getStock()));
        phoneService.savePhone(phone);
        return "redirect:/productList?query=" + phone.getModel();
    }
}
