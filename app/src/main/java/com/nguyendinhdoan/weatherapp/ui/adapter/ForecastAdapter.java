package com.nguyendinhdoan.weatherapp.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyendinhdoan.weatherapp.R;
import com.nguyendinhdoan.weatherapp.common.Common;
import com.nguyendinhdoan.weatherapp.data.model.WeatherForecastResult;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private static final String ICON_SCHEME = "https";
    private static final String ICON_AUTHORITY = "openweathermap.org";
    private static final String ICON_IMG_KEY = "img";
    private static final String ICON_W_KEY = "w";
    private static final String ICON_TYPE_KEY = ".png";
    private static final String TAG = "FORECAST_ADAPTER";

    private WeatherForecastResult weatherForecastResult;
    private Context context;

    public ForecastAdapter(WeatherForecastResult weatherForecastResult, Context context) {
        this.weatherForecastResult = weatherForecastResult;
        this.context = context;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forecast, viewGroup, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder forecastViewHolder, int position) {
        // Load icon image
        String iconName = weatherForecastResult.getList().get(position).getWeather().get(0).getIcon();
        String iconPath =
                new Uri.Builder().scheme(ICON_SCHEME)
                        .authority(ICON_AUTHORITY)
                        .appendPath(ICON_IMG_KEY)
                        .appendPath(ICON_W_KEY)
                        .appendPath(iconName + ICON_TYPE_KEY)
                        .build().toString();
        Log.d(TAG, "icon url: " + iconPath);
        Picasso.get().load(iconPath).into(forecastViewHolder.iconImageView);

        // load temperature
        int temperature = (int) weatherForecastResult.getList().get(position).getMain().getTemp();
        forecastViewHolder.temperatureTextView.setText(context.getString(R.string.temperature_value, temperature));

        // load description
        String description = weatherForecastResult.getList().get(position).getWeather().get(0).getDescription();
        forecastViewHolder.descriptionTextView.setText(description);

        // load hour time
        int dateTime = weatherForecastResult.getList().get(position).getDt();
        String hourFormatted = Common.convertUnixToHour(dateTime);
        forecastViewHolder.dateHourTextView.setText(hourFormatted);

        // load day time
        String dayFormatted = Common.convertUnixToDay(dateTime);
        forecastViewHolder.dateDayTextView.setText(dayFormatted);

    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.getList().size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon_image_view)
        ImageView iconImageView;
        @BindView(R.id.date_hour_text_view)
        TextView dateHourTextView;
        @BindView(R.id.date_day_text_view)
        TextView dateDayTextView;
        @BindView(R.id.description_text_view)
        TextView descriptionTextView;
        @BindView(R.id.temperature_text_view)
        TextView temperatureTextView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
