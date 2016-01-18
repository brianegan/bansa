package com.brianegan.listOfCounters;

import com.brianegan.RxRedux.Action;
import com.brianegan.RxRedux.CreateStoreKt;
import com.brianegan.RxRedux.Store;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module(library = true)
public class ReduxModule {
    public ReduxModule() {
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
