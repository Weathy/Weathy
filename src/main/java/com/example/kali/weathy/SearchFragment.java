package com.example.kali.weathy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SearchFragment extends Fragment {

    private EditText cityName;
    private Button backButton;
    private Button gpsButton;
    private Button sofiaButton;
    private Button plovdivButton;
    private Button ruseButton;
    private Button varnaButton;
    private Button burgasButton;
    private Button plevenButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);


        return view;
    }

    public static SearchFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString("str", str);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
