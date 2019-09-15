package rego.engine

import rego.world.WorldPixel
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

class Camera (val worldPixelSize: Int, val worldSizeX: Int, val worldSizeY: Int, val screenWidth: Int, val screenHeight: Int){



    val centerX = worldSizeX / 2
    val centerY = worldSizeY / 2

    private val zoomLevel = 1.0

    fun draw(worldGrid: List<List<WorldPixel>>) {

        //TODO check lowest amount so that it gets drawn closer to center if world is completely visible, camera center should always be screen center

        val lowerX = centerX - screenWidth/2
        val lowerXWorldPixelIndex: Int = lowerX / worldPixelSize
        val lowerXRemainder = lowerX % zoomLevel

        print("lowerX: $lowerX")
        print("centerX: $centerX")
        print("xPixelIndex: $lowerXWorldPixelIndex")

        val lowerY = centerX - screenHeight/2
        val lowerYWorldPixelIndex: Int = lowerY / worldPixelSize
        val lowerYRemainder = lowerY % zoomLevel

        val amountToDrawX = screenWidth / worldPixelSize + 1
        val upperXWorldPixelIndex = min(lowerXWorldPixelIndex + amountToDrawX, worldGrid.size - 1)


        val amountToDrawY = screenHeight / worldPixelSize + 1
        val upperYWorldPixelIndex = min(lowerYWorldPixelIndex + amountToDrawY, worldGrid.first().size - 1)

        for ((xCount, x) in (lowerXWorldPixelIndex..upperXWorldPixelIndex).withIndex()) {
            for ((yCount, y) in (lowerYWorldPixelIndex..upperYWorldPixelIndex).withIndex()) {
                worldGrid[x][y].draw(
                    (xCount * worldPixelSize - lowerXRemainder).toInt(),
                    (yCount * worldPixelSize - lowerYRemainder).toInt(),
                    worldPixelSize,
                    worldPixelSize)
            }
        }
    }

    fun getVisibleBoundaries() {

    }

}