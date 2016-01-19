package com.brianegan.rxredux.randomGif;

import dagger.Module;
import dagger.Provides;

import android.content.Context;

@Module(
        includes = ReduxModule.class,
        injects = {
                Application.class,
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
