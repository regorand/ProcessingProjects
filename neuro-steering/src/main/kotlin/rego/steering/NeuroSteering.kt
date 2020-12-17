package rego.steering

import processing.core.PApplet
import processing.core.PVector
import rego.steering.track.Constants
import rego.steering.track.Track
import rego.steering.vehicle.SteeringInstruction
import rego.steering.vehicle.Vehicle

class NeuroSteering: PApplet() {

    private val keyPressedMap: MutableMap<Char, Boolean> = mutableMapOf()

    private val track = Track(this)

    private val vehicle = Vehicle(PVector(150f, 150f), this)

    override fun settings() {
        size(1080, 720)
    }

    override fun setup() {
        frameRate(30f)
        ellipseMode(RADIUS)
        background(0)
    }

    override fun draw() {
        stroke(255)
        track.display()
        vehicle.doFrame(getSteering())
        if (!track.isOnTrack(vehicle.pos)) {
            vehicle.crash()
        }
    }

    fun getSteering(): SteeringInstruction {
        var gas = 0f
        var brake = 0f
        var steerLeft = 0f
        var steerRight = 0f
        if (keyPressedMap['w'] == true)
        {
            gas = Constants.MAX_GAS
        }
        if (keyPressedMap['s'] == true)
        {
            brake = Constants.MAX_GAS
        }
        if (keyPressedMap['a'] == true)
        {
            steerLeft = 0.1f
        }
        if (keyPressedMap['d'] == true)
        {
            steerRight = 0.1f
        }
        return SteeringInstruction(gas, brake, steerLeft, steerRight)
    }

    override fun keyPressed() {
        keyPressedMap[key] = true
    }

    override fun keyReleased() {
        keyPressedMap[key] = false
    }

}