package com.thiagoalexb.dev.clockin.di.modules;

import com.thiagoalexb.dev.clockin.MainActivity;
import com.thiagoalexb.dev.clockin.di.main.MainFragmentBuilderModule;
import com.thiagoalexb.dev.clockin.di.main.MainModule;
import com.thiagoalexb.dev.clockin.di.main.MainScope;
import com.thiagoalexb.dev.clockin.di.main.MainViewModelsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @MainScope
    @ContributesAndroidInjector(
            modules = { MainFragmentBuilderModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
