package com.example.otp.rest;

import com.example.otp.response.ResponseOneRumahMakan;
import com.example.otp.response.ResponseAuth;
import com.example.otp.response.ResponseKonsumen;
import com.example.otp.response.ResponseMenu;
import com.example.otp.response.ResponseOrder;
import com.example.otp.response.ResponseRumahMakan;
import com.example.otp.response.ResponseSearch;
import com.example.otp.response.ResponseValue;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Fungsi ini untuk memanggil API http://192.168.100.31:8000/api/
    @FormUrlEncoded
    @POST("konsumen")
    Call<ResponseValue> signupRequest(@Field("nama_konsumen") String nama,
                                         @Field("email") String email,
                                         @Field("nomor_telpon") String phone,
                                         @Field("token") String token);

    //Update Konsumen
    @FormUrlEncoded
    @PATCH("konsumen/{konsumen}")
    Call<ResponseValue> updateKonsumen(@Path("konsumen") String id,
                                       @Field("nama_konsumen") String nama,
                                       @Field("nomor_telpon") String phone,
                                       @Field("email") String email,
                                       @Field("token") String token);

    @GET("konsumen/login/{phone}")
    Call<ResponseAuth>signInRequest(@Path("phone") String phone);

    //Update Token if Logged
    @FormUrlEncoded
    @PATCH("konsumen/{konsuman}")
    Call<ResponseValue> updateToken(@Path("konsuman") String id,
                                    @Field("token") String token);

    // List Menu
    @GET("menu")
    Call<ResponseMenu>getMenu(@Query("id") String id_restorant,
                              @Query("id_konsumen") String id_konsumen);

    //List Rumah Makan
    @GET("rumahmakan")
    Call<ResponseRumahMakan>getRumahMakan(@Query("lat") String lat,
                                          @Query("long") String lang);

    //    get one restoran
    @GET("rumahmakan/{rumahmakan}")
    Call<ResponseOneRumahMakan> getRestoranByID(@Path("rumahmakan") String id_restoran);

    //cek cart
    @GET("rumahmakan")
    Call<ResponseOneRumahMakan> cekCart(@Query("lat") String lat,
                                      @Query("long") String lang,
                                      @Query("id_restoran") String id_restoran,
                                      @Query("id_konsumen") String id_konsumen);

    @FormUrlEncoded
    @POST("order/tambah")
    Call<ResponseValue> createOrder(@Field("title") String title,
                                    @Field("message") String message,
                                    @Field("id_konsumen") String id_konsumen,
                                    @Field("id_restoran") String id_restoran,
                                    @Field("order_lat") String order_lat,
                                    @Field("order_long") String order_long,
                                    @Field("order_alamat") String order_alamat,
                                    @Field("order_catatan") String order_catatan,
                                    @Field("order_metode_bayar") String order_metode_bayar,
                                    @Field("order_jarak_antar") String order_jarak_antar,
                                    @Field("order_biaya_anatar") String order_biaya_anatar,
//                                    @Field("order_pajak_pb_satu") Integer order_pajak_pb_satu,
                                    @Field("menu[]") ArrayList<String> menu,
                                    @Field("qty[]") ArrayList<String> qty,
                                    @Field("harga[]") ArrayList<String> harga,
//                                    @Field("discount[]") ArrayList<String> discount,
                                   @Field("catatan[]") ArrayList<String> catatan
                                    );

    //gerOrderIn proces
    @GET("order")
    Call<ResponseOrder> getOrder(@Query("id_konsumen") String id_konsumen,
                                 @Query("status[]") ArrayList<String> status);

    //setFavorit
    @FormUrlEncoded
    @POST("favorit")
    Call<ResponseValue> setFavorit(@Field("id_konsumen") String id_konsumen,
                                   @Field("id_menu") String id_menu);

    //cek cari
    @GET("rumahmakan")
    Call<ResponseSearch> cari(@Query("lat") String lat,
                              @Query("long") String lang,
                              @Query("cari") String cari);

    //Kirim struk
    @FormUrlEncoded
    @POST("order/struk")
    Call<ResponseValue> sendEmail(@Field("id_order") Integer id_order);

    //uat
    @FormUrlEncoded
    @POST("ulasan")
    Call<ResponseValue> getUlasan(@Field("id_konsumen") String id_user,
                                @Field("ulasan") String ulasan);


}
