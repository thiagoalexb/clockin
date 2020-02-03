package com.thiagoalexb.dev.clockin;

import com.thiagoalexb.dev.clockin.di.components.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().applicantion(this).build();
    }
}
