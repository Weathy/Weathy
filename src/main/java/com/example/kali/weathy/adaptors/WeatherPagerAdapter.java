package com.example.kali.weathy.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.kali.weathy.CityForecastFragment;
import com.example.kali.weathy.TenDayFragment;
import com.example.kali.weathy.TwentyFourFragment;


public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> registeredFragments;
    private FragmentManager manager;

    private final static int NUMBER_OF_FRAGMENTS = 3;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        registeredFragments = new SparseArray<>();
        this.manager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CityForecastFragment();
            case 1:
                return new TwentyFourFragment();
            case 2:
                return new TenDayFragment();
            default:
                return new CityForecastFragment();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_FRAGMENTS;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
}

