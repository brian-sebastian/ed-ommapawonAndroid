package com.example.otp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.otp.models.User;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences sharedPreferences;


    private SharedPreferences.Editor editor;

    private Context _context;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String ID = "id";
    public static final String ID_PENGGUNA = "username";
    public static final String NAMA_KONSUMEN = "nama_konsumen";
    public static final String EMAIL = "email";
    public static final String NOMOR_TELPON = "nomor_telpon";

    public static final String IS_GET_LOCATION = "isLogged";
    public static final String ALAMAT = "alamat";
    public static final String LAT = "lat";
    public static final String LANG = "lang";



    public Context get_context() {
        return _context;
    }

    //constuctor
    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    //Menyimpan Akun Konseumen
    public void createLoginSession(User user){
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putString(ID, String.valueOf(user.getId()));
        editor.putString(NAMA_KONSUMEN,user.getKonsumenNama());
        editor.putString(EMAIL,user.getKonsumenEmail());
        editor.putString(NOMOR_TELPON,user.getKonsumenPhone());
        editor.commit();
    }


    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(ID,sharedPreferences.getString(ID,null));
        user.put(ID_PENGGUNA, sharedPreferences.getString(ID_PENGGUNA, null));
        user.put(NAMA_KONSUMEN, sharedPreferences.getString(NAMA_KONSUMEN,null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL,null));
        user.put(NOMOR_TELPON, sharedPreferences.getString(NOMOR_TELPON,null));
        return user;
    }

    //Menyimpan Letak Posisi Konsumen
    public void setLocation (String alamat,String lat,String lang){
        editor.putBoolean(IS_GET_LOCATION,true);
        editor.putString(ALAMAT,alamat);
        editor.putString(LAT,lat);
        editor.putString(LANG,lang);
        editor.commit();
    }

    public HashMap<String,String> getLocation() {
        HashMap<String,String> location = new HashMap<>();
        location.put(ALAMAT,sharedPreferences.getString(ALAMAT,null));
        location.put(LAT,sharedPreferences.getString(LAT,null));
        location.put(LANG,sharedPreferences.getString(LANG,null));
        return location;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public boolean isGetLocation() {
        return sharedPreferences.getBoolean(IS_GET_LOCATION,false);
    }
}
