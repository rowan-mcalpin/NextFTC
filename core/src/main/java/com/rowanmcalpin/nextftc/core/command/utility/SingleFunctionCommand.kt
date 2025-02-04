/*
 * NextFTC: a user-friendly control library for FIRST Tech Challenge
 *     Copyright (C) 2025 Rowan McAlpin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.rowanmcalpin.nextftc.core.command.utility

import com.rowanmcalpin.nextftc.core.command.Command

open class SingleFunctionCommand(val action: () -> Boolean) : Command() {

    constructor(): this({false})

    private var _isDone = true

    override val isDone: Boolean
        get() = _isDone

    override fun update() {
        _isDone = !run()
    }

    open fun run(): Boolean {
        return action()
    }
}