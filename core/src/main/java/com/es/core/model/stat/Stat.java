package com.es.core.model.stat;

import java.util.HashMap;
import java.util.Map;

public class Stat {
    private String brand;
    private Map<String,String> qtyMap = new HashMap<>();

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Map<String, String> getQtyMap() {
        return qtyMap;
    }

    public void setQtyMap(Map<String, String> qtyMap) {
        this.qtyMap = qtyMap;
    }
}
