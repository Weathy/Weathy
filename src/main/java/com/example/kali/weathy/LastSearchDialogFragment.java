package com.example.kali.weathy;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kali.weathy.adaptors.LastSearchArrayAdapter;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.database.RequestWeatherIntentService;

import dmax.dialog.SpotsDialog;

public class LastSearchDialogFragment extends DialogFragment {

    public static SpotsDialog lastSearchDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_last_search_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ListView lv = (ListView) root.findViewById(R.id.last_search_lv);
        LastSearchArrayAdapter adapter = new LastSearchArrayAdapter(getActivity(), DBManager.getInstance(getActivity()).getLastSearchCities());
        lv.setAdapter(adapter);
        lastSearchDialog = new SpotsDialog(getActivity());
        lastSearchDialog.setCancelable(false);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lastSearchDialog.show();
                TextView v = (TextView) view.findViewById(R.id.last_search_tv);
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Intent intent = new Intent(getActivity(), RequestWeatherIntentService.class);
                intent.putExtra("city", v.getText().toString());
                intent.putExtra("country", DBManager.getInstance(getActivity()).getRequestData(v.getText().toString()));
                Log.e("internet" , "net" );
                getActivity().startService(intent);
                LastSearchDialogFragment.this.dismiss();
            }
        });

        return root;
    }
}
