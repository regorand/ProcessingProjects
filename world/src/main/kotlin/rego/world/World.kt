package rego.world

import rego.engine.Camera
import processing.core.PApplet
import processing.core.PConstants
import processing.event.MouseEvent
import kotlin.math.roundToInt

class World: PApplet() {

    lateinit var worldGrid: List<List<WorldPixel>>

    private val keyPressedMap: MutableMap<Char, Boolean> = mutableMapOf()

    private var shiftPressed = false

    var clickedMouseX = 0
    var clickedMouseY = 0

    val worldSizeX = 100
    val worldSizeY = 100

    var worldPixelSize = 5

    val scrollScaling = 1.1

    // coordinates of camera in pixels
    var cameraPosX = 0
    var cameraPosY = 0

    var savedCameraX = 0
    var savedCameraY = 0

    var counter: Double = 0.0

    val points = mutableListOf<Double>()

    val perlinDelta = 15f

    var camera = Camera(worldPixelSize, worldSizeX, worldSizeY, width, height)

    override fun settings() {
        size(1080, 720)
    }

    override fun setup() {
        worldGrid = List(worldSizeX) { List(worldSizeY) { WorldPixel(this) } }
        var largestPixelValue = -1f
        val values = List(worldSizeX) { x -> List(worldSizeY) { y -> noise(x.toFloat() / worldSizeX * perlinDelta, y.toFloat() / worldSizeY * perlinDelta) * 255 } }

        val maxValue = values.mapNotNull { l -> l.max() }.max()!!
        val minValue = values.mapNotNull { l -> l.min() }.min()!!

        val minMaxDiff = maxValue - minValue

        println("max: $maxValue")
        println("min: $minValue")

        val mappedValues = values.map { l -> l.map { v -> ((v - minValue) / minMaxDiff) * 255 } }

        val maxValue2 = mappedValues.mapNotNull { l -> l.max() }.max() ?: 255
        val minValue2 = mappedValues.mapNotNull { l -> l.min() }.min() ?: 0

        println("max2: $maxValue2")
        println("min2: $minValue2")

        for ((x, yList) in worldGrid.iterator().withIndex()) {
            for ((y, worldPixel) in yList.iterator().withIndex()) {
                val value = noise(x.toFloat() / worldSizeX * perlinDelta, y.toFloat() / worldSizeY * perlinDelta) * 255
                if (value > largestPixelValue) largestPixelValue = value
                //worldPixel.setRGBWhite(value)


                worldPixel.setColor(mappedValues[x][y])
            }
        }


        println("finished world grid: $largestPixelValue")
        frameRate(30f)
        ellipseMode(RADIUS)
        background(0)
    }

    override fun draw() {
        stroke(0f)
        fill(0f)
        rect(0f, 0f, width.toFloat(), height.toFloat())
        drawGrid()
    }

    fun drawGrid() {
        val pixelOffsetX = cameraPosX % worldPixelSize
        val pixelOffsetY = cameraPosY % worldPixelSize

        val startIndexX: Int = if (cameraPosX / worldPixelSize < 0) 0 else (cameraPosX / worldPixelSize).toInt()
        val startIndexY: Int = if (cameraPosY / worldPixelSize < 0) 0 else (cameraPosY / worldPixelSize).toInt()

        //calculates latest worldPixel to draw or sets it to last one
        val endIndexX = if (width/worldPixelSize + startIndexX >= worldSizeX) worldSizeX - 1 else (width/worldPixelSize + startIndexX).toInt()
        val endIndexY = if (height/worldPixelSize + startIndexY + 1 >= worldSizeY) worldSizeY - 1 else (height/worldPixelSize + startIndexY + 1).toInt()

        for (i in startIndexX..endIndexX) {
            for (j in startIndexY..endIndexY) {
                val worldPixel = worldGrid[i][j]
                val pixelRGB = worldPixel.color
                fill(pixelRGB.first.toFloat(), pixelRGB.second.toFloat(), pixelRGB.third.toFloat())
                if (worldPixelSize < 500) {
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
        if (cameraPosX > worldSizeX * worldPixelSize - width) cameraPosX = (worldSizeX * worldPixelSize - width).toInt()
        if (cameraPosY > worldSizeY * worldPixelSize - height) cameraPosY = (worldSizeY * worldPixelSize - height).toInt()

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
        //TODO scale camera value with difference in worldPixelSize so scrolling makes more sense

        //TODO while fixing camera scaling get blackscreens until screen is moved
        if(event != null) {
            val value = event.count

            val hoveredWorldPixelX = mouseX / worldPixelSize + cameraPosX * worldPixelSize
            val hoveredWorldPixelY = mouseY / worldPixelSize + cameraPosY * worldPixelSize

            if (value > 0 && worldPixelSize > 1) {
                worldPixelSize /= scrollScaling
                if (worldPixelSize < 1) worldPixelSize = 1.0
            } else if (value < 0) {
                worldPixelSize *= scrollScaling
            }

            cameraPosX = (hoveredWorldPixelX * worldPixelSize - mouseX).roundToInt()
            cameraPosY = (hoveredWorldPixelY * worldPixelSize - mouseY).roundToInt()
        }
    }
}