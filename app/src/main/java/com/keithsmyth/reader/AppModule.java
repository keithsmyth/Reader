package com.keithsmyth.reader;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides public Application provideApplication() {
        return application;
    }
}
