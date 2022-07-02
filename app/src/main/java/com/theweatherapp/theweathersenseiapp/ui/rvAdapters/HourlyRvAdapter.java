package com.theweatherapp.theweathersenseiapp.ui.rvAdapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.theweatherapp.theweathersenseiapp.MainActivity;
import com.theweatherapp.theweathersenseiapp.databinding.HourlyWeatherForecastLayoutBinding;
import com.theweatherapp.theweathersenseiapp.ui.customInrterfaces.MyClickInterFace;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Hourly;

import java.util.List;

public class HourlyRvAdapter extends RecyclerView.Adapter<HourlyRvAdapter.HourlyBindingAdapter>
{

    private List<Hourly> hourlyWeatherList;
    private int tempRand;

    public HourlyRvAdapter(List<Hourly> hourlyWeatherList) {
        this.hourlyWeatherList=hourlyWeatherList;
    }

    @NonNull
    @Override
    public HourlyBindingAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HourlyWeatherForecastLayoutBinding inflate =
                HourlyWeatherForecastLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new HourlyBindingAdapter(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyBindingAdapter holder, int position) {
        //setting other data
        holder.layoutBinding.setHourlyWeatherData(hourlyWeatherList.get(position));
        holder.layoutBinding.setIsHRVFahrenheitEnabled(holder.isFahrenheitEnabled);
        holder.layoutBinding.setRvHourlyClickLitener((MyClickInterFace) holder.context);
        //handling expandable layout
        holder.layoutBinding.hourlyRvExpandBtn.setOnClickListener(v->{
            if (holder.layoutBinding.hourlyRvExpandableLayout.getVisibility()==View.VISIBLE){
                TransitionManager.beginDelayedTransition(holder.layoutBinding.hourlyWeatherRvCard,
                        new AutoTransition());
                holder.layoutBinding.hourlyRvExpandableLayout.setVisibility(View.GONE);
                holder.layoutBinding.hourlyRvExpandImg.animate().rotation(0);
            }else{
                TransitionManager.beginDelayedTransition(holder.layoutBinding.hourlyWeatherRvCard,
                        new AutoTransition());
                holder.layoutBinding.hourlyRvExpandableLayout.setVisibility(View.VISIBLE);
                holder.layoutBinding.hourlyRvExpandImg.animate().rotation(180);
            }
        });

        holder.layoutBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }

    public void setHourlyListData(List<Hourly> hourly) {
        hourlyWeatherList.clear();
        hourlyWeatherList.addAll(hourly);
        notifyDataSetChanged();
    }

    static class HourlyBindingAdapter extends RecyclerView.ViewHolder{

        private boolean isFahrenheitEnabled;
        private Context context;
        private HourlyWeatherForecastLayoutBinding layoutBinding;
        public HourlyBindingAdapter(@NonNull HourlyWeatherForecastLayoutBinding itemView) {
            super(itemView.getRoot());
            layoutBinding=itemView;
            context = layoutBinding.getRoot().getContext();
            isFahrenheitEnabled = context.getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                    .getBoolean(MainActivity.IS_FAHRENHEIT, false);

            //creating pair and setting
            Pair []pairsList=new Pair[3];
            pairsList[0]=new Pair<View,String>(layoutBinding.hourlyWeatherConditionImg, "hourlyWeatherCondtionTrans");
            pairsList[1]=new Pair<View,String>(layoutBinding.hourlyRvTempTv, "hourlyWeatherTempTrans");
            pairsList[2]=new Pair<View,String>(layoutBinding.hourlyRvRainChancesIcon, "hourlyRvRainChancesTrans");
            layoutBinding.setTransPair(pairsList);

        }
    }
}
