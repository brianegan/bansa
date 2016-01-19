package com.brianegan.rxredux.listOfTrendingGifs;

import com.brianegan.RxRedux.Action;
import com.brianegan.RxRedux.ApplyMiddlewareKt;
import com.brianegan.RxRedux.CreateStoreKt;
import com.brianegan.RxRedux.Store;
import dagger.Module;
import dagger.Provides;
import kotlin.jvm.functions.Function1;
import rx.schedulers.Schedulers;

import javax.inject.Singleton;

@Module(library = true)
public class ReduxModule {
    public ReduxModule() {
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
