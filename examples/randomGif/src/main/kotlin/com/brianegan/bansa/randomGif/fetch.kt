package com.brianegan.bansa.randomGif

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.schedulers.Schedulers
import java.io.IOException

fun fetch(request: Request, scheduler: Scheduler = Schedulers.io()): Observable<Response> {
    return Observable.create<Response>(object : Observable.OnSubscribe<Response> {
        var client = OkHttpClient()

        override fun call(subscriber: Subscriber<in Response>) {
            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful)
                    throw IOException("${response.code()}: ${response.message()}")

                subscriber.onNext(response)
                subscriber.onCompleted()
            } catch (e: IOException) {
                subscriber.onError(e)
            }
        }
    }).subscribeOn(scheduler)
}
