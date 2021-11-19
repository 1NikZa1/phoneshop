package com.es.phoneshop.web.controller.pages;

import com.es.core.service.phone.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    private static final int PHONES_PER_PAGE = 7;

    @Resource
    private PhoneService phoneService;

    @GetMapping
    public String showProductList(@RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "sort", required = false) String sort,
                                  @RequestParam(value = "order", required = false) String order,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  Model model) {

        int countPhones = phoneService.countPhones(query);
        int pagesTotal;

        if (countPhones % PHONES_PER_PAGE == 0) {
            pagesTotal = countPhones / PHONES_PER_PAGE;
        } else {
            pagesTotal = countPhones / PHONES_PER_PAGE + 1;
        }

        model.addAttribute("phones", phoneService.findPhones(query, sort, order, page, PHONES_PER_PAGE));
        model.addAttribute("currentPage", page);
        model.addAttribute("pagesTotal", pagesTotal);
        return "productList";
    }
}
