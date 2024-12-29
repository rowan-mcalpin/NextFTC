package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command updates the Pedro Path follower continuously as long as the OpMode is running.
 */
class UpdateFollower: Command() {
    
    override val isDone: Boolean = false

    override fun update() {
        if (PedroData.follower == null) {
            throw FollowerNotInitializedException()
        }
        
        PedroData.follower!!.update()
    }
}