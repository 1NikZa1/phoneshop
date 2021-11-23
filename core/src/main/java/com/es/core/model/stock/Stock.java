package com.es.core.model.stock;

import com.es.core.model.phone.Phone;

public class Stock {
    private Phone phone;
    private Integer stock;
    private Integer reserved;

    public Stock() {
    }

    public Stock(Phone phone, Integer stock, Integer reserved) {
        this.phone = phone;
        this.stock = stock;
        this.reserved = reserved;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }
}
