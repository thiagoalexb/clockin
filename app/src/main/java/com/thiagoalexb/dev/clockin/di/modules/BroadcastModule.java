package com.thiagoalexb.dev.clockin.di.modules;

import com.thiagoalexb.dev.clockin.broadcasts.GeofenceBroadcastReceiver;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastModule {

    @ContributesAndroidInjector
    abstract GeofenceBroadcastReceiver constributeGeofenceBroadcastReceiver();
}
