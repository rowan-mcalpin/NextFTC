package com.rowanmcalpin.nextftc.core

val Double.inToMm get() = this * 25.4
val Double.mmToIn get() = this / 25.4
val Double.toRadians get() = (Math.toRadians(this))
val Double.square get() = this * this

val Int.inToMm get() = this * 25.4
val Int.mmToIn get() = this / 25.4
val Int.square get() = this * this