package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.otp.models.User;
import com.example.otp.response.ResponseValue;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.SessionManager;
import com.example.otp.utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKonsumenActivity extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressDialog;
    private static final String TAG = "SignUpActivity";

    EditText etNama, etPhone, etEmail;
    Button btnEditData;
    CoordinatorLayout coordinatorLayout;

    ApiService mApiSerivce;
    String value, message;
    SessionManager sessionManager;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_konsumen);
        etNama = (EditText) findViewById(R.id.editTextNama);
        etPhone = (EditText) findViewById(R.id.editTextPhone);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        btnEditData = (Button) findViewById(R.id.buttonEditData);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mApiSerivce = ServerConfig.getAPIService();
        mContext = this;
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        init();
        ubahData();

    }

    private void ubahData() {
        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hilangkan keyboard
                final User user1 = new User();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

                //create progres dialog
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);

                //mengambil nilai inputan ke string
                String id = user.get(SessionManager.ID);
                String strNama = etNama.getText().toString();
                String strPhone = clearPhone(etPhone.getText().toString());
                String strEmail = etEmail.getText().toString();
                String token = SharedPrefManager.getInstance(mContext).getDeviceToken();
                user1.setId(Integer.parseInt(id));
                user1.setKonsumenNama(strNama);
                user1.setKonsumenPhone(strPhone);
                user1.setKonsumenEmail(strEmail);

                if (strNama.isEmpty() || strNama.equals(null)) {
                    progressDialog.dismiss();
                    etNama.setError("Nama diperlukan");
                    etNama.requestFocus();
                    return;
                } else if (strPhone.equals("62")) {
                    progressDialog.dismiss();
                    etPhone.setError("Nomor telepon diperlukan");
                    etPhone.requestFocus();
                    return;
                } else if (strPhone.length() < 12) {
                    progressDialog.dismiss();
                    etPhone.setError("Nomor telepon tidak valid");
                    etPhone.requestFocus();
                    return;
                } else if (strPhone.isEmpty() || strPhone.equals(null)) {
                    progressDialog.dismiss();
                    etPhone.setError("Nomor telepon diperlukan");
                    etPhone.requestFocus();
                    return;
                } else if (strEmail.isEmpty() || strEmail.equals(null)) {
                    progressDialog.dismiss();
                    etEmail.setError("Email diperlukan");
                    etEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    progressDialog.dismiss();
                    etEmail.setError("Email tidak valid");
                    etEmail.requestFocus();
                    return;
                } else if (token.isEmpty() || token.equals(null)) {
                    progressDialog.dismiss();
                    etEmail.setError("Email diperlukan");
                    etEmail.requestFocus();
                    return;
                } else {
                    mApiSerivce.updateKonsumen(id, strNama, strPhone, strEmail, token).enqueue(new Callback<ResponseValue>() {
                        @Override
                        public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                value = response.body().getValue();
                                message = response.body().getMessage();
                                if (value.equals("1")) {
                                    //snack bar success
                                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                                    sessionManager.createLoginSession(user1);
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
    }

    private void init() {
        etNama.setText(user.get(SessionManager.NAMA_KONSUMEN));
        etPhone.setText("+" + user.get(SessionManager.NOMOR_TELPON).substring(2));
        etEmail.setText(user.get(SessionManager.EMAIL));
    }
    public String clearPhone(String phoneNumber) {
        String hp = phoneNumber.replaceAll("-", "");
        String clearPhone = hp.substring(1, hp.length());
        return clearPhone;
    }

}