package com.rowanmcalpin.nextftc.pedro

import com.pedropathing.follower.Follower

object PedroData {
    var follower: Follower? = null
}

class FollowerNotInitializedException: Exception("Follower was not initialized.")