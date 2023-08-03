package com.example.otp.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.otp.R;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.User;
import com.example.otp.response.ResponseAuth;
import com.example.otp.rest.ApiService;
import com.example.otp.rest.ClientService;
import com.example.otp.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigningActivity extends AppCompatActivity {

    Context mContext;
    String value, message;
    ApiService mApiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    EditText inputMobile;
    CoordinatorLayout coordinatorLayout;
    TextView textSignUp;
    Button buttonGetOTP;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);

        mContext = this;
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(this);

        textSignUp = (TextView) findViewById(R.id.textSignUp);
        inputMobile = findViewById(R.id.inputMobile);
        buttonGetOTP = findViewById(R.id.buttonGetOTP);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);




        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //hidden keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(),0);

                //progress dialog
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);

                final String phone = clearPhone(inputMobile.getText().toString());

                if (phone.equals("62")) {
                    progressDialog.dismiss();
                    inputMobile.setError("Nomor telepon diperlukan");
                    inputMobile.requestFocus();
                    return;
                }

                if (phone.length() < 12) {
                    progressDialog.dismiss();
                    inputMobile.setError("Nomor telepon tidak valid");
                    inputMobile.requestFocus();
                    return;
                }

                mApiService.signInRequest(phone).enqueue(new Callback<ResponseAuth>() {
                    @Override
                    public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {

                            value = response.body().getValue();
                            message = response.body().getMessage();
                            //phone terdaftar
                            if (value.equals("1")) {
                                //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                User user = response.body().getKonsumen();
                                Intent intent = new Intent(SigningActivity.this, VerifyActivity.class);
                                intent.putExtra("user", user);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                //nomor phone tidak terdaftar
                            } else {
                                Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE).setAction("Daftar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String hp = phone.substring(2, phone.length());
                                        Intent intent = new Intent(mContext, RegistasiActivity.class);
                                        intent.putExtra("phone", hp);
                                        startActivity(intent);
                                    }
                                }).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAuth> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar.make(coordinatorLayout, R.string.lostconnection, Snackbar.LENGTH_LONG).show();
                    }
                });

            }

        });


//        Text Daftar
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent textDaftar = new Intent(getApplicationContext(), RegistasiActivity.class);
                startActivity(textDaftar);
            }
        });

    }

    //untuk menggambil no hp
    public String clearPhone(String phoneNumber) {
        String hp = phoneNumber.replaceAll("-", "");
        String clearPhone = hp.substring(1, hp.length());
        return clearPhone;
    }

}