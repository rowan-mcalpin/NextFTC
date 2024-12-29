package com.rowanmcalpin.nextftc.ftc.driving

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.FollowerNotInitializedException
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.pedropathing.pathgen.Path
import com.pedropathing.pathgen.PathChain

/**
 * This Command tells the PedroPath follower to follow a specific path or pathchain
 * @param path the path to follow
 * @param holdEnd whether to actively hold position after the path is done being followed
 */
class FollowPath(val path: PathChain, val holdEnd: Boolean = false): Command() {
    
    constructor (path: Path, holdEnd: Boolean = false): this(
        PathChain(path), holdEnd)
    
    // Java single parameter compatability
    constructor(path: Path): this(
        PathChain(
            path
        ), false)
    constructor(path: PathChain): this(path, false)
    
    override val isDone: Boolean
        get() = !OpModeData.follower!!.isBusy

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (OpModeData.follower == null) {
            throw FollowerNotInitializedException()
        }
        
        OpModeData.follower!!.followPath(path, holdEnd)
    }
}