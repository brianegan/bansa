package com.brianegan.bansaKotlin.asyncaction

import com.brianegan.bansa.Action
import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Store
import nl.komponents.kovenant.task
import com.brianegan.bansaKotlin.invoke;

sealed class AsyncAction(val type:String):Action {
    class Started<P>(type:String,body: () -> P): AsyncAction(type) {
        val promise = task{body()}
        fun asCompleted() = Completed<P>(type,promise.get())
        fun asFailed() = Failed(type,promise.getError())
        /**
         * block until we get back the result from the promise
         */
        fun resolve(): AsyncAction{
            val res:AsyncAction
            try {
                res=Completed<P>(type,promise.get())
            } catch (e:Exception) {
                res= Failed(type,e)
            }
            return res
        }
    }
    class Completed<P>(type:String,val payload:P): AsyncAction(type)
    class Failed(type:String, val error:Exception): AsyncAction(type)
}
/**
 * a middleware that knows how to handle actions of type [AsyncAction]
 * based on ideas from
 * https://github.com/acdlite/redux-promise
 * https://github.com/acdlite/flux-standard-action
 * But do not return the unwrapped promise to the client (the external caller to store.dispatch),
 * ( because not very useful for chaining: we need to cast the result back to Promise, kotlin is not like javascript!!!)
 * Actually, because of this returning the action from the middleware is less useful than in javascript. TODO need to think about it
 *
 * Created by daely on 5/17/2016.
 */
class AsyncActionMiddleWare<S> : Middleware<S> {
    override fun dispatch(store: Store<S>, action: Action, next: NextDispatcher) {
        if(action is AsyncAction.Started<*>) {
            //queue some async actions when the promise resolves
            action.promise
                    .success { store.dispatch(action.asCompleted()) }
                    .fail { store.dispatch(action.asFailed()) }
            //do not pass back the unwrapped promise as result of the middleware like is done for example in https://github.com/acdlite/redux-promise
            //because not very useful for chaining .since we need anyway to cast back to the right type, just return the AsyncAction.Started<A> itself
        }
        return next(action)
    }

}
/* ORIGINAL JAVASCRIPT CODE FROM https://github.com/acdlite/redux-promise
function isPromise(val) {
  return val && typeof val.then === 'function';
}

export default function promiseMiddleware({ dispatch }) {
  return next => action => {
    if (!isFSA(action)) {
      return isPromise(action)
        ? action.then(dispatch)
        : next(action);
    }

    return isPromise(action.payload)
      ? action.payload.then(
          result => dispatch({ ...action, payload: result }),
          error => dispatch({ ...action, payload: error, error: true })
        )
      : next(action);
  };
}
 */