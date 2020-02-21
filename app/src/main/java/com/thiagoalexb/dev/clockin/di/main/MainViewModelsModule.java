package com.thiagoalexb.dev.clockin.di.main;

import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelKey;
import com.thiagoalexb.dev.clockin.ui.address.AddressViewModel;
import com.thiagoalexb.dev.clockin.ui.main.MainViewModel;
import com.thiagoalexb.dev.clockin.ui.report.ReportViewModel;
import com.thiagoalexb.dev.clockin.ui.schedule.EditScheduleViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel.class)
    public abstract ViewModel bindAddressViewModel(AddressViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditScheduleViewModel.class)
    public abstract ViewModel bindEditScheduleViewModel(EditScheduleViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel.class)
    public abstract ViewModel bindReportViewModel(ReportViewModel viewModel);
}
