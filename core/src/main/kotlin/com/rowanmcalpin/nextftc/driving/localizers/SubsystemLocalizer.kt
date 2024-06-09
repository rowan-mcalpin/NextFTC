package com.rowanmcalpin.nextftc.driving.localizers

import com.acmerobotics.roadrunner.localization.Localizer
import com.rowanmcalpin.nextftc.subsystems.Subsystem

/**
 * This interface is currently broken. We are working to resolve the issue.
 */
interface SubsystemLocalizer : Subsystem, Localizer {

    override fun periodic() { update() }
}