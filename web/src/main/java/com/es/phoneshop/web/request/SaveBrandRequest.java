package com.es.phoneshop.web.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class SaveBrandRequest {
    private Long id;
    @NotBlank(message = "The value is required")
    @Size(max = 50, message = "too long")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
