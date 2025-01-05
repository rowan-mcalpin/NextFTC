package com.rowanmcalpin.nextftc.core.control.controllers

/**
 * Wraps a Controller to deal with angles
 * @param baseController The controller to wrap
 * @author From CTRL ALT FTC, ported to Kotlin & NextFTC by Davis/BeepBot99
 */
class AngularController(private val baseController: Controller): Controller by baseController {
    override fun calculate(reference: Double): Double {
        return baseController.calculateFromError(wrapAngle(baseController.target - reference))
    }

    private fun wrapAngle(radians: Double): Double {
        var output = radians
        while (output > Math.PI) {
            output -= 2 * Math.PI
        }
        while (output < -Math.PI) {
            output += 2 * Math.PI
        }

        // keep in mind that the result is in radians
        return output
    }

}