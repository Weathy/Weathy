package com.example.kali.weathy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kali.weathy.Database.RequestTask;

public class SearchFragment extends Fragment {

    private EditText cityNameET;
    private Button backButton;
    private Button gpsButton;
    private Button sofiaButton;
    private Button plovdivButton;
    private Button ruseButton;
    private Button varnaButton;
    private Button burgasButton;
    private Button plevenButton;
    private String cityName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        cityNameET = (EditText) view.findViewById(R.id.city_search_button);
        backButton = (Button) view.findViewById(R.id.back_search_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = cityNameET.getText().toString();
                new RequestTask(getActivity()).execute(cityName);
                FragmentManager fragmentManager = getFragmentManager();
                Log.e("fragment", fragmentManager.toString());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.search_fragment);
                fragmentTransaction.remove(f);
                fragmentTransaction.add(CityForecastFragment.newInstance("one"),"Search fragment");
                fragmentTransaction.commit();
            }
        });


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
