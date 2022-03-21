package com.es.phoneshop.web.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class AddCommentRequest {
    @NotBlank(message = "The value is required")
    @Size(max = 512, message = "length between 5 and 512", min = 5)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
