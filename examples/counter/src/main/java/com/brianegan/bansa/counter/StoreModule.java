package com.brianegan.bansa.counter;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.CreateStoreKt;
import com.brianegan.bansa.Store;
import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

import javax.inject.Singleton;

@Module(library = true)
public class StoreModule {
    public StoreModule() {
    }

    @Provides
    @Singleton
    public Store<ApplicationState, Action> provideStore() {
        return CreateStoreKt.createStore(
                new ApplicationState(),
                ApplicationReducerKt.getApplicationReducer(),
                Schedulers.newThread()
        );
    }
}
