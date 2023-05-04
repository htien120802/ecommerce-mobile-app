package com.example.ecommerce_mobile_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignInRequest implements Serializable {

    private String usernameOrEmail;

    private String password;

    public SignInRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
