package com.es.phoneshop.web.request;


import java.util.ArrayList;
import java.util.List;

public class QuickAddRequest {
    private List<QuickItemRequest> items = new ArrayList<>();

    public List<QuickItemRequest> getItems() {
        return items;
    }

    public void setItems(List<QuickItemRequest> items) {
        this.items = items;
    }
}
