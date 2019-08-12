package rego.world

import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


class WorldPixel {
    var RGB: Triple<Int, Int, Int> = Triple(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))


    fun getColor(): Triple<Int, Int, Int> {
        return RGB
    }

    fun findRGB(x: Int, y: Int, extra: Int) {
        val c = sqrt((x*x + y*y).toDouble()) + (extra)
        val value = sin(c) * 256
        RGB = Triple(value.roundToInt(), value.roundToInt(), value.roundToInt())
        
    }

    fun findRGB(x: Int, y: Int) {
        val c = sqrt((x*x + y*y).toDouble())
        val value = sin(c) * 256
        RGB = Triple(value.roundToInt(), value.roundToInt(), value.roundToInt())

        /*
        val red = sin(x.toDouble() / 10) * 256
        val green = sin(y.toDouble() / 10) * 256
        val blue = sin(x.toDouble() / 10) * 256
        RGB = Triple(red.roundToInt(), green.roundToInt(), blue.roundToInt())
         */
    }

    fun findPerlinNoise2d(x: Double, y: Double) {

    }

}