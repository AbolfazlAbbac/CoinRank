package com.example.coinmarketjava.hilt.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class HiltModule {

    @Provides
    @Named("name")
    String abolfazl() {
        return "Joooon abolfazl";
    }

    @Provides
    @Named("family")
    String abbasi() {
        return "Joooon abbasi";
    }
}
