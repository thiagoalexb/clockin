package com.thiagoalexb.dev.clockin.di.modules;

import androidx.lifecycle.ViewModel;

import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelKey;
import com.thiagoalexb.dev.clockin.ui.address.AddressViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddressViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel.class)
    public abstract ViewModel bindAddressViewModel(AddressViewModel viewModel);
}
