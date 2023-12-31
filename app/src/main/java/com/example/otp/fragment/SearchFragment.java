package com.example.otp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.activities.CartListActivity;
import com.example.otp.adapter.PagerAdapterTabSearch;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.Menu;
import com.example.otp.response.ResponseSearch;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    AlertDialog.Builder dialog;
    AlertDialog myAlert;
    Context mContext;
    SearchView searchView;
    TabLayout tabLayout;
    ApiService mApiService;
    SessionManager sessionManager;
    HashMap<String, String> locationSession;
    ProgressDialog progressDialog;

    private List<Menu> menuList = new ArrayList<>();
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;

    String lat, lang;

    View message;
    ImageView icon_message;
    TextView title_message, sub_title_message, searchText;
    Button btnRecomend;
    Typeface myCustomFont;
    ImageButton cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        searchView = (SearchView) view.findViewById(R.id.serchview);
        sessionManager = new SessionManager(mContext);
        locationSession = sessionManager.getLocation();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.rootLayout);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        cart = (ImageButton) view.findViewById(R.id.cart);

        message = (View) view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);
        btnRecomend = (Button) view.findViewById(R.id.btnLocation);

        icon_message.setImageResource(R.drawable.msg_search_food);
        title_message.setText("Temukan Menu di Sekitar Anda");

        mApiService = ServerConfig.getAPIService();
        searchText = (TextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MavenPro-Regular.ttf");
        searchText.setTypeface(myCustomFont);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
                //progress dialog
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.cari), true, false);
                cari(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void cari(String query) {
        progressDialog.dismiss();
        lat = locationSession.get(SessionManager.LAT);
        lang = locationSession.get(SessionManager.LANG);
        mApiService.cari(lat, lang, query).enqueue(new Callback<ResponseSearch>() {
            @Override
            public void onResponse(Call<ResponseSearch> call, Response<ResponseSearch> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("1")) {
                        message.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        menuList = response.body().getMenu();
                        tabLayout.getTabAt(0).setText("Menu (" + menuList.size() + ")");
                        PagerAdapterTabSearch pagerAdapterTabSearch = new PagerAdapterTabSearch(getFragmentManager(), tabLayout.getTabCount(), menuList);
                        viewPager.setAdapter(pagerAdapterTabSearch);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                    } else {
                        viewPager.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        message.setVisibility(View.VISIBLE);
                        icon_message.setImageResource(R.drawable.msg_failure_search);
                        title_message.setText("Opps.. Maaf Kami Belum Menemukannya");
                        sub_title_message.setText("Kami akan terus mengembangkan \n jangkauan kami terhadap restoran anda \n");
                        Toast.makeText(mContext, "Tidak ditmeukan", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseSearch> call, Throwable t) {
                progressDialog.dismiss();
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps.. Gagal terhubung keserver");
                sub_title_message.setText("Priksa kembali koneksi internet Anda");
            }
        });

    }
}