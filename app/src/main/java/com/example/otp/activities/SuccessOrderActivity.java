package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.otp.R;

public class SuccessOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);
        TextView idOrder = (TextView) findViewById(R.id.idorder);
        TextView btnSelesai = (TextView) findViewById(R.id.btnSelesai);
        String id = getIntent().getStringExtra("id");
        idOrder.setText("Order number: #" + id);
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selesai();
            }
        });
    }

    @Override
    public void onBackPressed() {
        selesai();
    }

    private void selesai() {
        Intent intent = new Intent(SuccessOrderActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}