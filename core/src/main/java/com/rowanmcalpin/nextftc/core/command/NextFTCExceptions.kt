package com.rowanmcalpin.nextftc.core.command

class GamepadNotConnectedException(val gamepad: Int): Exception("Gamepad $gamepad is not connected.")