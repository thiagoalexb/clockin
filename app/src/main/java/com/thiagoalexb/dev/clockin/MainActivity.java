package com.thiagoalexb.dev.clockin;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
