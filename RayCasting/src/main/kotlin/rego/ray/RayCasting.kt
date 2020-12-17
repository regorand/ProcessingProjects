package rego.ray

import processing.core.PApplet
import processing.core.PVector

class RayCasting: PApplet() {

    private val player = Player(100, 100, this)

    private val keyPressedMap: MutableMap<Char, Boolean> = mutableMapOf()

    private val walls: MutableList<Wall> = mutableListOf()

    override fun settings() {
        size(1080, 720)
    }

    override fun setup() {
        frameRate(30f)
        ellipseMode(RADIUS)
        background(0)
        for (i in 0..5) {
            walls.add(Wall(PVector(random(0f, width.toFloat()),random(0f, height.toFloat())), PVector(random(0f, width.toFloat()),random(0f, height.toFloat()))))
        }
        walls.add(Wall(PVector(0f, 0f), PVector(0f, height.toFloat())))
        walls.add(Wall(PVector(0f, 0f), PVector(width.toFloat() - 1, 0f)))
        walls.add(Wall(PVector(0f, height.toFloat() - 1), PVector(width.toFloat() - 1, height.toFloat() - 1)))
        walls.add(Wall(PVector(width.toFloat() - 1, 0f), PVector(width.toFloat() - 1, height.toFloat() - 1)))

        val pt1 = PVector(0f, 120f)
        val testWall = Wall(PVector(0f, 0f), PVector(0f, height.toFloat() - 1))


        println("pt1 on wall: ${testWall.pointIsColliding(pt1)}")
    }

    override fun draw() {
        background(0)
        player.display(walls)
        player.checkCollisions(walls)
        player.drawLightPoints(walls)
        handleInputs()
        walls.forEach{ w -> w.display(this) }
    }

    fun handleInputs() {
        if (keyPressedMap['w'] == true)
        {
            player.move(5f)
        }
        if (keyPressedMap['s'] == true)
        {
            player.move(-5f)
        }
        if (keyPressedMap['a'] == true)
        {
            player.turn(-0.1f)
        }
        if (keyPressedMap['d'] == true)
        {
            player.turn(0.1f)
        }
    }

    override fun keyPressed() {
        keyPressedMap[key] = true
    }

    override fun keyReleased() {
        keyPressedMap[key] = false
    }
}