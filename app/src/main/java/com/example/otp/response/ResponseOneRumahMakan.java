package com.example.otp.response;

import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseOneRumahMakan {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
//    @SerializedName("konsumen_balance")
//    @Expose
//    private String konsumen_balance;
//    @SerializedName("rumah_makan")
//    @Expose
//    private  RumahMakan rumah_makan;
    @SerializedName("data")
    @Expose
    private RumahMakan data;
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

//    public String getKonsumen_balance() {
//        return konsumen_balance;
//    }
//
//    public void setKonsumen_balance(String konsumen_balance) {
//        this.konsumen_balance = konsumen_balance;
//    }

    public RumahMakan getData() {
        return data;
    }

    public void setData(RumahMakan data) {
        this.data = data;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

//    public RumahMakan getRumah_makan() {
//        return rumah_makan;
//    }
//
//    public void setRumah_makan(RumahMakan rumah_makan) {
//        this.rumah_makan = rumah_makan;
//    }

}
