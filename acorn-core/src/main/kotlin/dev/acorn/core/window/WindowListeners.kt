package dev.acorn.core.window

import dev.acorn.core.events.Subscription

fun Window.onResize(listener: (WindowResized) -> Unit): Subscription =
    events.subscribe { if(it is WindowResized) listener(it) }

fun Window.onFramebufferResize(listener: (FramebufferResized) -> Unit): Subscription =
    events.subscribe { if(it is FramebufferResized) listener(it) }