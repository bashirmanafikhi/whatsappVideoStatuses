package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import com.bashirmanafikhi.whatsappvideostatuses.R;

public class AboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);


        String versionName = BuildConfig.VERSION_NAME;
        TextView textViewVersionName = (TextView) findViewById(R.id.versionName);
        textViewVersionName.setText(versionName);

    }
}
