package com.example.ecommerce_mobile_app.model;

import java.io.Serializable;

public class SignInResponse implements Serializable {
    private String response_message;
    private String response_description;
    private Customer data;

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getResponse_description() {
        return response_description;
    }

    public void setResponse_description(String response_description) {
        this.response_description = response_description;
    }

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }
}
