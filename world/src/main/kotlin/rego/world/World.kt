package rego.world

import processing.core.PApplet
import processing.core.PConstants
import processing.event.MouseEvent

class World: PApplet() {

    lateinit var worldGrid: List<List<WorldPixel>>

    private val keyPressedMap: MutableMap<Char, Boolean> = mutableMapOf()

    private var shiftPressed = false

    var clickedMouseX = 0
    var clickedMouseY = 0

    val worldSizeX = 100
    val worldSizeY = 100

    var worldPixelSize = 20

    var cameraPosX = 0
    var cameraPosY = 0

    var savedCameraX = 0
    var savedCameraY = 0

    var counter = 0

    override fun settings() {
        size(1080, 720)
    }

    override fun setup() {
        worldGrid = List(worldSizeX) { List(worldSizeY) { WorldPixel() } }
        for ((x, yList) in worldGrid.iterator().withIndex()) {
            for ((y, worldPixel) in yList.iterator().withIndex()) {
                worldPixel.findRGB(x, y)
            }
        }
        frameRate(30f)
        ellipseMode(RADIUS)
        background(0)
    }

    override fun draw() {
        drawGrid()
    }

    fun drawGrid() {
        val pixelOffsetX = cameraPosX % worldPixelSize
        val pixelOffsetY = cameraPosY % worldPixelSize

        val startIndexX: Int = if (cameraPosX / worldPixelSize < 0) 0 else cameraPosX / worldPixelSize
        val startIndexY: Int = if (cameraPosY / worldPixelSize < 0) 0 else cameraPosY / worldPixelSize

        //calculates latest worldPixel to draw or sets it to last one
        val endIndexX = if (width/worldPixelSize + startIndexX >= worldSizeX) worldSizeX - 1 else width/worldPixelSize + startIndexX
        val endIndexY = if (height/worldPixelSize + startIndexY + 1 >= worldSizeY) worldSizeY - 1 else height/worldPixelSize + startIndexY + 1

        for (i in startIndexX..endIndexX) {
            for (j in startIndexY..endIndexY) {
                val worldPixel = worldGrid[i][j]
                val pixelRGB = worldPixel.RGB
                fill(pixelRGB.first.toFloat(), pixelRGB.second.toFloat(), pixelRGB.third.toFloat())
                if (worldPixelSize < 5) {
                    stroke(pixelRGB.first.toFloat(), pixelRGB.second.toFloat(), pixelRGB.third.toFloat())
                } else {
                    stroke(0f)
                }
                val rectX = ((i - startIndexX) * worldPixelSize - pixelOffsetX).toFloat()
                val rectY = ((j - startIndexY) * worldPixelSize - pixelOffsetY).toFloat()

                rect(rectX, rectY, worldPixelSize.toFloat(), worldPixelSize.toFloat())
            }
        }
    }

    override fun mousePressed(event: MouseEvent) {
        clickedMouseX = mouseX
        clickedMouseY = mouseY
        savedCameraX = cameraPosX
        savedCameraY = cameraPosY
    }

    override fun mouseDragged(event: MouseEvent) {
        val diffX = clickedMouseX - mouseX
        val diffY = clickedMouseY - mouseY
        cameraPosX = savedCameraX + diffX
        cameraPosY = savedCameraY + diffY

        if (cameraPosX < 0) cameraPosX = 0
        if (cameraPosY < 0) cameraPosY = 0
        if (cameraPosX > worldSizeX * worldPixelSize - width) cameraPosX = worldSizeX * worldPixelSize - width
        if (cameraPosY > worldSizeY * worldPixelSize - height) cameraPosY = worldSizeY * worldPixelSize - height

        super.mouseDragged(event)
    }

    override fun keyPressed() {
        if (key.toInt() == PConstants.CODED && keyCode == PConstants.SHIFT) {
            shiftPressed = true
        } else {
            keyPressedMap[key] = true
        }
    }

    override fun keyReleased() {
        if (key.toInt() == PConstants.CODED && keyCode == PConstants.SHIFT) {
            shiftPressed = false
        } else {
            keyPressedMap[key] = false
        }
    }

    override fun mouseWheel(event: MouseEvent?) {
        if(event != null) {
            val value = event.count
            if (value > 0 && worldPixelSize > 1) {
                worldPixelSize--
            } else if (value < 0) {
                worldPixelSize++
            }
        }
    }
}