package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.dao.iosu.IosuDao;
import com.es.core.service.phone.PhoneService;
import com.es.phoneshop.web.request.UnionStatRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class StatPageController {

    @Resource
    private IosuDao iosuDao;
    @Resource
    private PhoneService phoneService;

    @GetMapping("/admin/stat")
    public String getStat(Model model) {
        model.addAttribute("stats", iosuDao.getStat());
        return "admin/stat";
    }

    @GetMapping("/admin/unionstat")
    public String getUnionStat(Model model) {


        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new UnionStatRequest());
        }
        model.addAttribute("brands", phoneService.getAllBrands());
        model.addAttribute("deviceTypes", phoneService.getAllDeviceTypes());
        return "admin/unionstat";
    }

    @PostMapping("/admin/unionstat")
    public String renderUnionStat(@Valid @ModelAttribute("request") UnionStatRequest request, Model model) {
        model.addAttribute("brands", phoneService.getAllBrands());
        model.addAttribute("deviceTypes", phoneService.getAllDeviceTypes());
        model.addAttribute("stats", iosuDao.getUnionStat(request.getBrand(), request.getType()));
        return "/admin/unionstat";
    }
}
