package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.otp.R;

public class HalamanUtamaActivity extends AppCompatActivity {

    Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buttonLogin = new Intent(getApplicationContext(), SigningActivity.class);
                startActivity(buttonLogin);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonRegister();
            }
        });
    }

    public void ButtonRegister(){
        Intent buttonRegister = new Intent(getApplicationContext(), RegistasiActivity.class);
        startActivity(buttonRegister);
    }
}