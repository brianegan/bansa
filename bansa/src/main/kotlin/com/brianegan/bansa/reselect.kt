package com.brianegan.bansa

/**
 * A rewrite for kotlin of https://github.com/reactjs/reselect library for redux (https://github.com/reactjs/redux)
 * see also "Computing Derived Data" in redux documentation http://redux.js.org/docs/recipes/ComputingDerivedData.html
 * Created by Dario Elyasy  on 3/18/2016.
 */
private val defaultEqualityCheck = { a: Any, b: Any -> a === b }

interface memoizer<T> {
    fun memoize(vararg inputArgs: Any): T
}

inline fun <T : Any> Array<out T>.every(transform: (Int, T) -> Boolean): Boolean {
    forEachIndexed { i, t -> if (!transform(i, t)) return false }
    return true
}


// {a:Any,b:Any -> a===b}
fun <T> defaultMemoize(func: (Array<out Any>) -> T, equalityCheck: (a: Any, b: Any) -> Boolean = defaultEqualityCheck) = object : memoizer<T> {
    var lastArgs: Array<out Any>? = null
    var lastResult: T? = null
    override fun memoize(vararg inputArgs: Any): T {
        if (lastArgs != null &&
                lastArgs!!.size == inputArgs.size && inputArgs.every { index, value -> equalityCheck(value, lastArgs!![index]) }) {
            return lastResult!!
        }
        lastArgs = inputArgs
        lastResult = func(inputArgs)
        return lastResult!!
    }
}

interface SelectorInput<S, I> {
    operator fun invoke(state: S): I
}

/**
 * a selector function is a function that map a state object to the input for the selector compute function
 */
class SelectInput<S, I>(val fn: S.() -> I) : SelectorInput<S, I> {
    override operator fun invoke(state: S): I = state.fn()
}



/**
 * note: [Selector] inherit from [SelectorInput] because of support for composite selectors
 */
interface Selector<S, O> : SelectorInput<S, O> {
    val recomputations: Long
    fun resetComputations()
    fun isChanged(): Boolean
    fun resetChanged()
    fun getIfChangedIn(state:S):O? {
        var res =invoke(state)
        if(isChanged()) {
            resetChanged()
            return res
        }
        return null
    }
    fun onChangeIn(state:S,blockfn:(O)->Unit) {
        getIfChangedIn(state)?.let(blockfn)
    }
}


/**
 * abstract base class for all selectors
 */
abstract class AbstractSelector<S, O> : Selector<S, O> {
    protected var _recomputations = 0L
    protected var _lastchanged_recomputations = 0L
    override val recomputations: Long get() = _recomputations
    override fun resetComputations() {
        _recomputations = 0
        _lastchanged_recomputations = 0
    }

    override fun isChanged(): Boolean = _recomputations != _lastchanged_recomputations
    override fun resetChanged() {
        _lastchanged_recomputations = _recomputations
    }


    protected abstract val computeandcount: (i: Array<out Any>) -> O
    /**
     * 'lazy' because computeandcount is abstract. Cannot reference to it before it is initialized in concrete selectors
     * 'open' because we can provide a custom memoizer if needed
     */
    open val memoizer by lazy { defaultMemoize(computeandcount) }  //

}

/**
 * wrapper class for Selector factory methods [create], that basically is used only to capture
 * type information for the state parameter
 */
class SelectorFor<S> {

    /**
     * special single input selector that should be used when you just want to retrieve a single field
     */
    fun <I : Any> field(fn: S.() -> I) = object : AbstractSelector<S, I>() {
        override val computeandcount = fun(i: Array<out Any>): I {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return i[0] as I
        }
        override operator fun invoke(state: S): I {
            return memoizer.memoize(
                    fn(state)
            )
        }
    }

    /**
     * special single input selector that do not perform any computation, just return the selected input
     */
    fun <I : Any> create(si: SelectorInput<S, I>) = object : AbstractSelector<S, I>() {
        override val computeandcount = fun(i: Array<out Any>): I {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return i[0] as I
        }
        override operator fun invoke(state: S): I {
            return memoizer.memoize(
                    si(state)
            )
        }
    }

    /**
     * create a a selector with a single input
     */
    fun <I : Any, O> create(si: SelectorInput<S, I>, computefn: (I) -> O) = object : AbstractSelector<S, O>() {
        override val computeandcount = fun(i: Array<out Any>): O {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return computefn(i[0] as I)
        }

        override operator fun invoke(state: S): O {
            return memoizer.memoize(
                    si(state)
            )
        }
    }

    /**
     * create a a selector with a two inputs
     */
    fun <I0 : Any, I1 : Any, O> create(
            si0: SelectorInput<S, I0>,
            si1: SelectorInput<S, I1>,
            computeFun: (I0, I1) -> O) = object : AbstractSelector<S, O>() {
        override val computeandcount = fun(i: Array<out Any>): O {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return computeFun(i[0] as I0, i[1] as I1)
        }

        override operator fun invoke(state: S): O {
            return memoizer.memoize(
                    si0(state),
                    si1(state)
            )
        }
    }

    /**
     * create a a selector with a 3 inputs
     */
    fun <I0 : Any, I1 : Any, I2 : Any, O> create(
            si0: SelectorInput<S, I0>,
            si1: SelectorInput<S, I1>,
            si2: SelectorInput<S, I2>,
            computefn: (I0, I1, I2) -> O) = object : AbstractSelector<S, O>() {
        override val computeandcount = fun(i: Array<out Any>): O {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return computefn(i[0] as I0, i[1] as I1, i[2] as I2)
        }

        override operator fun invoke(state: S): O {
            return memoizer.memoize(
                    si0(state),
                    si1(state),
                    si2(state)
            )
        }
    }

    /**
     * create a a selector with a 4 inputs
     */
    fun <I0 : Any, I1 : Any, I2 : Any, I3 : Any, O> create(
            si0: SelectorInput<S, I0>,
            si1: SelectorInput<S, I1>,
            si2: SelectorInput<S, I2>,
            si3: SelectorInput<S, I3>,
            computefn: (I0, I1, I2, I3) -> O) = object : AbstractSelector<S, O>() {
        override val computeandcount = fun(i: Array<out Any>): O {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return computefn(i[0] as I0, i[1] as I1, i[2] as I2, i[3] as I3)
        }

        override operator fun invoke(state: S): O {
            return memoizer.memoize(
                    si0(state),
                    si1(state),
                    si2(state),
                    si3(state)
            )
        }
    }

    /**
     * create a a selector with a 5 inputs
     */
    fun <I0 : Any, I1 : Any, I2 : Any, I3 : Any, I4 : Any, O> create(
            si0: SelectorInput<S, I0>,
            si1: SelectorInput<S, I1>,
            si2: SelectorInput<S, I2>,
            si3: SelectorInput<S, I3>,
            si4: SelectorInput<S, I4>,
            computefn: (I0, I1, I2, I3, I4) -> O) = object : AbstractSelector<S, O>() {
        override val computeandcount = fun(i: Array<out Any>): O {
            ++_recomputations
            @Suppress("UNCHECKED_CAST")
            return computefn(i[0] as I0, i[1] as I1, i[2] as I2, i[3] as I3, i[4] as I4)
        }

        override operator fun invoke(state: S): O {
            return memoizer.memoize(
                    si0(state),
                    si1(state),
                    si2(state),
                    si3(state),
                    si4(state)
            )
        }
    }

}
