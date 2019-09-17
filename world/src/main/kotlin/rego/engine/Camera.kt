package rego.engine

import rego.world.Chunk
import kotlin.math.min

class Camera (val chunkSize: Int, val worldSizeX: Int, val worldSizeY: Int, val screenWidth: Int, val screenHeight: Int){

    var centerX = (worldSizeX / 2) * chunkSize
    var centerY = (worldSizeY / 2) * chunkSize

    private var zoomLevel = 3.0

    fun draw(worldGrid: List<List<Chunk>>) {
        drawNew(worldGrid)
        drawOld(worldGrid)
    }

    fun drawNew(worldGrid: List<List<Chunk>>) {
        val firstPixelX = centerX - screenWidth / 2
        val firstPixelY = centerY - screenWidth / 2

        var offsetX = 0
        var offsetY = 0
        if (firstPixelX < 0) {
            offsetX -= firstPixelX
        }
        if (firstPixelY < 0) {
            offsetY -= firstPixelY
        }

        val firstChunkX = firstPixelX / chunkSize
        val firstChunkY = firstPixelY / chunkSize


    }

    fun drawOld(worldGrid: List<List<Chunk>>) {
        //TODO fix bug where scrolling isnt centered -> center not correctly used/calculated ?

        val deltaChunkX = (((screenWidth / 2) / chunkSize + 1) / zoomLevel).toInt()
        val deltaChunkY = (((screenHeight / 2) / chunkSize + 1) / zoomLevel).toInt()

        var startChunkX = ((centerX.toDouble() / (chunkSize * zoomLevel).toInt()) - deltaChunkX).toInt()
        var startChunkY = ((centerY.toDouble() / (chunkSize * zoomLevel).toInt()) - deltaChunkY).toInt()

        var endChunkX = ((centerX.toDouble() / (chunkSize * zoomLevel).toInt()) + deltaChunkX).toInt() + 1
        var endChunkY = ((centerY.toDouble() / (chunkSize * zoomLevel).toInt()) + deltaChunkY).toInt() + 1

        //var pixelDiffX = (screenWidth - (worldSizeX * chunkSize * zoomLevel).toInt()) / 2
        var pixelDiffX = if (-startChunkX * (chunkSize * zoomLevel) <= 0) 0 else -startChunkX * (chunkSize * zoomLevel).toInt()
        var pixelDiffY = if (-startChunkY * (chunkSize * zoomLevel) <= 0) 0 else -startChunkY * (chunkSize * zoomLevel).toInt()

        pixelDiffX -= (centerX % (zoomLevel * chunkSize)).toInt()
        pixelDiffY -= (centerY % (zoomLevel * chunkSize)).toInt()

        if (startChunkX < 0) {
            endChunkX -= startChunkX //use subtraction to add startChunk difference because it will always be a negative number
            startChunkX = 0
        }
        if (endChunkX >= worldGrid.size) {
            endChunkX = worldGrid.size - 1
        }
        if (startChunkY < 0) {
            endChunkY -= startChunkY //use subtraction to add startChunk difference because it will always be a negative number
            startChunkY = 0
        }
        if (endChunkY >= worldGrid.first().size) {
            endChunkY = worldGrid.first().size - 1
        }

        for ((xCount, x) in (startChunkX..endChunkX).withIndex()) {
            for ((yCount, y) in (startChunkY..endChunkY).withIndex()) {
                val drawnChunkSize = (chunkSize * zoomLevel).toInt()
                worldGrid[x][y].draw(
                    (xCount * drawnChunkSize + pixelDiffX),
                    (yCount * drawnChunkSize + pixelDiffY),
                    drawnChunkSize,
                    drawnChunkSize)
            }
        }

    }

    fun move(deltaX: Int, deltaY: Int) {
        centerX += deltaX
        centerY += deltaY

/*
        if (cameraPosX < 0) cameraPosX = 0
        if (cameraPosY < 0) cameraPosY = 0
        if (cameraPosX > worldSizeX * chunkSize - width) cameraPosX = (worldSizeX * chunkSize - width).toInt()
        if (cameraPosY > worldSizeY * chunkSize - height) cameraPosY = (worldSizeY * chunkSize - height).toInt()
        */
    }

    fun scroll(value: Double) {
        zoomLevel += value
        if (zoomLevel < 1) zoomLevel = 1.0
    }

}