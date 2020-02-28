package com.thiagoalexb.dev.clockin.di.modules;

import com.thiagoalexb.dev.clockin.MainActivity;
import com.thiagoalexb.dev.clockin.di.modules.schedule.ScheduleFragmentBuilderModule;
import com.thiagoalexb.dev.clockin.di.modules.schedule.ScheduleModule;
import com.thiagoalexb.dev.clockin.di.modules.schedule.ScheduleScope;
import com.thiagoalexb.dev.clockin.di.modules.schedule.ScheduleViewModelsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ScheduleScope
    @ContributesAndroidInjector(
            modules = { ScheduleFragmentBuilderModule.class, ScheduleViewModelsModule.class, ScheduleModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
