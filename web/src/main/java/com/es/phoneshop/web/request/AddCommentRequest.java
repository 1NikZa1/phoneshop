package com.es.phoneshop.web.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddCommentRequest {
    @NotBlank(message = "The value is required")
    @Size(max = 50, message = "too long")
    private String username;
    @NotBlank(message = "The value is required")
    @Size(max = 512, message = "too long")
    private String message;
    @NotNull
    private Long phoneId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
}
