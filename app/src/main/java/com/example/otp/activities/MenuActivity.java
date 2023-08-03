package com.example.otp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.otp.R;
import com.example.otp.adapter.PagerAdapterTabKategoriMenuDynamic;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.Kategori;
import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;
import com.example.otp.response.ResponseMenu;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private RumahMakan resto;
    private ApiService mApiService;
    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    TextView mAlamat, mOperasional;
    ImageView imgResto;
    ProgressDialog progressDialog;
    int oprasional;
    SessionManager sessionManager;
    HashMap<String, String> user;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mContext = this;
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getUserDetail();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabLayout = findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        imgResto = (ImageView) findViewById(R.id.imageRestoran);
        mAlamat = (TextView) findViewById(R.id.tvAlamat);
        mOperasional = (TextView) findViewById(R.id.tvOperasional);
        setSupportActionBar(toolbar);
        initViews();

        getIncomingIntent();
        progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);

        //set alamat restoran
        mAlamat.setText(resto.getRestoran_alamat());
        //set operasional restoran
        if (resto.getRestoran_oprasional() == 1) {
            mOperasional.setText("BUKA");
            mOperasional.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            mOperasional.setText("TUTUP");
            mOperasional.setBackgroundResource(R.drawable.rounded_corner_red);
        }
        //set title toolbar
        collapsingToolbar.setTitle(resto.getRestoran_nama());
//        //set image background
       oprasional = resto.getRestoran_oprasional();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initViews() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setDynamicFragmentToTabLayout();
    }

    private void setDynamicFragmentToTabLayout() {
        int jmlKate = kategoriList.size();
        for (int i = 0; i < jmlKate; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(kategoriList.get(i).getKategoriNama() + " (" + kategoriList.get(i).getTotal_menu() + ")"));
        }
      PagerAdapterTabKategoriMenuDynamic mDynamicFragmentAdapter = new PagerAdapterTabKategoriMenuDynamic(getSupportFragmentManager(), mTabLayout.getTabCount(), menuList, kategoriList , oprasional);
        viewPager.setAdapter(mDynamicFragmentAdapter);
        viewPager.setCurrentItem(0);
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("Resto")) {

           resto = (RumahMakan) getIntent().getSerializableExtra("Resto");

            String id_restoran = resto.getId().toString();
            String id_konsuemn = user.get(SessionManager.ID);
            setValue(id_restoran, id_konsuemn);
        }

    }

    private void setValue(String id_restorant, String id_konsumen) {
        mApiService.getMenu(id_restorant, id_konsumen).enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    if (value.equals("1")) {
                        menuList = response.body().getRestoran_menu();
                        kategoriList = response.body().getRestoran_kategori();
                        initViews();
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "gagal", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}