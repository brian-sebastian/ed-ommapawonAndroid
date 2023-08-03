package com.example.otp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.activities.CartListActivity;
import com.example.otp.activities.MapsActivity;
import com.example.otp.adapter.RumahMakantAdapter;
import com.example.otp.config.ServerConfig;
import com.example.otp.models.RumahMakan;
import com.example.otp.response.ResponseRumahMakan;
import com.example.otp.rest.ApiService;
import com.example.otp.utils.GPSTracker;
import com.example.otp.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestoFragment extends Fragment {

    private RecyclerView recyclerView;
    private RumahMakantAdapter adapter;
    private List<RumahMakan> data = new ArrayList<>();
    ApiService mApiService;
    TextView textview, title_error, sub_title_error;
    ImageButton openmap;
    ImageView img_msg;
//    Button btnLocation;
    Context mContext;
    GPSTracker gpsTracker;
    String addressLine, lat, lang, value;
    View viewError;
    SearchView searchView = null;
    SearchView.OnQueryTextListener queryTextListener;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resto, container, false);
        mContext = getActivity();
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MavenPro-Regular.ttf");

        textview = (TextView) view.findViewById(R.id.tvLokasiAnda);
        textview.setTypeface(type);
        mApiService = ServerConfig.getAPIService();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        openmap = (ImageButton) view.findViewById(R.id.openmap);
//        btnLocation = (Button) view.findViewById(R.id.btnLocation);

        viewError = view.findViewById(R.id.error);
        img_msg = (ImageView) view.findViewById(R.id.img_msg);
        title_error = (TextView) view.findViewById(R.id.title_msg);
        sub_title_error = (TextView) view.findViewById(R.id.sub_title_msg);


        // addData("0.47101406701336923", "101.40498843044043");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
            }
        });

//        btnLocation.setVisibility(View.VISIBLE);
//        btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MapsActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SessionManager sessionManager = new SessionManager(mContext);
        HashMap<String, String> locationSession;
        locationSession = sessionManager.getLocation();
        textview.setText(locationSession.get(SessionManager.ALAMAT));

        Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
        //progress dialog
       progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
        addData(locationSession.get(SessionManager.LAT), locationSession.get(SessionManager.LANG));
    }
    private void addData(String lat, String lang) {

        mApiService.getRumahMakan(lat, lang).enqueue(new Callback<ResponseRumahMakan>() {
            @Override
            public void onResponse(Call<ResponseRumahMakan> call, Response<ResponseRumahMakan> response) {

                value = response.body().getValue();

                if (response.isSuccessful()) {
                    if (value.equals("1")) {
                        data = response.body().getData();
                        adapter = new RumahMakantAdapter(getActivity(), data);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    } else {
                        viewError.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "restoran tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRumahMakan> call, Throwable t) {
                viewError.setVisibility(View.VISIBLE);
                img_msg.setImageResource(R.drawable.msg_no_connection);
                title_error.setText("Opps.. Tidak Ada Koneksi");
                sub_title_error.setText("Priksa Kembali Koneksi Internet Anda");
//                btnLocation.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
       // Inflate the menu; this adds items to the action bar if it is present.
       super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.menu_location_cart, menu);
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.cart_lokasi:
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_location:
                //progress dialog
                progressDialog = ProgressDialog.show(mContext, null, getString(R.string.memuat), true, false);
                onLocation();
                break;
        }
        return true;
    }


    void onLocation() {
        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.canGetLocation()) {
            addressLine = gpsTracker.getAddressLine(mContext);
            if (addressLine.equals("Lokasi Tidak Ditemukan")) {
                progressDialog.dismiss();
                Toast.makeText(mContext, addressLine, Toast.LENGTH_SHORT).show();
            } else {
                SessionManager sessionManager = new SessionManager(mContext);
                lat = String.valueOf(gpsTracker.getLatitude());
                lang = String.valueOf(gpsTracker.getLongitude());
                Toast.makeText(mContext, addressLine + "\n" + lat + "," + lang, Toast.LENGTH_SHORT).show();
                sessionManager.setLocation(addressLine, lat, lang);
                HashMap<String, String> locationSession;
                locationSession = sessionManager.getLocation();
                textview.setText(locationSession.get(SessionManager.ALAMAT));
                addData(locationSession.get(SessionManager.LAT), locationSession.get(SessionManager.LANG));
            }
        } else {
            progressDialog.dismiss();
            gpsTracker.showSettingsAlert();
        }
    }
}