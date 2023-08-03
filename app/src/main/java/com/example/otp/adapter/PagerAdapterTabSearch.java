package com.example.otp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.otp.fragment.SearchMenuFragment;
import com.example.otp.models.Menu;
import com.example.otp.models.RumahMakan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapterTabSearch extends FragmentStatePagerAdapter {
    private int number_tbas;
    private List<Menu> menuList = new ArrayList<>();
    private List<RumahMakan> restoranList = new ArrayList<>();

    public PagerAdapterTabSearch(FragmentManager fm, int number_tbas, List<Menu> menuList) {
        super(fm);
        this.number_tbas = number_tbas;
        this.menuList = menuList;
    }

//    public PagerAdapterTabSearch(FragmentManager fm, int number_tbas, List<Menu> menuList, List<RumahMakan> restoranList) {
//        super(fm);
//        this.number_tbas = number_tbas;
//        this.menuList = menuList;
//        this.restoranList = restoranList;
//    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        Fragment frag;
        switch (position) {
//            case 0:
//                b.putSerializable("restoran", (Serializable) restoranList);
//                frag = SearchRestoranFragment.newInstance();
//                frag.setArguments(b);
//                return frag;

            case 0:
                b.putSerializable("menu", (Serializable) menuList);
                frag = SearchMenuFragment.newInstance();
                frag.setArguments(b);
                return frag;


        }
        return null;
    }

    @Override
    public int getCount() {
        return number_tbas;
    }
}
