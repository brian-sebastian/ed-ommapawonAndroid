package com.example.otp.response;

import com.example.otp.models.Kategori;
import com.example.otp.models.Menu;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMenu {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("restoran_menu")
    @Expose
    private List<Menu> restoran_menu = null;
    @SerializedName("restoran_kategori")
    @Expose
    private List<Kategori> restoran_kategori = null;

    public List<Kategori> getRestoran_kategori() {
        return restoran_kategori;
    }

    public void setRestoran_kategori(List<Kategori> restoranKategori) {
        this.restoran_kategori = restoranKategori;
    }
    
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

    public List<Menu> getRestoran_menu() {
        return restoran_menu;
    }

    public void setRestoran_menu(List<Menu> restoran_menu) {
        this.restoran_menu = restoran_menu;
    }
}
