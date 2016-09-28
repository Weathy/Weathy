package com.example.pc.weathy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

}
