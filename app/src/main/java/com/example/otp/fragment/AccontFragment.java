package com.example.otp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.activities.EditKonsumenActivity;
import com.example.otp.activities.SigningActivity;
import com.example.otp.activities.UlasanActivity;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.User;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class AccontFragment extends Fragment {

    Context mContext;
    SessionManager sessionManager;
    TextView tvNamaUser, tvPhoneUser, tvEmailUser, btnPenilaian;
    ImageButton signout, edit;
    HashMap<String, String> user;
    ApiService mApiService;
    User user1 = new User();
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accont, container, false);
        mContext = getActivity();
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();
        init(view);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertKonfirmasi();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditKonsumenActivity.class);
                startActivity(intent);
            }
        });

        btnPenilaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UlasanActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setValue() {
        tvNamaUser.setText(user.get(SessionManager.NAMA_KONSUMEN));
        tvPhoneUser.setText("+" + user.get(SessionManager.NOMOR_TELPON));
        tvEmailUser.setText(user.get(SessionManager.EMAIL));
    }

    private void init(View view) {
        tvNamaUser = (TextView) view.findViewById(R.id.tvNamaUser);
        tvPhoneUser = (TextView) view.findViewById(R.id.tvPhoneUser);
        tvEmailUser = (TextView) view.findViewById(R.id.tvEmailUser);
        signout = (ImageButton) view.findViewById(R.id.btn_sign_out);
        edit = (ImageButton) view.findViewById(R.id.edit);
        btnPenilaian = (TextView) view.findViewById(R.id.btnPenilaian);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
        setValue();
    }
    public void alertKonfirmasi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Apakah Anda Yakin Akan Keluar ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                logout();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });
        alert.show();
    }

    void logout() {
        FirebaseAuth.getInstance().signOut();
        sessionManager.logoutUser();
        progressDialog.dismiss();
        Intent intent = new Intent(mContext, SigningActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}