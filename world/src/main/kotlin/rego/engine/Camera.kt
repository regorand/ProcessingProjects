package rego.engine

import rego.world.Chunk
import kotlin.math.min

class Camera (val chunkSize: Int, val worldSizeX: Int, val worldSizeY: Int, val screenWidth: Int, val screenHeight: Int){

    var centerX = worldSizeX / 2
    var centerY = worldSizeY / 2

    private val zoomLevel = 3.0

    fun draw(worldGrid: List<List<Chunk>>) {

        //TODO fix Bug that causes black bars when scrolling around while zoomed in

        val deltaChunkX = (((screenWidth / 2) / chunkSize + 1) / zoomLevel).toInt()
        val deltaChunkY = (((screenHeight / 2) / chunkSize + 1) / zoomLevel).toInt()

        var startChunkX = centerX - deltaChunkX
        var endChunkX = centerX + deltaChunkX
        var startChunkY = centerY  - deltaChunkY
        var endChunkY = centerY + deltaChunkY

        //var pixelDiffX = (screenWidth - (worldSizeX * chunkSize * zoomLevel).toInt()) / 2
        var pixelDiffX = -startChunkX * chunkSize
        var pixelDiffY = -startChunkY * chunkSize

        if (startChunkX < 0) {
            startChunkX = 0
        }
        if (endChunkX >= worldGrid.size) {
            endChunkX = worldGrid.size - 1
        }
        if (startChunkY < 0) {
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
        centerX += deltaX / chunkSize
        centerY += deltaY / chunkSize

/*
        if (cameraPosX < 0) cameraPosX = 0
        if (cameraPosY < 0) cameraPosY = 0
        if (cameraPosX > worldSizeX * worldPixelSize - width) cameraPosX = (worldSizeX * worldPixelSize - width).toInt()
        if (cameraPosY > worldSizeY * worldPixelSize - height) cameraPosY = (worldSizeY * worldPixelSize - height).toInt()
        */
    }


    fun getVisibleBoundaries() {

    }

}