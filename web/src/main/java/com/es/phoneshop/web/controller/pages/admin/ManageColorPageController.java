package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Color;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.SaveColorRequest;
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
public class ManageColorPageController {

    @Resource
    private PhoneService phoneService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        ColorEditor colorEditor = new ColorEditor();
        colorEditor.setPhoneService(phoneService);
        binder.registerCustomEditor(Color.class, colorEditor);
    }

    @GetMapping("/admin/colors")
    public String getAllColorsPage(Model model) {
        model.addAttribute("colors", phoneService.getColors());
        return "admin/colors";
    }

    @GetMapping(value = {"/admin/addColor", "/admin/addColor/{colorId}"})
    public String getUpdateColorPage(@PathVariable(required = false) Long colorId,
                                     Model model) {

        model.addAttribute("colors", phoneService.getColors());

        if (colorId == null) {
            model.addAttribute("isNewColor", true);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", new Color());
            }
        } else {
            Color color = phoneService.getColor(colorId).orElseThrow(PhoneNotFoundException::new);
            if (!model.containsAttribute("request")) {
                model.addAttribute("request", color);
            }
        }

        return "admin/addColor";
    }

    @PostMapping(value = {"/admin/addColor", "/admin/addColor/{colorId}"})
    public String saveBrand(@PathVariable(required = false) Long colorId,
                            @Valid @ModelAttribute("request") SaveColorRequest request,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(v -> errors.put(v.getField(), v.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("request", request);
            if (colorId == null) {
                return "redirect:/admin/addColor";
            }
            return "redirect:/admin/addColor/" + colorId;
        }
        Color color;
        if (colorId == null) {
            color = new Color();
        } else {
            color = phoneService.getColor(colorId).orElseThrow(PhoneNotFoundException::new);
        }
        populateBrand(color, request);

        List<String> colorCodes = phoneService.getColors().stream()
                .map(col -> col.getCode().trim().toLowerCase())
                .collect(Collectors.toList());
        if (colorId == null && colorCodes.stream().anyMatch(value -> value.trim().equalsIgnoreCase(color.getCode()))) {
            redirectAttributes.addFlashAttribute("error", "Already exist");
            return "redirect:/admin/addColor";
        }
        phoneService.saveColor(color);

        return "redirect:/admin/colors";
    }

    @GetMapping("/admin/deleteColor/{colorId}")
    public String deleteBrand(@PathVariable Long colorId,
                            RedirectAttributes redirectAttributes) {

        Color color = phoneService.getColor(colorId).orElseThrow(PhoneNotFoundException::new);
        try {
            phoneService.deleteColor(color);
            redirectAttributes.addFlashAttribute("msg", "color " + color.getCode() + " was deleted");
        }catch (Exception exception){
            redirectAttributes.addFlashAttribute("msg", "color " + color.getCode() + " in use");
        }

        return "redirect:/admin/colors";
    }

    void populateBrand(Color color, SaveColorRequest request) {
        color.setCode(request.getCode().trim());
    }
}

