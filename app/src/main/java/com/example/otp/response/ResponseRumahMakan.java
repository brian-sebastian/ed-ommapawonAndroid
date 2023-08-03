package com.example.otp.response;

import com.example.otp.models.RumahMakan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRumahMakan {
    @SerializedName("data")
    @Expose
    private List<RumahMakan> data = null;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;

    public List<RumahMakan> getData() {
        return data;
    }

    public void setData(List<RumahMakan> data) {
        this.data = data;
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
