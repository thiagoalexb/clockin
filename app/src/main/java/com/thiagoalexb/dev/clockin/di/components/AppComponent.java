package com.thiagoalexb.dev.clockin.di.components;

import android.app.Application;

import com.thiagoalexb.dev.clockin.BaseApplication;
import com.thiagoalexb.dev.clockin.data.repository.AddressRepository;
import com.thiagoalexb.dev.clockin.di.modules.AddressViewModelModule;
import com.thiagoalexb.dev.clockin.di.modules.AppModule;
import com.thiagoalexb.dev.clockin.di.viewmodels.ViewModelFactoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
                AddressViewModelModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    AddressRepository addressRepository();

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder applicantion(Application application);

        AppComponent build();
    }
}
