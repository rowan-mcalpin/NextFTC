package com.rowanmcalpin.nextftc.core.command

class GamepadNotConnectedException(val gamepad: Int): Exception("Gamepad $gamepad is not connected.")
class PIDBoundaryException(): Exception("Minimum boundary must be less than maximum boundary.")