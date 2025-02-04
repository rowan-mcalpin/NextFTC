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

/**
 * This command does nothing and serves as a placeholder. It is designed to use up as little
 * processing space as possible, by setting isDone to true instantly. 
 * @param parameters used for when it is a placeholder for a command that takes parameters
 */
class NullCommand(vararg parameters: Any): Command() {
    override val isDone = true
}