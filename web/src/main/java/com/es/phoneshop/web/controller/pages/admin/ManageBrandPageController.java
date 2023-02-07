package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Brand;
import com.es.core.model.phone.Color;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.SaveBrandRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ManageBrandPageController {

    @Resource
    private PhoneService phoneService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setPhoneService(phoneService);
        binder.registerCustomEditor(Color.class, colorEditor);
    }

    @GetMapping("/admin/brands")
    public String getAllBrandsPage(Model model) {
        model.addAttribute("brands", phoneService.getAllBrands());
        return "admin/brands";
    }

    @GetMapping(value = {"/admin/addBrand", "/admin/addBrand/{brandId}"})
    public String getUpdateBrandPage(@PathVariable(required = false) Long brandId,
                                     Model model) {

        model.addAttribute("brands", phoneService.getAllBrands());

        if (brandId == null) {
            model.addAttribute("isNewBrand", true);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", new Brand());
            }
        } else {
            Brand brand = phoneService.getBrand(brandId).orElseThrow(PhoneNotFoundException::new);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", brand);
            }
        }

        return "admin/addBrand";
    }

    @PostMapping(value = {"/admin/addBrand", "/admin/addBrand/{brandId}"})
    public String saveBrand(@PathVariable(required = false) Long brandId,
                            @Valid @ModelAttribute("request") SaveBrandRequest request,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            if (brandId == null) {
                return "redirect:/admin/addBrand";
            }
            return "redirect:/admin/addBrand/" + brandId;
        }
        Brand brand;
        if (brandId == null) {
            brand = new Brand();
        } else {
            brand = phoneService.getBrand(brandId).orElseThrow(PhoneNotFoundException::new);
        }
        populateBrand(brand, request);

        List<String> brandNames = phoneService.getAllBrands().values().stream()
                .map(value -> value.trim().toLowerCase())
                .collect(Collectors.toList());
        if (brandId == null && brandNames.stream().anyMatch(value -> value.trim().equalsIgnoreCase(brand.getName()))) {
            redirectAttributes.addFlashAttribute("error", "Already exist");
            return "redirect:/admin/addBrand";
        }
        phoneService.saveBrand(brand);

        return "redirect:/admin/brands";
    }

    @GetMapping("/admin/deleteBrand/{brandId}")
    public String deleteBrand(@PathVariable Long brandId,
                            RedirectAttributes redirectAttributes) {

        Brand brand = phoneService.getBrand(brandId).orElseThrow(PhoneNotFoundException::new);
        try {
            phoneService.deleteBrand(brand);
            redirectAttributes.addFlashAttribute("msg", "brand " + brand.getName() + " was deleted");
        }catch (Exception exception){
            redirectAttributes.addFlashAttribute("msg", "brand " + brand.getName() + " in use");
        }

        return "redirect:/admin/brands";
    }

    void populateBrand(Brand brand, SaveBrandRequest request) {
        brand.setName(request.getName().trim());
    }
}

