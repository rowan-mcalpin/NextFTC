package com.rowanmcalpin.nextftc.subsystems

/**
 * A subsystem represents any mechanism on the robot that cannot be used by two things at once. It
 * serves two purposes: first, the CommandScheduler prevents more than one command from using a
 * subsystem at a time, and second, the CommandScheduler runs the periodic and inUsePeriodic
 * functions every loop.
 *
 * P.S. I know that the name of an interface is supposed to be an adjective, so if someone could
 * help me turn the word subsystem into an adjective that would be greatly appreciated.
 */
interface Subsystem {

    /**
     * The CommandScheduler runs this function when the Subsystem is registered using
     * registerSubsystems(). It should contain anything that needs to be done once at the start of
     * the program, like initializing motors or servos.
     */
    fun initialize() { }

    /**
     * The CommandScheduler runs this function every loop (provided the subsystem has been
     * registered using registerSubsystems()). It should contain anything related to the subsystem
     * that needs to be done repeatedly, such as displaying telemetry information.
     */
    fun periodic() { }

    /**
     * This function is similar to periodic, but it's only run if there's an active command using
     * this subsystem.
     */
    fun inUsePeriodic() { }
}