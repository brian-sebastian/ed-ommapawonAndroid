package com.example.otp.models;

public class CartList {
    private Integer id;
    private String id_resto;
    private String id_menu;
    private String harga;
    private Integer qty;
    private String catatan;
//    private Integer discount;
    private String nama_makanan;
    private String image;
    private int ketersediaan;

    public CartList(int id, String id_resto, String id_menu, String harga, Integer qty, String catatan, String nama_menu, String menu_foto, int ketersediaan) {
        this.id = id;
        this.id_resto = id_resto;
        this.id_menu = id_menu;
        this.harga = harga;
        this.qty = qty;
        this.catatan = catatan;
        this.nama_makanan = nama_menu;
//        this.discount = discount;
        this.image = menu_foto;
        this.ketersediaan = ketersediaan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId_resto() {
        return id_resto;
    }

    public void setId_resto(String id_resto) {
        this.id_resto = id_resto;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

//    public Integer getDiscount() {
//        return discount;
//    }
//
//    public void setDiscount(Integer discount) {
//        this.discount = discount;
//    }

    public String getNama_makanan() {
        return nama_makanan;
    }

    public void setNama_makanan(String nama_makanan) {
        this.nama_makanan = nama_makanan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getKetersediaan() {
        return ketersediaan;
    }

    public void setKetersediaan(int ketersediaan) {
        this.ketersediaan = ketersediaan;
    }


}
