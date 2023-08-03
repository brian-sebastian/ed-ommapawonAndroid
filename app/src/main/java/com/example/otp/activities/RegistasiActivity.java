package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otp.R;

import com.example.otp.config.ServerConfig;
import com.example.otp.response.ResponseValue;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;
import com.example.otp.utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistasiActivity extends AppCompatActivity {

    private Context mContext;
    private static final String TAG = "SignUpActivity";
    ApiService mApiService;

    ProgressDialog progressDialog;
    Button buttonDaftar, buttonMasuk;
    EditText kolomNama, kolomNomor, kolomEmail;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registasi);


        mApiService = ServerConfig.getAPIService();

        mContext = this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        kolomNama = findViewById(R.id.kolomNama);
        kolomNomor = findViewById(R.id.kolomNomor);
        kolomEmail = findViewById(R.id.kolomEmail);

        buttonDaftar = findViewById(R.id.buttonDaftar);
        buttonMasuk = findViewById(R.id.buttonMasuk);


        //if get intent
        if (getIntent().hasExtra("phone")) {
            kolomNomor.setText(getIntent().getStringExtra("phone").toString());
        }

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hilangkan keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

                //create progres dialog
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);


                //mengambil nilai inputan ke string
                String strNama = kolomNama.getText().toString();
                String strPhone = clearPhone(kolomNomor.getText().toString());
                String strEmail = kolomEmail.getText().toString();
                String token = SharedPrefManager.getInstance(mContext).getDeviceToken();


                if (strNama.isEmpty() || strNama.equals(null)) {
                    progressDialog.dismiss();
                    kolomNama.setError("Nama diperlukan");
                    kolomNama.requestFocus();
                    return;
                } else if (strPhone.equals("62")) {
                    progressDialog.dismiss();
                    kolomNomor.setError("Nomor telepon diperlukan");
                    kolomNomor.requestFocus();
                    return;
                } else if (strPhone.length() < 12) {
                    progressDialog.dismiss();
                    kolomNomor.setError("Nomor telepon tidak valid");
                    kolomNomor.requestFocus();
                    return;
                } else if (strPhone.isEmpty() || strPhone.equals(null)) {
                    progressDialog.dismiss();
                    kolomNomor.setError("Nomor telepon diperlukan");
                    kolomNomor.requestFocus();
                    return;
                } else if (strEmail.isEmpty() || strEmail.equals(null)) {
                    progressDialog.dismiss();
                    kolomEmail.setError("Email diperlukan");
                    kolomEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    progressDialog.dismiss();
                    kolomEmail.setError("Email tidak valid");
                    kolomEmail.requestFocus();
                    return;
                } else if (token.isEmpty() || token.equals(null)) {
                    progressDialog.dismiss();
                    kolomEmail.setError("Email diperlukan");
                    kolomEmail.requestFocus();
                    return;
                }else{
                    mApiService.signupRequest(strNama, strEmail, strPhone, token).enqueue(new Callback<ResponseValue>() {
                        @Override
                        public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                            progressDialog.dismiss();
                            String value, message;
                            if (response.isSuccessful()) {
                                value = response.body().getValue();
                                message = response.body().getMessage();
                                if (value.equals("1")) {
                                    //snack bar success
                                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Gagal Registrasi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseValue> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout, R.string.lostconnection, Snackbar.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });

        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent masuk = new Intent(RegistasiActivity.this, SigningActivity.class);
                startActivity(masuk);
                finish();
            }
        });

    }

    public String clearPhone(String phoneNumber) {
        String hp = phoneNumber.replaceAll("-", "");
        String clearPhone = hp.substring(1, hp.length());
        return clearPhone;
    }

}

