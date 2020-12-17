package rego.template

import processing.core.PApplet

class Template: PApplet() {

    override fun settings() {
        size(1080, 720)
    }

    override fun setup() {
        frameRate(30f)
        ellipseMode(RADIUS)
        background(0)
    }

    override fun draw() {
        rect(100f, 100f, 100f, 100f)
    }
}