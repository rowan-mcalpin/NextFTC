package com.rowanmcalpin.nextftc.core.control.controllers.feedforward

class StaticFeedforward(val kF: Double): Feedforward {
    override fun compute(position: Double): Double = kF
}