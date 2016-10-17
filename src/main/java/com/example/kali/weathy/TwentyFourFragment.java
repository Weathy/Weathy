package com.example.kali.weathy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kali.weathy.adaptors.TwentyFourListAdaptor;
import com.example.kali.weathy.database.DBManager;
import com.example.kali.weathy.model.Weather;

import java.util.ArrayList;


public class TwentyFourFragment extends Fragment {

    private InnerReceiver receiver;
    private ArrayList<Weather.TwentyFourWeather> forecast;
    public TwentyFourListAdaptor adaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_twenty_four, container, false);
        forecast = (ArrayList<Weather.TwentyFourWeather>) DBManager.getInstance(getActivity()).getTwentyHourForecastObjects();

        receiver = new InnerReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter("SerciveComplete"));

        ListView lv = (ListView) root.findViewById(R.id.listview);
        if(forecast != null) {
            adaptor = new TwentyFourListAdaptor(getActivity(), forecast);
            lv.setAdapter(adaptor);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment newFragment = TwentyFourDialogFragment.newInstance(DBManager.getInstance(getActivity()).getTwentyHourForecastObjects().get(position));
                newFragment.show(ft, "TwentyFourDialog");
            }
        });

        return root;
    }

    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            getActivity().unregisterReceiver(receiver);
        }
    }
}
