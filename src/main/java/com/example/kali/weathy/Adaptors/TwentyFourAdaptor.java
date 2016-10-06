package com.example.kali.weathy.adaptors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kali.weathy.R;
import com.example.kali.weathy.model.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class TwentyFourAdaptor  extends RecyclerView.Adapter<TwentyFourAdaptor.TwentyFourVH>{

    private List<Weather.TwentyFourWeather> forecast;
    private Activity activity;
    private String firstDate;
    private String secondDate;
    private int secondDatePlace;
    private int firstHour;

    public TwentyFourAdaptor(Activity activity, List<Weather.TwentyFourWeather> forecast) {
        this.activity = activity;
        this.forecast = forecast;
        this.firstDate = forecast.get(0).getDate();
        this.firstHour = Integer.parseInt(forecast.get(0).getTime().split(":")[0]);

        for(int i = 0; i<forecast.size(); i++){
            if(!firstDate.equals(forecast.get(i).getDate())){
                secondDate = forecast.get(i).getDate();
                break;
            }
        }
        secondDatePlace = 23 - firstHour;
    }

    @Override
    public TwentyFourVH onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.one_hour_view, parent, false);
        TwentyFourVH holder = new TwentyFourVH(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(TwentyFourVH holder, int position) {

        final Weather.TwentyFourWeather weather = forecast.get(position);

        if(position == 0) {
            holder.dateTV.setVisibility(View.VISIBLE);
            holder.dateTV.setText(firstDate);
        }
        else{
            if(position == secondDatePlace+1){
                holder.dateTV.setVisibility(View.VISIBLE);
                holder.dateTV.setText(secondDate);
            }
            else{
                holder.dateTV.setVisibility(View.GONE);
            }
        }

        holder.timeTV.setText(weather.getTime());
        holder.feelslikeTV.setText(weather.getFeelsLike()+"℃");
        holder.currentTempTV.setText(weather.getCurrentTemp()+"℃");

        new IconTask(holder.iconIV).execute(weather.getIconURL());

    }


    @Override
    public int getItemCount() {
        return forecast.size();
    }

    class TwentyFourVH extends RecyclerView.ViewHolder{

        private TextView timeTV;
        private TextView currentTempTV;
        private TextView feelslikeTV;
        private ImageView iconIV;
        private TextView dateTV;

        public TwentyFourVH(View item) {
            super(item);
            timeTV = (TextView) item.findViewById(R.id.tf_time_textview);
            currentTempTV = (TextView) item.findViewById(R.id.tf_temp_textview);
            feelslikeTV = (TextView) item.findViewById(R.id.tf_feelslike_textview);
            iconIV = (ImageView) item.findViewById(R.id.tf_icon_imageview);
            dateTV = (TextView) item.findViewById(R.id.date_textview);

        }
    }


    class TwentyFourDateVH extends RecyclerView.ViewHolder{

        private TextView dateTV;

        public TwentyFourDateVH(View item) {
            super(item);
            dateTV = (TextView) item.findViewById(R.id.date_textview);

        }
    }


    private class IconTask extends AsyncTask<String, Void, Bitmap>{
        private ImageView icon;

        public IconTask(ImageView icon){
            this.icon = icon;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap icon = null;

            try {
                InputStream in = new URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            icon.setImageBitmap(image);
        }
    }

}
