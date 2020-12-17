package rego.ray

import processing.core.PApplet
import processing.core.PApplet.radians
import processing.core.PVector
import kotlin.math.PI

class Player(private val pos: PVector, private val p: PApplet) {

    private val rays: MutableList<Float> = mutableListOf()

    private val ANGLE = 70
    private val MULTIPLIER = 5

    init {

        for (i in (-ANGLE/2 * MULTIPLIER)..(ANGLE/2 * MULTIPLIER)) {
            rays.add(i.toFloat() / MULTIPLIER)
        }
    }

    constructor(x: Float, y: Float, p: PApplet) : this(PVector(x, y), p)

    constructor(x: Int, y: Int, p: PApplet) : this(PVector(x.toFloat(), y.toFloat()), p)

    private var lightPoints = mutableListOf<LightPoint>()

    private var isColliding = false

    private val size = 3f

    private var angle = 0f

    fun turn(turnAngle: Float) {
        angle += turnAngle
        angle %= (2 * PI).toFloat()
    }

    fun move(speed: Float) {
        val lineAngle = PVector.fromAngle(angle)
        changePos(lineAngle.mult(speed))
    }

    fun changePos(movement: PVector) {
        pos.x += movement.x
        if (pos.x > p.width) {
            pos.x = 0f;
        } else if (pos.x < 0) {
            pos.x = p.width.toFloat()
        }
        pos.y += movement.y
        if (pos.y > p.height) {
            pos.y = 0f;
        } else if (pos.y < 0) {
            pos.y = p.height.toFloat()
        }
    }

    fun changePos(x: Int, y: Int) {
        changePos(PVector(x.toFloat(), y.toFloat()))
    }

    fun display(walls: List<Wall>) {
        with(p) {
            fill(255)
            stroke(255)
            ellipse(pos.x, pos.y, size, size)
            val lineAngle = PVector.fromAngle(angle).mult(20f)
            strokeWeight(3f)
            line(pos.x, pos.y, pos.x + lineAngle.x, pos.y + lineAngle.y)
        }
        displayLines(walls)
    }

    fun displayLines(walls: List<Wall>) {
        lightPoints.clear()
        p.strokeWeight(1f)
        for (ray in rays) {
            val lineAngle = PVector.fromAngle(angle + radians(ray))
            val point = walls
                .map { wall -> wall.getIntersectionPoint(pos, lineAngle) }
                .filter { it != null }
                .minBy { it!!.dist }

            if (point != null) {
                p.stroke(255f, 10f)
                p.strokeWeight(1f)
                p.line(pos.x, pos.y, point.pos.x, point.pos.y)
                lightPoints.add(point)
                p.stroke(255)
                p.strokeWeight(1f)
                //p.ellipse(point.pos.x, point.pos.y, 1f, 1f)
            }

        }
    }

    fun drawLightPoints(walls: List<Wall>) {
        for(wall in walls) {
            val points = lightPoints.filter { it.wall == wall }
            if (!wall.isPerfectlyHorizontal()) {
                val highestPoint = points.maxBy { it.pos.y }
                val lowestPoint = points.minBy { it.pos.y }
                if (highestPoint != null && lowestPoint != null) {
                    p.line(highestPoint.pos.x, highestPoint.pos.y, lowestPoint.pos.x, lowestPoint.pos.y)
                }
            }
        }
    }

    fun checkCollisions(walls: List<Wall>) {
        var anyCollision = false
        for(wall in walls) {
            if(wall.pointIsColliding(pos)) {
                if (!isColliding && !anyCollision) {
                    println("Collision!")
                }
                anyCollision = true
            }
        }
        isColliding = anyCollision
    }
}