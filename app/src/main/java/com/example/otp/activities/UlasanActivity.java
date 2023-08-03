package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.config.ServerConfig;
import com.example.otp.response.ResponseValue;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;
import com.example.otp.utils.SessionManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UlasanActivity extends AppCompatActivity {

    Button btnUlasan;
    EditText kolomUlasan;

    ApiService mApiServie;
    SessionManager sessionManager;
    HashMap<String, String> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);

        kolomUlasan = (EditText) findViewById(R.id.kolomUlasan);
        btnUlasan = (Button) findViewById(R.id.buttonUlasan);

        mApiServie = ServerConfig.getAPIService();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();


        btnUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Mengambil Inputan Nilai String
                String strUlasan = kolomUlasan.getText().toString();
                String id_kons = user.get(SessionManager.NAMA_KONSUMEN).toString();

                if (strUlasan.isEmpty()){
                    kolomUlasan.setError("Ulasan Harus Di isi");
                    kolomUlasan.requestFocus();
                }else{
                    mApiServie.getUlasan(id_kons, strUlasan).enqueue(new Callback<ResponseValue>() {
                        @Override
                        public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                            String value;
                            if(response.isSuccessful()){
                                value = response.body().getValue();
                                if(value.equals("1")){
                                    Toast.makeText(UlasanActivity.this, "Teriakasih Atas Ulasan Anda", Toast.LENGTH_SHORT).show();
                                    Intent ulasan = new Intent(UlasanActivity.this, MainActivity.class);
                                    startActivity(ulasan);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseValue> call, Throwable t) {
                            Toast.makeText(UlasanActivity.this, R.string.lostconnection, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}