package com.example.otp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("nama_konsumen")
    private String KonsumenNama;

    @SerializedName("email")
    private String KonsumenEmail;

    @SerializedName("nomor_telpon")
    private String KonsumenPhone;

    @SerializedName("token")
    private String Token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKonsumenNama() {
        return KonsumenNama;
    }

    public void setKonsumenNama(String konsumenNama) {
        this.KonsumenNama = konsumenNama;
    }

    public String getKonsumenEmail() {
        return KonsumenEmail;
    }

    public void setKonsumenEmail(String konsumenEmail) {
        this.KonsumenEmail = konsumenEmail;
    }

    public String getKonsumenPhone() {
        return KonsumenPhone;
    }

    public void setKonsumenPhone(String konsumenPhone) {
        this.KonsumenPhone = konsumenPhone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

}
