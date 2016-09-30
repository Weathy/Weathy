package com.example.kali.weathy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TenDayFragment extends Fragment {

    private TenDayComunicator activity;

    interface TenDayComunicator{

       public void changeFragment(Fragment f);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TenDayComunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ten_day, container, false);


        return root;
    }

    public static TenDayFragment newInstance(String str) {
        
        Bundle args = new Bundle();
        args.putString("str", str);
        
        TenDayFragment fragment = new TenDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
