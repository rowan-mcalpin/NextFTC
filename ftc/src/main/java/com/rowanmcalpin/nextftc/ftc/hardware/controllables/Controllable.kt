package com.rowanmcalpin.nextftc.ftc.hardware.controllables

/**
 * An item that has a position and a power.
 */
interface Controllable {
    var currentPosition: Double
    var velocity: Double
    var power: Double
}