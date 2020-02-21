package com.thiagoalexb.dev.clockin.di.main;

import com.thiagoalexb.dev.clockin.ui.address.AddressFragment;
import com.thiagoalexb.dev.clockin.ui.main.MainFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.EditScheduleFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract MainFragment constributeMainFragment();

    @ContributesAndroidInjector
    abstract AddressFragment constributeAddressFragment();

    @ContributesAndroidInjector
    abstract EditScheduleFragment constributeEditScheduleFragment();
}
