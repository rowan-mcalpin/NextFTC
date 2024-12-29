package com.rowanmcalpin.nextftc.pedro

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
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
        get() = !PedroData.follower!!.isBusy

    override val subsystems: Set<Subsystem> = setOf(Drivetrain)

    override fun start() {
        if (PedroData.follower == null) {
            throw FollowerNotInitializedException()
        }

        PedroData.follower!!.followPath(path, holdEnd)
    }
}