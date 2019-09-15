package rego.engine

import rego.world.WorldPixel
import kotlin.math.ceil
import kotlin.math.floor

class Camera (val worldPixelSize: Int, val worldSizeX: Int, val worldSizeY: Int, val screenWidth: Int, val screenHeight: Int){



    val centerX = worldSizeX / 2
    val centerY = worldSizeY / 2

    private val zoomLevel = 1.0

    fun draw(worldGrid: List<List<WorldPixel>>) {
        val lowerX = centerX - screenWidth/2
        val lowerXWorldPixelIndex: Int = floor(lowerX / zoomLevel).toInt()
        val lowerXRemainder = lowerX % zoomLevel

        val lowerY = centerX - screenHeight/2
        val lowerYWorldPixelIndex: Int = floor(lowerY / zoomLevel).toInt()
        val lowerYRemainder = lowerY % zoomLevel

        val amountToDrawX = ceil(screenWidth / zoomLevel ).toInt()
        val upperXWorldPixelIndex = lowerXWorldPixelIndex + amountToDrawX


        val amountToDrawY = ceil(screenHeight / zoomLevel).toInt()
        val upperYWorldPixelIndex = lowerYWorldPixelIndex + amountToDrawY

        for ((xCount, x) in (lowerXWorldPixelIndex..upperXWorldPixelIndex).withIndex()) {
            for ((yCount, y) in (lowerYWorldPixelIndex..upperYWorldPixelIndex).withIndex()) {
                worldGrid[x][y].draw()
            }
        }

        }
    }

    fun getVisibleBoundaries() {

    }

}