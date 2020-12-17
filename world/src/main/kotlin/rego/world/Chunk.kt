package rego.world

import processing.core.PApplet
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


class Chunk(val drawContext: PApplet) {
    var color: Triple<Int, Int, Int> = Triple(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

    private val waterLevel = 150
    private val grassLevel = 200
    private val mountainLevel  = 240

    fun draw(lowerX: Int, lowerY: Int, sizeX: Int, sizeY: Int) {
        drawContext.fill(color.first.toFloat(), color.second.toFloat(), color.third.toFloat())
        if (sizeX < 500) {
            drawContext.stroke(color.first.toFloat(), color.second.toFloat(), color.third.toFloat())
        } else {
            drawContext.stroke(0f)
        }
        drawContext.rect(lowerX.toFloat(), lowerY.toFloat(), sizeX.toFloat(), sizeY.toFloat())
    }

    fun setColor(value: Float) {
        color = when {
            value < waterLevel -> {
                val depth = waterLevel - value
                Triple(0, 0, 255 - depth.toInt())
            }
            value < grassLevel -> {
                val height = grassLevel - value
                Triple(0, 150 + height.toInt(), 0)
            }
            value < mountainLevel -> {
                val height = mountainLevel - value
                Triple(100 + (height * 2).toInt(), 100 + (height * 2).toInt(), 100 + (height * 2).toInt())
            }
            else -> Triple(255, 255, 255)
        }
    }

    fun setColorWhite(value: Float) {
        color = Triple(value.roundToInt(), value.roundToInt(), value.roundToInt())
    }

    fun findColor(x: Int, y: Int, extra: Int) {
        val c = sqrt((x*x + y*y).toDouble()) + (extra)
        val value = sin(c) * 256
        color = Triple(value.roundToInt(), value.roundToInt(), value.roundToInt())

    }

    fun findColor(x: Int, y: Int) {
        val c = sqrt((x*x + y*y).toDouble())
        val value = sin(c) * 256
        color = Triple(value.roundToInt(), value.roundToInt(), value.roundToInt())

        /*
        val red = sin(x.toDouble() / 10) * 256
        val green = sin(y.toDouble() / 10) * 256
        val blue = sin(x.toDouble() / 10) * 256
        RGB = Triple(red.roundToInt(), green.roundToInt(), blue.roundToInt())
         */
    }

}