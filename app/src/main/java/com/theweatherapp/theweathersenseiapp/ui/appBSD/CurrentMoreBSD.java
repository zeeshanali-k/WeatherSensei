package com.theweatherapp.theweathersenseiapp.ui.appBSD;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theweatherapp.theweathersenseiapp.MainActivity;
import com.theweatherapp.theweathersenseiapp.databinding.CurrentMoreDetailsBsdBinding;
import com.theweatherapp.theweathersenseiapp.ui.WeatherDataFrag;

public class CurrentMoreBSD extends BottomSheetDialogFragment {

    private String TAG="tagg";

    public CurrentMoreBSD(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");

        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        CurrentMoreDetailsBsdBinding bsdBinding
                = CurrentMoreDetailsBsdBinding.inflate(inflater,container,false);

        bsdBinding.setIsMoreFEnabled(getActivity().getApplicationContext()
                .getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(MainActivity.IS_FAHRENHEIT, false));
        bsdBinding.setCurrentWeatherData(getArguments().getParcelable(WeatherDataFrag.BSD_ARGS));

        return bsdBinding.getRoot();
    }
}
