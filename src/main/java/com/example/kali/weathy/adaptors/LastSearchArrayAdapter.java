package com.example.kali.weathy.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kali.weathy.R;

import java.util.List;

public class LastSearchArrayAdapter extends ArrayAdapter{

    private Context context;
    private List<String> items;

    public LastSearchArrayAdapter(Context context, List items) {
        super(context, R.layout.last_search_cv, items);

        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.last_search_cv, null);
        }

        TextView cityTV = (TextView) view.findViewById(R.id.last_search_tv);
        cityTV.setText(items.get(position));

        return view;
    }
}
