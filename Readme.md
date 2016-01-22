Bansa
=======

This is my take on Redux, the state container for JavaScript apps. It's oriented toward developers who like RxJava and Kotlin!

Why the name Bansa? Because it means "Nation" in Filipino. And a nations are "state containers." Get it!? Oh ho ho ho. Continue on for more, dear reader.
 
## What's the goal?

"State Container" is pretty vague. So let me explain what I'm trying to accomplish with this little project: An easier way to write Android UIs & Apps. Perhaps an easy way to start would be a concrete analogy.

Think about List and RecyclerViews on Android. At a high level, you just give them some data and tell them how to render that data. When the data updates, you call `notifyDataSetChanged` to inform the View and it redraws everything for ya. Nice and simple. And heck, with RecyclerView, it'll even perform some pretty impressive animations with just a couple lines of code! 

That's what I'm going for with this project: I want a simple way to declare the way my interface should look, and when the data changes, everything should re-render for me!

So where does Bansa fit into that picture, one might ask? It doesn't say anything on the box about making my life easier as a UI developer!?

Bansa doesn't handle the UI magic. That's left to other tools, namely Anvil. Bansa is responsible for holding or "containing" the state of your application, and informing the UI layer (or database layer, or logging layer, etc) about updates to the state of the application.

The examples in this project simply show one person's vision for how we could think about Android App development, and whether we could make it easier and more fun.
 
## Using it 

We'll demonstrate using a simple counter exampple!

### Define what your state should look like

All we need for a simple counter example is the value of one counter. In one delicate line with Kotlin:

```kotlin
data class ApplicationState(val counter: Int = 0) : State
```

### Define the types of actions of your application

Actions are payloads of information that send data from your application to your state container, which we'll call the `store` from here on out. They are used to determine what actions your `store` should respond to. You send them from your application to a `store` with the `store.dispatch(ACTION)` method. 

So here are the three actions we need for our counter app:

```kotlin
sealed class CounterActions {
    object INIT : CounterAction
    object INCREMENT : CounterAction
    object DECREMENT : CounterAction
}
```

If you're unfamiliar with Kotlin, a `sealed class` is roughly the same as an `ENUM`

### Update the state with Reducers

Actions describe the fact that something happened, but don’t specify how the application’s state changes in response. This is the job of a reducer.
 
Let's see some code and we'll chat about it afterwards:

val reducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is CounterActions.INIT -> ApplicationState()
        is CounterActions.INCREMENT -> state.copy(counter = state.counter.plus(1))
        is CounterActions.DECREMENT -> state.copy(counter = state.counter.minus(1))
        else -> state
    }
}

So what's happening here? A reducer is a function that takes two arguments: the current state of the application, and the action that was fired. It returns an updated version of the state.

In this example, when the "INIT" action is fired, we want to initialize the state of our application. Therefore, we return a new instance.

When the "INCREMENT" action is fired, we want to simply increase the counter by 1.

If "DECREMENT" is fired, we'll need to decrease the counter by 1.

And that's all there is to it: We're simply describing how the state should change in response to an action.

### Create a new `store` (this is your state container)

Now that we've gotten everything setup, we can create our state container!

```kotlin
val counterStore = createStore(ApplicationState(), reducer);
```

And that's it! Now you've got a store. Woohoo! So what now?

### Dispatch (Fire / Trigger) an action

This will cause the reducers we previously wrote 

```kotlin
counterStore.dispatch(INCREMENT)
```

### Get the current state of the store

Say you want to know what the current state of the app is. That's simple:

```kotlin
counterStore.getState() // After the INCREMENT, state is now (counter = 1)
```

### And this is where the magic begins: Subscribe to updates!

```kotlin
counterStore.subscribe({
    textView.setText(store.getState())
})
```

ZOMG MY TEXT JUST UPDATED DUE TO A STATE CHANGE!!!

### Stay with me!

I know what you're saying: "Bri, I've seen all this before. It's called an EVENT BUS." And you know what? You're pretty much right. This isn't anything radical, it's just gluing some pieces together in a different way. But just think about how we can use this simple pattern to simplify our UIs.

### Hooking it up to Anvil

Ok, so now we have to chat about Anvil. It's a simple way to write UIs in Java & Kotlin, and they're magic. You simply describe your UI in Java / Kotlin code (not XML -- give it a few minutes, I think you'll fall in love), update the state, and call `Anvil.render()`. Then everything just updates! We've done it! We've achieved the goal set out in the opening paragraphs!!!

So here's an example view. It's job is to create a linearLayout with three child views: A text view to display the current count, an plus button, and a minus button.

When someone clicks on the plus button, we want to dispatch an `INCREMENT` action. When someone clicks the minus button, we want to dispatch a `DECREMENT` action.

```kotlin
linearLayout {
    size(FILL, WRAP)
    orientation(LinearLayout.VERTICAL)

    textView {
        text("Counts: ${counter.toString()}") // The counter from our state model!
    }

    button {
        size(FILL, WRAP)
        padding(dip(10))
        text("+")
        onClick(store.dispatch(INCREMENT))
    }

    button {
        size(FILL, WRAP)
        padding(dip(5))
        text("-")
        onClick(store.dispatch(DECREMENT))
    }
}
```

And now, we hook anvil and Bansa up together:

```kotlin
counterStore.subscribe({
    Anvil.render()
})
```

That's right: When a user clicks "+", the increment action will be fired, the reducer will update the state, and our UI will auto-render with the new info. WHAAAAAAAAAAT.

Check the examples for way more!

## More docs -- public shameful note for Brian

Write sections for:

  * Async Actions
  * Middleware
  * Combining reducers
  * Breaking down apps into smaller parts

## The tech setup

If you like Buzzwords, then boy howdy, have you found yourself the right repo. 

*Bansa itself has one important dependency, RxJava, and is written in Kotlin.*

The examples use:

  * Kotlin (yay! it's so lovely.)
  * RxJava for Observables
  * Anvil for UI
  * OkHttp for, uh, http
  * Moshi for Json parsing
  * Picasso for image loading
  * Spek for Unit Testing
  * AssertJ to make dem assertions all fluent like

## Inspiration

I'd also like to add that this library is a complete , these ideas aren't new in any way. Credit has to be given to the following projects, listed in autobiographical order.

  * React
  * Om
  * ClojureScript
  * Elm
  * Redux
  * CycleJs
  * re-frame
