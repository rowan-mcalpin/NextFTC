package com.rowanmcalpin.nextftc.command.groups

import com.rowanmcalpin.nextftc.command.Command

/**
 * Runs a group of commands one at a time.
 */
class SequentialCommandGroup(vararg val command: Command): CommandGroup() {

    /**
     * Starts the first command in the list.
     */
    override fun onStart() {
        command.forEach {
            add(it)
        }

        if (commands.isNotEmpty()) {
            commands[0].start()
        }
    }

    /**
     * Starts the first command in the list if it hasn't been started already, then executes it, and
     * checks to see if it's done. If it is done, it runs the command's end function and removes it
     * from the list. It then starts the next command in the list.
     */
    override fun onExecute() {
        if (commands.isNotEmpty()) {
            if (!commands[0].isStarted) {
                commands[0].start()
            }
            commands[0].execute()
            if (commands[0].isDone) {
                commands[0].end(false)
                commands.removeFirst()
                if (commands.isNotEmpty())
                    commands[0].start()
            }
        }
    }
}

