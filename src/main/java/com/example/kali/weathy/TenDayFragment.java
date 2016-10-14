package com.example.kali.weathy;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kali.weathy.adaptors.TenDayListAdaptor;
import com.example.kali.weathy.database.DBManager;

public class TenDayFragment extends Fragment {

    public TenDayListAdaptor adaptor;

    @Override
    public void onResume() {
        super.onResume();
        adaptor.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ten_day, container, false);

        ListView lv = (ListView) root.findViewById(R.id.one_day_lv);
        adaptor = new TenDayListAdaptor(getActivity(), DBManager.getInstance(getActivity()).getTenDayForecast());
        lv.setAdapter(adaptor);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment newFragment = TenDayDialogFragment.newInstance(DBManager.getInstance(getActivity()).getTenDayForecast().get(position));
                newFragment.show(ft, "TenDayDialog");
            }
        });

        return root;
    }

}
