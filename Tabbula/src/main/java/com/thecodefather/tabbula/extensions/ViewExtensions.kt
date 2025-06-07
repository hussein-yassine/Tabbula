package com.thecodefather.tabbula.extensions

import android.content.res.Resources
import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()