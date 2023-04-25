package com.example.ecommerce_mobile_app.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String name;
    private boolean enabled;
    private String allParentIds;
    private String imagePath;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAllParentIds() {
        return allParentIds;
    }

    public void setAllParentIds(String allParentIds) {
        this.allParentIds = allParentIds;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
