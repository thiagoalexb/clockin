package com.thiagoalexb.dev.clockin.di.components;

import android.app.Application;

import com.thiagoalexb.dev.clockin.BaseApplication;
import com.thiagoalexb.dev.clockin.di.modules.BroadcastModule;
import com.thiagoalexb.dev.clockin.di.modules.ActivityBuilderModule;
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
                ActivityBuilderModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
                BroadcastModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication>  {


    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
