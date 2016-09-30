package com.example.kali.weathy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TwentyFourFragment extends Fragment {

    private Button searchButton;
    private TwentyFourComunicator activity;

    interface TwentyFourComunicator{

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (TwentyFourComunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twenty_four, container, false);
    }

    public static TwentyFourFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("str", str);
        
        TwentyFourFragment fragment = new TwentyFourFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
