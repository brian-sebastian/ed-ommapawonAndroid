package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.otp.R;

public class TransferActivity extends AppCompatActivity {

    Button btnCekStatus, btnBack;
    TextView tvNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        btnCekStatus = findViewById(R.id.btn_cekStatus);
//        btnBack = findViewById(R.id.btn_kembali);

        tvNominal = findViewById(R.id.tv_nominal);
        String nominal = getIntent().getStringExtra("Total");
        tvNominal.setText(nominal);

        btnCekStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransferActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(TransferActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}