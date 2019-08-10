package rego.ray

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PVector

class Wall(val start: PVector, val end: PVector) {

    fun display(p: PApplet) {
        //p.stroke(255, 255f)
        //p.line(start.x, start.y, end.x, end.y)
    }

    fun isPerfectlyHorizontal(): Boolean {
        return start.y == end.y
    }

    fun getIntersectionPoint(pos: PVector, dir: PVector): LightPoint? {
        val x1 = start.x
        val y1 = start.y
        val x2 = end.x
        val y2 = end.y

        val x3 = pos.x
        val y3 = pos.y
        val x4 = pos.x + dir.x
        val y4 = pos.y + dir.y

        val denom = (x1 - x2)*(y3 - y4) - (y1 - y2) * (x3 - x4)
        if(denom == 0f) return null

        val t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denom
        val u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denom

        if (t > 0 &&  t < 1 && u > 0) {
            val x = x1 + t * (x2 - x1)
            val y = y1 + t * (y2 - y1)
            val pt = PVector(x, y)
            return LightPoint(this, pt, PVector.dist(pos, pt))
        }
        return null
    }

    fun pointIsColliding(point: PVector): Boolean {
        val dist1 = PVector.dist(start, point)
        val dist2 = PVector.dist(point, end)
        val dist3 = PVector.dist(start, end)

        //println("dist 1: $dist1")
        //println("dist 2: $dist2")
        //println("dist 3: $dist3")

        val diff = (dist1 + dist2) - dist3

        val tolerance = 0.05

        if(diff < tolerance) {
            return true
        }

        return false
    }
}