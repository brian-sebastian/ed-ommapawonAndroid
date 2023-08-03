package com.example.otp.response;

import com.example.otp.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAuth{
    @SerializedName("data")
    @Expose
    private User konsumen;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;

    public User getKonsumen() {
        return konsumen;
    }

    public void setKonsumen(User konsumen) {
        this.konsumen = konsumen;
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


}
