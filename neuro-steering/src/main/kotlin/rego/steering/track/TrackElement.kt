package rego.steering.track

import processing.core.PApplet
import processing.core.PVector
import kotlin.math.max
import kotlin.math.min

class TrackElement(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val context: PApplet) {

    private val lowerX = min(x1, x2)
    private val lowerY = min(y1, y2)
    private val higherX = max(x1, x2)
    private val higherY = max(y1, y2)
    private val width = higherX - lowerX
    private val height = higherY - lowerY

    fun display() {
        context.rect(lowerX.toFloat(), lowerY.toFloat(), width.toFloat(), height.toFloat())
    }

    fun isOnTrack(point: PVector): Boolean {
        val bool = point.x > lowerX
                && point.x < higherX
                && point.y > lowerY
                && point.y < higherY
        return bool
    }

}