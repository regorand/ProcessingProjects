package rego.steering.track

import processing.core.PApplet
import processing.core.PVector

class Track(private val context: PApplet) {

    private val elements = mutableListOf<TrackElement>()

    init {
        elements.add(TrackElement(100, 100, 980, 200, context))
        elements.add(TrackElement(100, 100, 200, 680, context))
        elements.add(TrackElement(100, 580, 980, 680, context))
        elements.add(TrackElement(880, 100, 980, 680, context))
    }

    fun isOnTrack(point: PVector): Boolean {
        return elements.map { it.isOnTrack(point) }.any { it }
    }

    fun display() {
        context.stroke(255)
        context.fill(255)
        elements.forEach(TrackElement::display)
    }

}