package com.brianegan.bansa.counter

import android.content.Context
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import trikita.anvil.Anvil

abstract class RenderableDrawerLayout : DrawerLayout, Anvil.Renderable {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Anvil.mount(this, this)
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Anvil.unmount(this)
    }

    abstract override fun view()
}
