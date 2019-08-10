package rego.steering.vehicle

data class SteeringInstruction(val gas: Float,
                               val brake: Float,
                               val steerLeft: Float,
                               val steerRight: Float)