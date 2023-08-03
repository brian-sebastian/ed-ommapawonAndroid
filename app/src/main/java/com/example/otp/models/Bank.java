package com.example.otp.models;

public class Bank {
    private Integer id;
    private String nama;
    private String penerima;
    private String rekening;
    private String image;

    public Bank(int id, String nama, String penerima, String rekening, String image){
        this.id = id;
        this.nama = nama;
        this.penerima = penerima;
        this.rekening = rekening;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
