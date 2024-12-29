package com.rowanmcalpin.nextftc.ftc.hardware.motors

interface Controllable {
    var currentPosition: Double
    var power: Double
}