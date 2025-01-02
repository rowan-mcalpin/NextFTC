package com.rowanmcalpin.nextftc.core.control.controllers

@Deprecated("Not necessary or recommended", replaceWith = ReplaceWith("PIDFController"))
class PController(kP: Double,
                  target: Double = 0.0,
                  setPointTolerance: Double = 10.0
) : PIDFController(kP=kP, target=target, setPointTolerance=setPointTolerance)