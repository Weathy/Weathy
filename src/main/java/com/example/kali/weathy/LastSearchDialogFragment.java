package com.example.kali.weathy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kali.weathy.adaptors.LastSearchArrayAdapter;
import com.example.kali.weathy.database.DBManager;

public class LastSearchDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_last_search_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ListView lv = (ListView) root.findViewById(R.id.last_search_lv);
        LastSearchArrayAdapter adapter = new LastSearchArrayAdapter(getActivity(), DBManager.getInstance(getActivity()).getLastSearchCities());
        lv.setAdapter(adapter);

        return root;
    }
}
