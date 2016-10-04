package com.example.kali.weathy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.kali.weathy.adaptors.TenDayListAdaptor;
import com.example.kali.weathy.database.DBManager;

public class TenDayFragment extends Fragment {

    private Button searchButton;
    private TenDayComunicator activity;

    interface TenDayComunicator{

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

        ListView lv = (ListView) root.findViewById(R.id.one_day_lv);
        TenDayListAdaptor adaptor = new TenDayListAdaptor(getActivity(), DBManager.getInstance(getActivity()).getTenDayForecast());
        lv.setAdapter(adaptor);

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
