package com.example.otp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_restoran")
    @Expose
    private Integer id_restoran;
    @SerializedName("id_kategori")
    @Expose
    private Integer id_kategori;
    @SerializedName("nama_makanan")
    @Expose
    private String nama_makanan;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("menu_ketersediaan")
    @Expose
    private Integer menuKetersediaan;
//    @SerializedName("menu_discount")
//    @Expose
//    private Integer menuDiscount;
    @SerializedName("menu_jumlah_favorit")
    @Expose
    private Integer menuJumlahFavorit;
    @SerializedName("menu_favorit")
    @Expose
    private Integer menuFavorit;
    @SerializedName("pivot")
    @Expose
    private DetailOrder pivot;


    public Integer getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(Integer id_kategori) {
        this.id_kategori = id_kategori;
    }

    public Integer getMenuKetersediaan() {
        return menuKetersediaan;
    }

    public void setMenuKetersediaan(Integer menuKetersediaan) {
        this.menuKetersediaan = menuKetersediaan;
    }

//    public Integer getMenuDiscount() {
//        return menuDiscount;
//    }
//
//    public void setMenuDiscount(Integer menuDiscount) {
//        this.menuDiscount = menuDiscount;
//    }

    public Integer getMenuJumlahFavorit() {
        return menuJumlahFavorit;
    }

    public void setMenuJumlahFavorit(Integer menuJumlahFavorit) {
        this.menuJumlahFavorit = menuJumlahFavorit;
    }

    public Integer getMenuFavorit() {
        return menuFavorit;
    }

    public void setMenuFavorit(Integer menuFavorit) {
        this.menuFavorit = menuFavorit;
    }

    public DetailOrder getPivot() {
        return pivot;
    }

    public void setPivot(DetailOrder pivot) {
        this.pivot = pivot;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_restoran() {
        return id_restoran;
    }

    public void setId_restoran(Integer id_restoran) {
        this.id_restoran = id_restoran;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNama_makanan() {
        return nama_makanan;
    }

    public void setNama_makanan(String nama_makanan) {
        this.nama_makanan = nama_makanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

}
