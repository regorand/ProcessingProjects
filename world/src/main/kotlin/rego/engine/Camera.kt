package rego.engine

import rego.world.Chunk
import kotlin.math.min

class Camera (val chunkSize: Int, val worldSizeX: Int, val worldSizeY: Int, val screenWidth: Int, val screenHeight: Int){

    var centerX = (worldSizeX / 2) * chunkSize
    var centerY = (worldSizeY / 2) * chunkSize

    private var zoomLevel = 3.0

    fun draw(worldGrid: List<List<Chunk>>) {

        //TODO subtract remainder off position of first chunk, so that moving around isnt always on chunk borders, but on pixels

        val deltaChunkX = (((screenWidth / 2) / chunkSize + 1) / zoomLevel).toInt()
        val deltaChunkY = (((screenHeight / 2) / chunkSize + 1) / zoomLevel).toInt()

        var startChunkX = ((centerX.toDouble() / chunkSize) - deltaChunkX).toInt()
        var startChunkY = ((centerY.toDouble() / chunkSize) - deltaChunkY).toInt()

        var endChunkX = ((centerX.toDouble() / chunkSize) + deltaChunkX).toInt() + 1
        var endChunkY = ((centerY.toDouble() / chunkSize) + deltaChunkY).toInt() + 1

        //var pixelDiffX = (screenWidth - (worldSizeX * chunkSize * zoomLevel).toInt()) / 2
        val pixelDiffX = if (-startChunkX * chunkSize < -chunkSize * zoomLevel) (-chunkSize * zoomLevel).toInt() else -startChunkX * chunkSize
        val pixelDiffY = if (-startChunkY * chunkSize < -chunkSize * zoomLevel) (-chunkSize * zoomLevel).toInt() else -startChunkY * chunkSize

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