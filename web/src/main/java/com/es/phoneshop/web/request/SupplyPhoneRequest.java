package com.es.phoneshop.web.request;

import com.es.core.model.phone.Color;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

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
