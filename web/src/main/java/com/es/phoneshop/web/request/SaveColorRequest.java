package com.es.phoneshop.web.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class SaveColorRequest {
    private Long id;
    @NotBlank(message = "The value is required")
    @Size(max = 50, message = "too long")
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
