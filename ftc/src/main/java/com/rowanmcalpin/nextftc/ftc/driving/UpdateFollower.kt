package com.rowanmcalpin.nextftc.ftc.driving

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.FollowerNotInitializedException
import com.rowanmcalpin.nextftc.ftc.OpModeData

/**
 * This command updates the Pedro Path follower continuously as long as the OpMode is running.
 */
class UpdateFollower: Command() {
    
    override val isDone: Boolean = false

    override fun update() {
        if (OpModeData.follower == null) {
            throw FollowerNotInitializedException()
        }
        
        OpModeData.follower!!.update()
    }
}