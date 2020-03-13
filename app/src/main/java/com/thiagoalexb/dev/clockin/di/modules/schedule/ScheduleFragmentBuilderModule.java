package com.thiagoalexb.dev.clockin.di.modules.schedule;

import com.thiagoalexb.dev.clockin.ui.address.AddressFragment;
import com.thiagoalexb.dev.clockin.ui.dayschedules.DayScheduleFragment;
import com.thiagoalexb.dev.clockin.ui.dayschedules.DaySchedulesFragment;
import com.thiagoalexb.dev.clockin.ui.schedule.ScheduleFragment;
import com.thiagoalexb.dev.clockin.ui.report.ReportFragment;
import com.thiagoalexb.dev.clockin.ui.dayschedules.EditScheduleFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ScheduleFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract ScheduleFragment constributeMainFragment();

    @ContributesAndroidInjector
    abstract AddressFragment constributeAddressFragment();

    @ContributesAndroidInjector
    abstract EditScheduleFragment constributeEditScheduleFragment();

    @ContributesAndroidInjector
    abstract ReportFragment constributeReportFragment();

    @ContributesAndroidInjector
    abstract DaySchedulesFragment constributeDaySchedulesFragment();

    @ContributesAndroidInjector
    abstract DayScheduleFragment constributeDayScheduleFragment();
}
