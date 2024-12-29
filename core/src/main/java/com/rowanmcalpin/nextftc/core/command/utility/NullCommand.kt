package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.command.Command

/**
 * This command does nothing and serves as a placeholder. It is designed to use up as little
 * processing space as possible, by setting isDone to true instantly. 
 * @param parameters used for when it is a placeholder for a command that takes parameters
 */
class NullCommand(vararg parameters: Any): Command() {
    override val isDone = true
}