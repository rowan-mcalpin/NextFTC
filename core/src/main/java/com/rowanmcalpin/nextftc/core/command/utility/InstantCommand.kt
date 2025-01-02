/*
NextFTC: a user-friendly control library for FIRST Tech Challenge
    Copyright (C) 2025 Rowan McAlpin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.Subsystem

/**
 * This is a LambdaCommand that sets isDone to true instantly. As such, there is no update or stop
 * lambda (since the command finishes instantly). All code should be put in the startLambda.
 *
 * @param lambda the lambda to execute
 * @param subsystemCollection a set of subsystems this command implements
 */
class InstantCommand @JvmOverloads constructor(lambda: () -> Unit = { },
           subsystemCollection: Set<Subsystem> = setOf()): LambdaCommand({ true }, lambda,
            {  }, {  }, subsystemCollection, true) {

    @JvmOverloads
    constructor(lambda: () -> Unit = { }, subsystem: Subsystem): this(lambda, setOf(subsystem))
}