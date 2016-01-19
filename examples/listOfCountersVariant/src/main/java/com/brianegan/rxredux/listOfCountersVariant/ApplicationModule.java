package com.brianegan.rxredux.listOfCountersVariant;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = ReduxModule.class,
        injects = {
                RootActivity.class
        },
        library = true
)
public class ApplicationModule {
    private final Context application;

    public ApplicationModule(Context application) {
        this.application = application;
    }

    @Provides
    Context provideApplicationContext() {
        return application;
    }
}
