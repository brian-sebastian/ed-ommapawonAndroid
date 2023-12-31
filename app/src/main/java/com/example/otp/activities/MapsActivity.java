package com.example.otp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng location;
    private int geocoderMaxResult = 100;
    Context mContext;

    String alamat;
    SessionManager sessionManager;
    Button SetLocation;
    TextView mLocation;
    LatLng midLatLng, temp;
    HashMap<String, String> sessionLocation;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mContext = this;
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MavenPro-Regular.ttf");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SetLocation = (Button) findViewById(R.id.btnSetLocation);
        SetLocation.setTypeface(type);
        mLocation = (TextView) findViewById(R.id.tvLocation);
        sessionManager = new SessionManager(mContext);
        alamat = getAddressLine(mContext);
        String lat = null;
        String lang = null;

        sessionLocation = sessionManager.getLocation();
        if (sessionManager.isGetLocation()) {
            lat = sessionLocation.get(SessionManager.LAT);
            lang = sessionLocation.get(SessionManager.LANG);
        }

        //cek lokasi awal
        if (lat == null || lang == null) {
            location = new LatLng(-7.40074, 112.72513);
        } else {
            location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
        }

        //Click set Lokasi
        SetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLat = String.valueOf(midLatLng.latitude);
                String strLang = String.valueOf(midLatLng.longitude);
                Toast.makeText(mContext, alamat + " lat " + strLat + " lang " + strLang, Toast.LENGTH_SHORT).show();
                sessionManager.setLocation(alamat, strLat, strLang);

                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17), 5000, null);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            Toast.makeText(MapsActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                midLatLng = mMap.getCameraPosition().target;
                location = midLatLng;
                alamat = getAddressLine(mContext).toString();
                mLocation.setText(alamat);
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showSettingsAlert();
                } else {
                    Criteria criteria = new Criteria();
                    String provider = mgr.getBestProvider(criteria, false);
                    // Location locationn = mgr.getLastKnownLocation(provider);

                    Location currentLoc = mMap.getMyLocation();
                    temp = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
                    Toast.makeText(mContext, "Loc " + location, Toast.LENGTH_SHORT).show();
                    location = temp;
                    //  marker.setPosition(location);
                    alamat = getAddressLine(mContext).toString();
                    // marker.setSnippet(alamat);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17), 5000, null);
                    // marker.showInfoWindow();
                }
                return true;
            }
        });
    }


    public List<Address> getGeocoderAddress(Context context) {
        if (location != null) {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, geocoderMaxResult);
                return addresses;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getAddressLine(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addresLine = address.getAddressLine(0);
            return addresLine;
        } else {
            return "Alamat Tidak Ditemukan";
        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Atur Layanan GPS Anda");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Layanan GPS Tidak Aktif. Apakah Anda ingin masuk ke menu pengaturan?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // Showing Alert Message
        final AlertDialog alert = alertDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });
        alert.show();
    }
}