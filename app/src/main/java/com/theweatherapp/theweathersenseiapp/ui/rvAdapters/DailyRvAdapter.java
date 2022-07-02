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
import com.theweatherapp.theweathersenseiapp.databinding.DailyWeatherForecastRvLayoutBinding;
import com.theweatherapp.theweathersenseiapp.ui.customInrterfaces.MyClickInterFace;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Daily;

import java.util.List;

public class DailyRvAdapter extends RecyclerView.Adapter<DailyRvAdapter.DailyBindingHolder> {

    private List<Daily> dailyWeatherList;
    private int tempRand;

    public DailyRvAdapter(List<Daily> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public DailyBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new DailyBindingHolder(DailyWeatherForecastRvLayoutBinding
                .inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyBindingHolder holder, int position) {

        holder.layoutBinding.setDailyWeaherData(dailyWeatherList.get(position));
        holder.layoutBinding.setIsDRVFahrenheitEnabled(holder.isFahrenheitEnabled);
        holder.layoutBinding.setDailyRvListener((MyClickInterFace) holder.context);
        //handling expandable layout
        holder.layoutBinding.dailyRvExpandBtn.setOnClickListener(v->{
            if (holder.layoutBinding.expandableContainer.getVisibility()==View.VISIBLE){
                TransitionManager.beginDelayedTransition(holder.layoutBinding.dailyWeatherCard,
                        new AutoTransition());
                holder.layoutBinding.expandableContainer.setVisibility(View.GONE);
                holder.layoutBinding.dailyRvExpandImg.animate().rotation(0);
            }else{
                TransitionManager.beginDelayedTransition(holder.layoutBinding.dailyWeatherCard,
                        new AutoTransition());
                holder.layoutBinding.expandableContainer.setVisibility(View.VISIBLE);
                holder.layoutBinding.dailyRvExpandImg.animate().rotation(180);
            }
        });

        holder.layoutBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }

    public void setDailyData(List<Daily> daily) {
        dailyWeatherList.clear();
        dailyWeatherList.addAll(daily);
        notifyDataSetChanged();
    }

    static class DailyBindingHolder extends RecyclerView.ViewHolder{

        private boolean isFahrenheitEnabled;
        private Context context;
        private DailyWeatherForecastRvLayoutBinding layoutBinding;

        public DailyBindingHolder(@NonNull DailyWeatherForecastRvLayoutBinding itemView) {
            super(itemView.getRoot());
            layoutBinding=itemView;
            context=layoutBinding.getRoot().getContext();
            isFahrenheitEnabled = context.getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                    .getBoolean(MainActivity.IS_FAHRENHEIT, false);
            Pair<View,String> []pairsList=new Pair[3];
            pairsList[0]=new Pair<>(layoutBinding.dailyWeatherConditionImg, "dailyWeatherCondtionTrans");
            pairsList[1]=new Pair<>(layoutBinding.dailyRvTempTv, "dailyWeatherTempTrans");
            pairsList[2]=new Pair<>(layoutBinding.dailyRvRainChancesIcon, "dailyRvRainChancesTrans");
            layoutBinding.setDailyTransPair(pairsList);
        }
    }
}
