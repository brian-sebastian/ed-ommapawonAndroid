package com.example.otp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RumahMakan implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("restoran_nama")
    @Expose
    private String restoran_nama;
    @SerializedName("restoran_phone")
    @Expose
    private String restoran_phone;
    @SerializedName("restoran_email")
    @Expose
    private String restoran_email;
    @SerializedName("restoran_latitude")
    @Expose
    private String restoran_latitude;
    @SerializedName("restoran_longitude")
    @Expose
    private String restoran_longitude;
    @SerializedName("restoran_alamat")
    @Expose
    private String restoran_alamat;
    @SerializedName("restoran_deskripsi")
    @Expose
    private String restoran_deskripsi;
    @SerializedName("restoran_oprasional")
    @Expose
    private Integer restoran_oprasional;
    @SerializedName("jarak_pesanan")
    @Expose
    private Integer jarak_pesanan;
    @SerializedName("restoran_delivery")
    @Expose
    private String restoran_delivery;
    @SerializedName("restoran_delivery_tarif")
    @Expose
    private String restoran_delivery_tarif;
    @SerializedName("restoran_delivery_minimum")
    @Expose
    private String restoran_delivery_minimum;
    @SerializedName("restoran_pajak_pb_satu")
    @Expose
    private Integer restoran_pajak_pb_satu;
    @SerializedName("jumlah_kurir")
    @Expose
    private Integer jumlahKurir;
    @SerializedName("restoran_distace")
    @Expose
    private String restoran_distace;
    @SerializedName("restoran_order")
    @Expose
    private Integer restoran_order;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestoran_phone() {
        return restoran_phone;
    }

    public void setRestoran_phone(String restoranPhone) {
        this.restoran_phone = restoranPhone;
    }


    public String getRestoran_nama() {
        return restoran_nama;
    }
    public void setRestoran_nama(String restoran_nama) {
        this.restoran_nama = restoran_nama;
    }

    public String getRestoran_email() {
        return restoran_email;
    }

    public void setRestoran_email(String restoran_email) {
        this.restoran_email = restoran_email;
    }

    public String getRestoran_latitude() {
        return restoran_latitude;
    }

    public void setRestoran_latitude(String restoran_latitude) {
        this.restoran_latitude = restoran_latitude;
    }

    public Integer getRestoran_order() {
        return restoran_order;
    }

    public void setRestoran_order(Integer restoran_order) {
        this.restoran_order = restoran_order;
    }


    public String getRestoran_longitude() {
        return restoran_longitude;
    }

    public void setRestoran_longitude(String restoran_longitude) {
        this.restoran_longitude = restoran_longitude;
    }

    public String getRestoran_alamat() {
        return restoran_alamat;
    }

    public void setRestoran_alamat(String restoran_alamat) {
        this.restoran_alamat = restoran_alamat;
    }

    public Integer getJarak_pesanan() {
        return jarak_pesanan;
    }

    public void setJarak_pesanan(Integer jarak_pesanan) {
        this.jarak_pesanan = jarak_pesanan;
    }

    public String getRestoran_deskripsi() {
        return restoran_deskripsi;
    }

    public void setRestoran_deskripsi(String restoran_deskripsi) {
        this.restoran_deskripsi = restoran_deskripsi;
    }

    public Integer getRestoran_oprasional() {
        return restoran_oprasional;
    }

    public void setRestoran_oprasional(Integer restoran_oprasional) {
        this.restoran_oprasional = restoran_oprasional;
    }

    public String getRestoran_delivery() {
        return restoran_delivery;
    }

    public void setRestoran_delivery(String restoran_delivery) {
        this.restoran_delivery = restoran_delivery;
    }

    public String getRestoran_delivery_tarif() {
        return restoran_delivery_tarif;
    }

    public void setRestoran_delivery_tarif(String restoran_delivery_tarif) {
        this.restoran_delivery_tarif = restoran_delivery_tarif;
    }

    public String getRestoran_delivery_minimum() {
        return restoran_delivery_minimum;
    }

    public void setRestoran_delivery_minimum(String restoran_delivery_minimum) {
        this.restoran_delivery_minimum = restoran_delivery_minimum;
    }

    public Integer getRestoran_pajak_pb_satu() {
        return restoran_pajak_pb_satu;
    }

    public void setRestoran_pajak_pb_satu(Integer restoran_pajak_pb_satu) {
        this.restoran_pajak_pb_satu = restoran_pajak_pb_satu;
    }

    public Integer getJumlahKurir() {
        return jumlahKurir;
    }

    public void setJumlahKurir(Integer jumlahKurir) {
        this.jumlahKurir = jumlahKurir;
    }

    public String getRestoran_distace() {
        return restoran_distace;
    }

    public void setRestoran_distace(String restoran_distace) {
        this.restoran_distace = restoran_distace;
    }





}
