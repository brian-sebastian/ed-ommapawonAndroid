package com.example.otp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.otp.R;
import com.example.otp.fragment.AccontFragment;
import com.example.otp.fragment.HistoryFragment;
import com.example.otp.fragment.OrderFragment;
import com.example.otp.fragment.RestoFragment;
import com.example.otp.fragment.SearchFragment;
import com.example.otp.utils.SessionManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    SessionManager sessionManager;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        sessionManager = new SessionManager(MainActivity.this);

        checkSessionLogin();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //loading the default fragment
        Fragment f = new RestoFragment();

        loadFragment(f);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.action_resto:
                        fragment = new RestoFragment();
                        break;

                    case R.id.action_order:
                        fragment = new OrderFragment();
                        break;
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;

                    case R.id.action_history:
                        fragment = new HistoryFragment();
                        break;

                    case R.id.action_account:
                        fragment = new AccontFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });



    }

    private boolean loadFragment(Fragment fragment){
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setPhoneNumber() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            //          tvPhoneNumber.setText(user.getPhoneNumber());
        } catch (Exception e) {
            Toast.makeText(this, "Phone Number Not Found", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkSessionLogin() {
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(mContext, HalamanUtamaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        } else {
            if (!sessionManager.isGetLocation()) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
    }


}