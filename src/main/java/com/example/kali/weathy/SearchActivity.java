package com.example.kali.weathy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class SearchActivity extends AppCompatActivity implements PlaceSelectionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_fragment);
        fragment.setOnPlaceSelectedListener(this);
        fragment.setHint("Search for location");
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        fragment.setFilter(filter);
        String condition = getIntent().getStringExtra("condition");

        switch (condition) {
            case "Clear":
                findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_clear);
                break;
            case "Clouds":
                findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_cloudy);
                break;
            case "Thunderstorm":
                findViewById(R.id.activity_search).setBackgroundResource(R.drawable.day_thunderstorm);
                break;
            case "Rain":
                findViewById(R.id.activity_search).setBackgroundResource(R.drawable.rain);
                break;
        }


    }

    @Override
    public void onPlaceSelected(Place place) {

        Toast.makeText(this, "You have selected" + place.getName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(Status status) {

        Toast.makeText(this, "You did something stupid", Toast.LENGTH_SHORT).show();

    }
}
