package com.rowanmcalpin.nextftc.core.control.controllers

@Deprecated("Not necessary or recommended", replaceWith = ReplaceWith("PIDFController"))
class PDController(kP: Double,
                   kD: Double,
                   target: Double = 0.0,
                   setPointTolerance: Double = 0.0
) : PIDFController(kP=kP, kD=kD, target=target, setPointTolerance=setPointTolerance)