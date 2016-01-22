package com.brianegan.bansa.randomGif;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.ApplyMiddlewareKt;
import com.brianegan.bansa.CreateStoreKt;
import com.brianegan.bansa.Store;
import dagger.Module;
import dagger.Provides;
import kotlin.jvm.functions.Function1;
import rx.schedulers.Schedulers;

import javax.inject.Singleton;

@Module(library = true)
public class StoreModule {
    public StoreModule() {
    }

    @Provides
    @Singleton
    public Store<ApplicationState, Action> provideStore() {
        final Store<ApplicationState, Action> store = CreateStoreKt.createStore(
                new ApplicationState(),
                AppReducerKt.getApplicationReducer(),
                Schedulers.newThread()
        );

        final Function1<Store<ApplicationState, Action>, Store<ApplicationState, Action>> applyMiddlewareToStore = ApplyMiddlewareKt.applyMiddleware(GifMiddlewareKt.getGifMiddleware());

        return applyMiddlewareToStore.invoke(store);
    }
}
