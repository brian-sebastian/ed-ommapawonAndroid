package com.example.otp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.otp.fragment.MenuFragment;
import com.example.otp.models.Kategori;
import com.example.otp.models.Menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapterTabKategoriMenuDynamic extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private String id_resto;
    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    private int oprasional;

    public PagerAdapterTabKategoriMenuDynamic(FragmentManager fm, int NumOfTabs, List<Menu> menuList, List<Kategori> kategoriList, int operasional) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.menuList = menuList;
        this.kategoriList = kategoriList;
        this.oprasional = operasional;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putSerializable("menu", (Serializable) menuList);
        b.putSerializable("kategori", (Serializable) kategoriList);
        b.putInt("oprasinal", Integer.valueOf(oprasional));
        Fragment frag = MenuFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
