package com.example.kali.weathy.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kali.weathy.CityForecastFragment;
import com.example.kali.weathy.TenDayFragment;
import com.example.kali.weathy.TwentyFourFragment;

public class WeatherPagerAdapter extends FragmentPagerAdapter {


    private final static int NUMBER_OF_FRAGMENTS = 3;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CityForecastFragment.newInstance("one");
            case 1:
                return TwentyFourFragment.newInstance("two");
            case 2:
                return TenDayFragment.newInstance("three");
            default:
                return CityForecastFragment.newInstance("one");

        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_FRAGMENTS;
    }
}

