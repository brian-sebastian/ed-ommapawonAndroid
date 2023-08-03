package com.example.otp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.otp.fragment.FragmentListHistory;

public class PagerAdapterTabHistory extends FragmentStatePagerAdapter {

    private int number_tbas;

    public PagerAdapterTabHistory(FragmentManager fm, int number_tbas) {
        super(fm);
        this.number_tbas = number_tbas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                return new FragmentListFavorit();
            case 0:
                return new FragmentListHistory();
        }
        return null;
    }

    @Override
    public int getCount() {
        return number_tbas;
    }
}
