package com.example.ecommerce_mobile_app.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.ecommerce_mobile_app.BR;

import java.io.Serializable;

public class WishlistItem extends BaseObservable implements Serializable {
    private int productId;
    private String shortName;
    private String mobileName;
    private String mainImagePath;
    @Bindable
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
        notifyPropertyChanged(BR.productId);
    }
    @Bindable
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
        notifyPropertyChanged(BR.shortName);
    }
    @Bindable
    public String getMobileName() {
        return mobileName;
    }

    public void setMobileName(String mobileName) {
        this.mobileName = mobileName;
        notifyPropertyChanged(BR.mobileName);
    }
    @Bindable
    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
        notifyPropertyChanged(BR.mainImagePath);
    }
}
