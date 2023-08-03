package com.example.otp.response;

import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSearch {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("rumah_makan")
    @Expose
    private List<RumahMakan> rumah_makan = null;
    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RumahMakan> getRumah_makan() {
        return rumah_makan;
    }

    public void setRumah_makan(List<RumahMakan> rumah_makan) {
        this.rumah_makan = rumah_makan;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }


}
