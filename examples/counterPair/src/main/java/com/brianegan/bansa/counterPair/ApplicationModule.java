package com.brianegan.bansa.counterPair;

import dagger.Module;
import dagger.Provides;

import android.content.Context;

@Module(
        includes = StoreModule.class,
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
