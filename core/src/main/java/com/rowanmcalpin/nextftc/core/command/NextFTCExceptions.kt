package com.rowanmcalpin.nextftc.core.command

class FollowerNotInitializedException: Exception("Follower was not initialized.")
class GamepadNotConnectedException(val gamepad: Int): Exception("Gamepad $gamepad is not connected.")