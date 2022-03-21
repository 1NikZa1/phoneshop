package com.es.phoneshop.web.request;

import javax.validation.constraints.Pattern;

public class SupplyPhoneRequest {
    private Long id;
    @Pattern(regexp = "[0-9]+", message = "int number")
    private String stock;

    @Pattern(regexp = "[0-9]+", message = "int number")
    private String stockRequested;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockRequested() {
        return stockRequested;
    }

    public void setStockRequested(String stockRequested) {
        this.stockRequested = stockRequested;
    }
}
