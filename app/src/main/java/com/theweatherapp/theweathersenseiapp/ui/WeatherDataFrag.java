package com.theweatherapp.theweathersenseiapp.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.theweatherapp.theweathersenseiapp.AboutAppActivity;
import com.theweatherapp.theweathersenseiapp.MainActivity;
import com.theweatherapp.theweathersenseiapp.R;
import com.theweatherapp.theweathersenseiapp.databinding.WeatherDataBinding;
import com.theweatherapp.theweathersenseiapp.navHeaderData.WeatherGuideActivity;
import com.theweatherapp.theweathersenseiapp.ui.appBSD.CurrentMoreBSD;
import com.theweatherapp.theweathersenseiapp.ui.rvAdapters.DailyRvAdapter;
import com.theweatherapp.theweathersenseiapp.ui.rvAdapters.HourlyRvAdapter;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Daily;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;
import static com.theweatherapp.theweathersenseiapp.MainActivity.IS_BACKGROUND_IMAGE;
import static com.theweatherapp.theweathersenseiapp.MainActivity.IS_FAHRENHEIT;
import static com.theweatherapp.theweathersenseiapp.MainActivity.SHOW_TIPS;
import static com.theweatherapp.theweathersenseiapp.MainActivity.WEAHTER_SENSEI_PREFERENCES;

public class WeatherDataFrag extends Fragment implements View.OnClickListener {

    public static final String BSD_ARGS = "weather data args";
    private static final String TAG = "tagg";
    private static final int REQUEST_CHECK_SETTINGS = 12312;
    private final int LOCATION_PERMS_REQ_CODE = 1831;
    private WeatherDataFragViewModel mViewModel;
    private WeatherDataBinding dataBinding;
    private boolean isBgEnabled;

    //location data fields
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //receiver for location enable/disable
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                requestLocationUpdates();
                context.unregisterReceiver(broadcastReceiver);
            }
        }
    };
    private boolean isFahrenheitEnabled;
    //rv adapters
    private HourlyRvAdapter hourlyRvAdapter;
    private DailyRvAdapter dailyRvAdapter;
    private Daily theWeatherData;
    private ActivityResultLauncher<String[]> permissionsLauncher;


    private void setUpLocationFormalities() {
        if (getActivity() == null) return;
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = new SettingsClient(requireActivity().getApplicationContext());
        //checking if gps is on or not if not show dialogue to activate
        Task<LocationSettingsResponse> responseTask = settingsClient
                .checkLocationSettings(builder.build());
        responseTask.addOnSuccessListener(locationSettingsResponse -> requestLocationUpdates());

        responseTask.addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: " + e.getMessage() + " localized msg: " + e.getLocalizedMessage());
            //registering gps broadcast receiver
            requireActivity().getApplicationContext()
                    .registerReceiver(broadcastReceiver,
                            new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
            //showing dialogue for enabling gps
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    if (getActivity() == null) return;
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(),
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }

        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLocations() == null) {
                    return;
                }
                locationProviderClient.removeLocationUpdates(locationCallback);
                Location location = locationResult.getLastLocation();
                mViewModel.getWeatherData(location.getLatitude(),
                        location.getLongitude(), isFahrenheitEnabled ? "imperial" : "metric");
            }
        };

        locationProviderClient = LocationServices
                .getFusedLocationProviderClient(requireActivity().getApplicationContext());

    }

    private void requestLocationUpdates() {
        if (SDK_INT >= M) {
            if (getActivity() != null) {
                if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }

        if (locationProviderClient != null) {
            dataBinding.weatherFragProgressBar.setVisibility(View.VISIBLE);
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SDK_INT >= M)
            permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result != null && result.containsKey(Manifest.permission.ACCESS_FINE_LOCATION) && result.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    getLastLocation();
                }
            });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getActivity() == null) return null;
        dataBinding = WeatherDataBinding.inflate(inflater, container, false);
        dataBinding.setLifecycleOwner(requireActivity());
        //end of colouring
        dataBinding.weatherDataFragLocateMe.setOnClickListener(this);

        dataBinding.weatherDataCurrentMoreTv.setOnClickListener(this);

        //getting preferences
        isFahrenheitEnabled = requireActivity().getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE)
                .getBoolean(IS_FAHRENHEIT, false);

        dataBinding.setIsFahrenheitEnabled(isFahrenheitEnabled);

        //setting up recycler view
        dataBinding.weatherDailyRv.
                setLayoutManager(new LinearLayoutManager(
                        dataBinding.getRoot().getContext(), RecyclerView.HORIZONTAL, false));
        dataBinding.weatherHourlyRv.setLayoutManager(new LinearLayoutManager(dataBinding.getRoot().getContext(), RecyclerView.HORIZONTAL
                , false));

        //setting daily and hourly details
        dataBinding.setIsDailyRvExpnaded(true);
        dataBinding.setIsHourlyRvExpnaded(true);
        dataBinding.setListener(this);

        //setting bg visibility
        isBgEnabled = requireActivity().getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE)
                .getBoolean(IS_BACKGROUND_IMAGE, true);
        dataBinding.setIsBGEnabled(isBgEnabled);
        Log.d(TAG, "onCreateView: ");

        loadAds();
        return dataBinding.getRoot();
    }

    private void loadAds() {
        AdLoader adLoader = new AdLoader.Builder(dataBinding.getRoot().getContext(), MainActivity.NATIVE_AD_UNIT_ID)
                .forNativeAd(nativeAd -> {
                    dataBinding.nativeAdCard.setVisibility(View.VISIBLE);
                    /*NativeTemplateStyle templateStyle = new NativeTemplateStyle.Builder()
                            .withMainBackgroundColor(new ColorDrawable(Color
                                    .parseColor("#BAD1D1D1")))
                            .build();
                    dataBinding.myTemplate.setStyles(templateStyle);*/
                    dataBinding.myTemplate.setNativeAd(nativeAd);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) return;
        setUpViews();
        setUpLocationFormalities();
        mViewModel = new ViewModelProvider(requireActivity())
                .get(WeatherDataFragViewModel.class);
        mViewModel.getLiveData().observe(requireActivity(), weatherData -> {
            if (weatherData != null) {
                //setting bsd weather data which is Daily
                theWeatherData = weatherData.getDaily().get(0);
                //progress bar setting
                dataBinding.weatherFragProgressBar.setVisibility(View.GONE);
                //
                dataBinding.setWeatherDataFragVM(weatherData);
                if (hourlyRvAdapter == null) {
                    hourlyRvAdapter = new HourlyRvAdapter(weatherData.getHourly());
                    dailyRvAdapter = new DailyRvAdapter(weatherData.getDaily());
                    dataBinding.weatherHourlyRv.setAdapter(hourlyRvAdapter);
                    dataBinding.weatherDailyRv.setAdapter(dailyRvAdapter);
                } else {
                    hourlyRvAdapter.setHourlyListData(weatherData.getHourly());
                    dailyRvAdapter.setDailyData(weatherData.getDaily());
                }
                dataBinding.setIsBGEnabled(isBgEnabled);
            }

        });

        if (getActivity() == null) return;
        if (SDK_INT >= M) {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                    && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                permissionsLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION});
            } else {
                getLastLocation();
            }
        } else {
            getLastLocation();
        }

    }


    private void setUpViews() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(dataBinding.mainFragToolbar);
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        ConstraintLayout constraintLayout = requireActivity().findViewById(R.id.main_constraint_layout);
//        drawerLayout.setDrawerTitle(GravityCompat.START, "Weather Sensei");
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_exit) {
                requireActivity().finish();
                return true;
            } else if (item.getItemId() == R.id.menu_weather_guide) {
                Intent intent = new Intent(requireActivity(), WeatherGuideActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.menu_about) {
                Intent intent = new Intent(requireActivity(), AboutAppActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.menu_share_app) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_string)
                        + " \n\n https://play.google.com/store/apps/details?id=com.theweatherapp.theweathersenseiapp");
                requireActivity().startActivity(Intent.createChooser(intent, "Choose app to share"));
                return true;
            }
            return false;
        });
        //setting up drawer toggle button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), drawerLayout, dataBinding.mainFragToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slide_x = drawerView.getWidth() * slideOffset;
                constraintLayout.setTranslationX(slide_x);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerSlideAnimationEnabled(true);

        //getting switch buttons in nav view menu
        SwitchMaterial fahrenheitSwitchMaterial = (SwitchMaterial) navigationView
                .getMenu().findItem(R.id.menu_fahrenheit_switch).getActionView();
        SwitchMaterial showTipsSwitch = (SwitchMaterial) navigationView
                .getMenu().findItem(R.id.menu_tips_switch).getActionView();
        SwitchMaterial backgroundImageSwitch = (SwitchMaterial) navigationView
                .getMenu().findItem(R.id.menu_backgroung_image_switch).getActionView();


        SharedPreferences.Editor perfsEditor = requireActivity()
                .getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE).edit();


        //switch manipulation
        showTipsSwitch.setChecked(requireActivity().getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE)
                .getBoolean(SHOW_TIPS, true));
        fahrenheitSwitchMaterial.setChecked(requireActivity().getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE)
                .getBoolean(IS_FAHRENHEIT, false));
        backgroundImageSwitch.setChecked(requireActivity().getSharedPreferences(WEAHTER_SENSEI_PREFERENCES, MODE_PRIVATE)
                .getBoolean(IS_BACKGROUND_IMAGE, true));
        //switch listeners
        backgroundImageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            perfsEditor.putBoolean(IS_BACKGROUND_IMAGE, b);
            perfsEditor.apply();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_holder, new WeatherDataFrag())
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        fahrenheitSwitchMaterial.setOnCheckedChangeListener((compoundButton, b) -> {
            perfsEditor.putBoolean(IS_FAHRENHEIT, b);

            perfsEditor.apply();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_holder, new WeatherDataFrag())
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        showTipsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            perfsEditor.putBoolean(SHOW_TIPS, b);
            perfsEditor.apply();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    private void getLastLocation() {
        if (getActivity() == null) return;
        if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                mViewModel.getWeatherData(task.getResult().getLatitude(), task.getResult().getLongitude(),
                        isFahrenheitEnabled ? "imperial" : "metric");
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.weather_data_frag_locate_me) {
            requestLocationUpdates();
        } else if (view.getId() == R.id.weather_data_frag_hourly_drop_down_btn) {
            dataBinding.setIsHourlyRvExpnaded(!dataBinding.getIsHourlyRvExpnaded());
        } else if (view.getId() == R.id.weather_data_frag_daily_drop_down_btn) {
            dataBinding.setIsDailyRvExpnaded(!dataBinding.getIsDailyRvExpnaded());
        } else if (view.getId() == R.id.weather_data_current_more_tv) {
            if (theWeatherData != null) {
                CurrentMoreBSD currentMoreBSD = new CurrentMoreBSD();
                Bundle bundle = new Bundle(1);
                bundle.putParcelable(BSD_ARGS, theWeatherData);
                currentMoreBSD.setArguments(bundle);
                currentMoreBSD.setStyle(DialogFragment.STYLE_NORMAL, R.style.Custom_BSD_Style);
                currentMoreBSD.show(requireActivity().getSupportFragmentManager(), "current_more_bsd");
            }
        }

    }

}