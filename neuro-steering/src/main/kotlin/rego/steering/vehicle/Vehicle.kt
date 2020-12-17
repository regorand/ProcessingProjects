package rego.steering.vehicle

import processing.core.PApplet
import processing.core.PVector
import rego.steering.track.Constants

class Vehicle(val pos: PVector, val context: PApplet) {

    private var isAlive = true

    private var velocity = 0f

    private var angle = 0f

    private var momentumAngle = angle

    fun doFrame(instruction: SteeringInstruction) {
        if(isAlive) {
            doSteering(instruction)
            doPhysics()
        }
        display()
    }

    fun crash() {
        isAlive = false
    }

    fun doSteering(instruction: SteeringInstruction) {
        velocity += instruction.gas - instruction.brake
        angle += instruction.steerRight - instruction.steerLeft
    }

    fun doPhysics() {
        val deltaPos = PVector.fromAngle(angle).mult(velocity)
        pos.x += deltaPos.x
        pos.y += deltaPos.y

        //Drag
        velocity -= (velocity * Constants.DRAG_FACTOR)
    }

    fun display() {
        context.fill(255f, 0f, 0f)
        context.stroke(255f, 0f, 0f)
        context.ellipse(pos.x, pos.y, 4f, 4f)
        val displayAngle = PVector.fromAngle(angle).mult(10f)
        context.line(pos.x, pos.y, pos.x + displayAngle.x, pos.y + displayAngle.y)
    }

}