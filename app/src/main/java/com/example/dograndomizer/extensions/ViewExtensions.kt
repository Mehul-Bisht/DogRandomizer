package com.example.dograndomizer.extensions

import android.view.View

/**
 * Created by Mehul Bisht on 24-10-2021
 */

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.isGone() = this.visibility == View.GONE

