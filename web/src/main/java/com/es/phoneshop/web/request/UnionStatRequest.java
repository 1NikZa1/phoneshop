package com.es.phoneshop.web.request;

public class UnionStatRequest {
    private Long brand;
    private Long type;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getBrand() {
        return brand;
    }

    public void setBrand(Long brand) {
        this.brand = brand;
    }
}
