package com.example.otp.config;


import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;

public class ServerConfig {
    // .0.2.2 ini adalah localhost.
    //http://192.168.43.226:8000/api/
    //http://192.168.100.31:8000/api/
    //192.168.1.193
    //http://180.20.31.7:8000/api/
    //https://topapp.id/marketresto/api/v1/
    private static final String BASE_URL_API = "http://192.168.43.226:8000/api/";
//    private static final String BASE_URL_API = "http://192.168.100.31:8000/api/";
    // Mendeklarasikan Interface BaseApiService
    public static ApiService getAPIService(){
        return ClientService.getClient(BASE_URL_API).create(ApiService.class);
    }
}
