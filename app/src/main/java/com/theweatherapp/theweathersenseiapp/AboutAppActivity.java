package com.theweatherapp.theweathersenseiapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.app_privacy_policy){

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://sites.google.com/view/weathersensei-privacy-policy"));
            startActivity(Intent.createChooser(intent,"Choose app:"));

        }
        else if (view.getId()==R.id.app_terms_conditions){
           new AlertDialog.Builder(this)
                    .setTitle("Terms And Conditions")
                    .setMessage(R.string.app_terms_and_conditions)
                    .setPositiveButton("Ok",null)
                    .create().show();
        }
        else if (view.getId()==R.id.about_activity_back_btn){
            onBackPressed();
        }
    }
}