package project2

import org.khronos.webgl.*
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window

// Create and hold the necessary elements
val inp = document.createElement("input") as HTMLInputElement
val button = document.createElement("button") as HTMLButtonElement
val canvas = document.createElement("canvas") as HTMLCanvasElement
val modalCont = document.createElement("div") as HTMLDivElement
val modal = document.createElement("div") as HTMLDivElement
val modalDiv = document.createElement("div") as HTMLDivElement

// Global Variables we will need
var tokens = Array(0) { i -> ""}
var flowState = 0
var derivationError = false
var parsedItems = MutableList<CanvasItem>(0) { i -> CanvasBarItem("  ", "  ") }
var canvasState:CanvasState = CanvasState(canvas)

// The Global Context Variable
val context: CanvasRenderingContext2D
    get() {
        return canvas.getContext("2d") as CanvasRenderingContext2D
    }

// A Position class, to hold x and y
class Pos (val x: Double, val y: Double) {}

// Holds the canvas, and allows us to perform functions
// on the canvas
class CanvasState(val canvas: HTMLCanvasElement) {
    // Variables
    var width = canvas.width
    var height = this.canvas.height
    val context = project2.context
    var xMax = 9
    var yMax = 9
    var graph = false
    var items = mutableListOf<CanvasItem>()

    // Function to set Axis
    fun setAxis(x: Int, y: Int){
        xMax = x
        yMax = y
    }

    // Adds an item to the canvas
    fun addItem(item: CanvasItem) {
        items.add(item)
    }

    // Add multiple items at the same time
    fun addItems(newItems: MutableList<CanvasItem>) {
        for(i in newItems) {
            items.add(i)
        }
    }

    // Remove all items currently on the canvas
    fun emptyItems() {
        // Remove items
        while(items.lastOrNull() != null) {
            items.removeAt(0)
        }
        // Redraw canvas
        clear()
    }

    // Set graph to true
    fun isGraph() {
        graph = true
    }

    // Set graph to false
    fun notGraph() {
        graph = false
    }

    // Draws the graph
    fun drawGraph() {

        // Y - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("Y-Axis", 35.0, 35.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 50.0, 1.0, 450.0)

        context.fillStyle = "#000000"
        context.fillRect(500.0, 50.0, 1.0, 450.0)

        for (i in 0 until yMax + 1 ) {
            context.strokeStyle = "#000000"
            context.lineWidth = 1.0
            context.strokeText(i.toString(), 25.0, 500.0 - ((450.0/yMax) * i))
        }

        // X - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("X-Axis", 515.0, 500.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 500.0, 450.0, 1.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 50.0, 450.0, 1.0)

        for (i in 0 until xMax + 1){
            context.strokeStyle = "#000000"
            context.lineWidth = 1.0
            context.strokeText((97 + i).toChar().toString(), 70.0 + ((410.0/xMax) * i), 550.0)
        }
    }

    // Draws Backround
    fun clear() {
        context.fillStyle = "#D0D0D0"
        context.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
        context.strokeStyle = "#000000"
        context.lineWidth = 4.0
        context.strokeRect(0.0, 0.0, width.toDouble(), height.toDouble())
        if(graph)
            drawGraph()
    }

    //  Draws items
    fun draw() {
        // Add drawings
        for(item in items) {
            item.draw(this)
        }
    }

}

// Checks Grammar and Outputs partial Derivation
var lineString = ""
fun processInput(tokens: Array<String>) : Boolean {
    // Look for STOP to end program
    if(tokens[0].matches("STOP")) {
        window.close()
    }

    modal.innerHTML = "Graph<br>"

    if(!tokens[0].matches("start")) {
        return generateError(0, "Program Does not Begin With 'start'", tokens)
    }

    if(!tokens[tokens.size-1].matches("stop")) {
        return generateError(tokens.size-1, "Program Does not End With 'stop'", tokens)
    }

    modal.innerHTML += "Graph -&gt; start &lt;plot_data&gt; stop<br>"

    // Check Grammar and Provide Derivation
    lineString = "start &lt;plot_data&gt; stop<br>"
    var posIndex = 1
    while(posIndex < tokens.size){
        // If not final statement, put plot data
        var last = true
        inner@ for(i in posIndex..tokens.size-1) {
            if (tokens[i].matches(";")) {
                last = false
                break@inner
            }
        }

        if(last){
            lineString = lineString.replace("&lt;plot_data&gt;", "&lt;plot&gt;")
        } else {
            lineString = lineString.replace("&lt;plot_data&gt;", "&lt;plot&gt; ; &lt;plot_data&gt;")
        }
        generateLine(lineString) // Output Derivation

        // Search for Errors
        // generateLine provides Derivation
        if(tokens[posIndex].matches("bar") || tokens[posIndex].matches("edge")){
            if(posIndex+4 > tokens.size){
                return generateError(posIndex, "Statement has not enough arguments.", tokens)
            }
            for (i in 1 until 4) {
                if(tokens[posIndex+i].matches(";") || tokens[posIndex+i].matches("stop") )
                    return generateError(posIndex, "Statement has not enough arguments.", tokens)
            }

            if(!(tokens[posIndex+4].matches(";") || tokens[posIndex+4].matches("stop")))
                return generateError(posIndex, "Statement has too many arguments.", tokens)

            if(!tokens[posIndex+2].matches(","))
                return generateError(posIndex, "Incorrect Syntax.", tokens)

            val coord1 = tokens[posIndex+1]
            val coord2 = tokens[posIndex+3]

            if(tokens[posIndex].matches("bar")) {
                lineString = lineString.replace("&lt;plot&gt;", "bar &lt;x&gt;&lt;y&gt;,&lt;y&gt;")
                generateLine(lineString)

                if(!checkCoord(coord1, posIndex, tokens)){
                    return false
                }

                if(coord2.length != 1) {
                    generateError(posIndex, "Coord Y is not correct length", tokens)
                    return false
                }
                if(!checkY(coord2[0])){
                    generateError(posIndex, "Coord Y is not valid", tokens)
                    return false
                }
                lineString = lineString.replaceFirst("&lt;y&gt;", coord2[0].toString())
                generateLine(lineString)


            } else {
                lineString = lineString.replace("&lt;plot&gt;", "edge &lt;x&gt;&lt;y&gt;,&lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                // Process Edge
                if(!checkCoord(coord1, posIndex, tokens)){
                    return false
                }
                if(!checkCoord(coord2, posIndex, tokens)){
                    return false
                }
            }

            posIndex+= 5
        }
        else if(tokens[posIndex].matches("axis") || tokens[posIndex].matches("fill")){
            if(posIndex+2 > tokens.size){
                return generateError(posIndex, "Statement has not enough arguments.", tokens)
            }

            if(tokens[posIndex+1].matches(";") || tokens[posIndex+1].matches("stop") )
                return generateError(posIndex, "Statement has not enough arguments.", tokens)


            if(!(tokens[posIndex+2].matches(";") || tokens[posIndex+2].matches("stop")))
                return generateError(posIndex, "Statement has too many arguments.", tokens)


            val coord = tokens[posIndex+1]
            if(tokens[posIndex].matches("fill")){
                lineString = lineString.replace("&lt;plot&gt;", "fill &lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                //Process Fill
                if(!checkCoord(coord, posIndex, tokens)){
                    return false
                }

            } else {
                lineString = lineString.replace("&lt;plot&gt;", "axis &lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                if(!checkCoord(coord, posIndex, tokens)){
                    return false
                }
            }

            posIndex+= 3
        }
        else {
            return generateError(posIndex, "This statement does not exist", tokens)
        }
    }
    return true
}

// Checks if coords are in format <x><y>
fun checkCoord(str: String, posIndex: Int, tokens: Array<String>) : Boolean {
    // xy
    if (str.length != 2) {
        generateError(posIndex, "Incorrect Syntax of &lt;x&gt;&lt;y&gt;", tokens)
        return false
    }
    // Checks X
    if(!checkX(str[0])){
        generateError(posIndex, "Coord X is not valid", tokens)
        return false
    }
    lineString = lineString.replaceFirst("&lt;x&gt;", str[0].toString())
    generateLine(lineString)

    // Checks Y
    if(!checkY(str[1])){
        generateError(posIndex, "Coord Y is not valid", tokens)
        return false
    }
    lineString = lineString.replaceFirst("&lt;y&gt;", str[1].toString())
    generateLine(lineString)

    return true
}

// Checks if x is valid
fun checkX(char: Char) : Boolean {
    when (char) {
        !in "abcdefghij" -> return false
    }
    return true
}

// Checks if y is valid
fun checkY(char: Char) : Boolean {
    when (char) {
        !in "0123456789" -> return false
    }
    return true
}

// Adds line to derivation output
fun generateLine(str: String){
    val outputStr = "Graph -> $str <br>"

    modal.innerHTML += outputStr
}

// Adds error to derivation output
fun generateError(errorPos: Int, errorMessage: String, tokens: Array<String>) : Boolean {

    var errorStr = ""
    var i = 0
    // Recreates the error statement
    while (!(errorPos+i > tokens.size - 1 || tokens[errorPos+i].matches(";") )){
        errorStr += tokens[errorPos+i] + " "
        i++
    }
    generateLine("<span style='color:red;'>" + errorMessage + "</span>")
    generateLine("<span style='color:red;'>" + errorStr + "</span>")
    // Flag the error
    derivationError = true
    return false
}

// Turns Input string into tokens
fun parseString(str: String) : Array<String>  {

    return str
            .split( Regex("((?<=[;, ])|(?=[;, ]))") )
            .filterNot { it.matches(" ") }
            .toTypedArray()

}

// Canvas Items with their drawn implementations
// and custom variables
abstract class CanvasItem() {
    val color = "#000"
    val lineColor = "#FFF"

    abstract fun draw(state: CanvasState)
}
// Text Item, to hold strings, and point to previous. For Parse Tree
class CanvasTextItem(val str: String, var x: Double, val y: Double, val parent: CanvasTextItem?) : CanvasItem() {
    val textSize = 1.0

    override fun draw(state: CanvasState) {
        x -= 130 // Displace to the left

        val  context = state.context
        context.save()
        context.lineWidth = textSize
        context.strokeStyle = lineColor
        context.beginPath()
        context.moveTo(x,y)
        // For top level Item
        if(parent != null) {
            context.lineTo(parent.x, parent.y)
        }
        context.stroke()
        context.strokeStyle = color
        context.strokeText(str, x, y)
        context.restore()

    }
}
// Bar Item
class CanvasBarItem(val coord1: String, val coord2: String) : CanvasItem() {
    // Variables
    var x: Double = (coord1[0] - 97).toDouble()
    var y: Double = (coord1[1]).toString().toDouble()
    var w: Int = (coord2).toInt()

    override fun draw(state: CanvasState) {
        // Draw Rect
        val  context = state.context
        context.save()
        context.strokeStyle = color
        context.lineWidth = 1.0
        var height = ((450.0/state.yMax) * y)
        context.strokeRect(50+((400.0/state.xMax+1) * x),(500-height),((450.0/(state.xMax+1)) * w).toDouble(), height)
    }
}
// Edge Item / Line
class CanvasEdgeItem(val coord1: String, val coord2: String) : CanvasItem() {
    // Variables
    var x: Double = (coord1[0] - 97).toDouble()
    var y: Double = (coord1[1]).toString().toDouble()
    var x2: Double = (coord2[0] - 97).toInt().toDouble()
    var y2: Double = (coord2[1]).toString().toDouble()

    override fun draw(state: CanvasState) {
        // Draw Line
        val  context = state.context
        context.save()
        context.beginPath()
        context.strokeStyle = "000000"
        context.lineWidth = 1.0
        context.moveTo(50+((400.0/state.xMax+1) * x), 500 - ((450.0/state.yMax) * y))
        context.lineTo(50+((400.0/state.xMax+1) * x2), 500 - ((450.0/state.yMax) * y2))
        context.stroke()
        context.closePath()
        context.restore()
    }
}
// Does Flood Fill
class CanvasFillItem(val coord: String) : CanvasItem() {
    // Variables
    var x: Double = (coord[0] - 97).toDouble()
    var y: Double = (coord[1]).toString().toDouble()

    // Helper Functions
    fun getPixelPos(x: Double, y: Double) : Int {
        return ((y * canvas.width + x) * 4).toInt()
    }
    fun matchStartColor(data: Uint8ClampedArray, pos: Int, startColor: Uint8ClampedArray): Boolean {

        return (
                data.get(pos + 0) == startColor.get(0) &&
                        data.get(pos + 1) == startColor.get(1) &&
                        data.get(pos + 2) == startColor.get(2) &&
                        data.get(pos + 3) == startColor.get(3)
                )
    }
    fun colorPixel(data: Uint8ClampedArray, pos: Int, color: Uint16Array): Uint8ClampedArray {
        data.set(pos, color[0].toByte())
        data.set(pos+1, color[1].toByte())
        data.set(pos+2, color[2].toByte())
        data.set(pos+3, color[3].toByte())

        return data
    }

    // Performs Flood Fill
    override fun draw(state: CanvasState) {
        val context = state.context
        val width = context.canvas.width
        // Canvas as array
        var dstImg = context.getImageData(0.0, 0.0, canvas.width * 1.0, canvas.height * 1.0)
        var dstData = dstImg.data

        // Background Color
        var startCol = Uint8ClampedArray(4).unsafeCast<Uint16Array>()
        startCol[0] = 208
        startCol[1] = 208
        startCol[2] = 208
        startCol[3] = 255
        var startColor = startCol.unsafeCast<Uint8ClampedArray>()

        // Color to place
        var fillColor = Uint8ClampedArray(4).unsafeCast<Uint16Array>()
        fillColor[0] = 255
        fillColor[1] = 255
        fillColor[2] = 255
        fillColor[3] = 255

        // Loops to replace all functions that match start color
        var todo = MutableList<Pos>(1) { i -> Pos(50+((400/state.xMax+1) * x), 500 - ((450/state.yMax) * y)) }
        while (todo.size != 0) {
            var pos = todo.removeLast()
            var x = pos.x
            var y = pos.y
            var currentPos = getPixelPos(x, y)

            // Travels to Top
            while (y-- >= 0 && matchStartColor(dstData, currentPos, startColor)) {
                currentPos -= width * 4
            }

            currentPos += width * 4
            ++y
            var reachLeft = false
            var reachRight = false

            // Travels to Bottom
            while ((y++ < canvas.height - 1) && matchStartColor(dstData, currentPos, startColor)) {
                var dstTemp = dstData
                dstData = colorPixel(dstData, currentPos, fillColor)

                // Travels Left
                if (x > 0) {
                    if (matchStartColor(dstData, currentPos - 4, startColor)) {
                        if (!reachLeft) {
                            // Adds pixel at left
                            todo.add(Pos(x - 1, y))
                            reachLeft = true
                        }
                    } else if (reachLeft) {
                        reachLeft = false
                    }
                }
                // Travels Right
                if (x < canvas.width - 1) {
                    if (matchStartColor(dstData, currentPos + 4, startColor)) {
                        if (!reachRight) {
                            // Adds pixel at right
                            todo.add(Pos(x + 1, y))
                            reachRight = true
                        }
                    } else if (reachRight) {
                        reachRight = false
                    }
                }
                currentPos += width * 4
            }
        }

        // Replaces canvas with filled content
        context.putImageData(dstImg, 0.0, 0.0)
    }
}

// Creates Parse Tree and Graph
fun generateVisuals(tokens: Array<String>) {
    // Variables
    val width = canvasState.width.toDouble()
    var y = 40.0

    // Empty old graph
    while (parsedItems.isNotEmpty()){
        parsedItems.removeAt(0)
    }

    // List of Canvas Items for Parse Tree
    var arrOfPlot = MutableList<CanvasTextItem>(1) { i -> CanvasTextItem("Graph", width/2, y, null) }
    // Position in terms of height
    y += 20

    // Initialize Parse Tree Data
    arrOfPlot.add(CanvasTextItem("start", (width/2)-100, y, arrOfPlot[0]))
    arrOfPlot.add(CanvasTextItem("<plot_data>", width/2, y, arrOfPlot[0]))
    arrOfPlot.add(CanvasTextItem("stop", (width/2)+100, y, arrOfPlot[0]))
    y += 20

    // Adds plot_data s and plots
    for (i in 0 until tokens.size - 1){
        if (tokens[i].matches(";")){
            inner@ for (k in arrOfPlot.asReversed()){
                if(k.str.matches("<plot_data>")){
                    arrOfPlot.add(CanvasTextItem("<plot>", (k.x)-100, y, k))
                    arrOfPlot.add(CanvasTextItem(";", (k.x), y, k))
                    arrOfPlot.add(CanvasTextItem("<plot_data>", (k.x)+100, y, k))
                    y+= 20
                    break@inner
                }
            }
        }
    }

    // Find and replace final plot_data
    for (k in arrOfPlot.asReversed()){
        if(k.str.matches("<plot_data>")){
            arrOfPlot.add(CanvasTextItem("<plot>", (k.x), y, k))
            y+= 20
            break
        }
    }

    // List with positions of plots
    var plotPos = MutableList<CanvasTextItem>(0) { i -> arrOfPlot[0]}
    for (item in arrOfPlot) {
        if(item.str.matches("<plot>")){
            plotPos.add(item)
        }
    }

    // Adds statements to parse tree
    var posIndex = 1
    var counter = 0
    while(posIndex < tokens.size - 1) {
        // Parent for statement
        var plot : CanvasTextItem = plotPos[counter++]

        if (tokens[posIndex].matches("bar") || tokens[posIndex].matches("edge")) {

            // Both bar and edge have 2 coords
            var coord1 = tokens[posIndex + 1]
            var coord2 = tokens[posIndex + 3]

            if (tokens[posIndex].matches("bar")) {
                // Bar's second coord is only a y
                arrOfPlot.add(CanvasTextItem("bar", (plot!!.x)-50, y, plot))
                arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+20, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[0].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))

                // Add BarItem to canvas graph
                parsedItems.add(CanvasBarItem(coord1,coord2))
            } else {
                // Edge's second coord is xy
                arrOfPlot.add(CanvasTextItem("edge", (plot!!.x)-55, y, plot))
                arrOfPlot.add(CanvasTextItem("<x>", (plot.x)+20, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[0].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))
                arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+40, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[1].toString(),(plot.x)+40, y + 20,  arrOfPlot.last()))

                // Add EdgeItem to canvas graph
                parsedItems.add(CanvasEdgeItem(coord1,coord2))
            }

            // Both Edge and Bar's first coord is xy
            arrOfPlot.add(CanvasTextItem("<x>", (plot.x)-30, y, plot))
            arrOfPlot.add(CanvasTextItem(coord1[0].toString(),(plot.x)-30, y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem("<y>", (plot.x)-10, y, plot))
            arrOfPlot.add(CanvasTextItem(coord1[1].toString(),(plot.x)-10, y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem(",", (plot.x)+10, y, plot))

            // Continue to next statement
            y+= 20
            posIndex += 5
        } else if (tokens[posIndex].matches("axis") || tokens[posIndex].matches("fill")) {

            // Both axis and fill have 1 coord
            var coord = tokens[posIndex + 1]

            if (tokens[posIndex].matches("fill")) {
                // Place fill text into parse tree
                arrOfPlot.add(CanvasTextItem("fill", (plot!!.x)-20, y, plot))
                // Place FillItem into graph
                parsedItems.add(CanvasFillItem(coord))
            } else {
                // Place axis text into parse tree
                arrOfPlot.add(CanvasTextItem("axis", (plot!!.x)-20, y, plot))
                // Change the axis of the canvas
                canvasState.apply {
                    setAxis(coord[0].toInt() - 97, coord[1].toString().toInt())
                }
            }

            // Both axis and fill have xy
            arrOfPlot.add(CanvasTextItem("<x>", (plot.x), y, plot))
            arrOfPlot.add(CanvasTextItem(coord[0].toString(),(plot.x), y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+20, y, plot))
            arrOfPlot.add(CanvasTextItem(coord[1].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))

            // Move to next Statement
            y+= 20
            posIndex += 3
        }
    }

    // Add parse tree items to canvas
    for (item in arrOfPlot){
        canvasState.apply {
            addItem(item)
        }
    }

}

// Acts as the flow of a standard console program
fun continueFlow(canvasState: CanvasState){
    when(flowState) {
        // 0 is at browser state
        // Parse Input, and process It
        0 -> {
            tokens = parseString(inp.value)
            canvas.hidden = true
            derivationError = false

            processInput(tokens)

            // Show Derivation Output
            modalDiv.style.display = "block"
            modal.style.display = "block"

            // Continue Flow
            flowState++
        }
        // 1 is at derivation output
        1 -> {
            // Hide Derivation Output
            modal.style.display = "none"
            // If Derivation Success
            if(!derivationError){
                // Show Parse Tree
                canvas.hidden = false
                canvasState.apply {
                    emptyItems()
                    notGraph()
                    clear()
                }
                generateVisuals(tokens)
                canvasState.apply {
                    draw()
                }
                // Continue Flow
                flowState++
            } else {
                // Derivation Fails, Reset
                modalDiv.style.display = "none"
                flowState = 0
            }
        }
        // 2 is at parse tree
        2 -> {
            // Show Graph
            canvasState.apply {
                emptyItems()
                isGraph()
                clear()
                addItems(parsedItems)
                draw()
            }

            // Continue Flow
            flowState++
        }
        // 3 is at graphic output
        3 -> {
            // Reset
            modalDiv.style.display = "none"
            flowState = 0
        }
    }
}

// Things to do when windows fully loads
fun main(args: Array<String>) {
    window.onload = {
        // Background Modal
        modalDiv.style.display = "none"
        modalDiv.classList.add("modal")
        document.body!!.appendChild(modalDiv)

        // Continue Button
        val contButton = document.createElement("button") as HTMLButtonElement
        contButton.innerText = "Continue"
        modalDiv!!.appendChild(contButton)

        // Visible Segment
        modalCont.classList.add("modal-content")
        modalCont!!.appendChild(modal)
        modalDiv!!.appendChild(modalCont)

        // Canvas
        context.canvas.width = 600 // window.innerWidth.toInt()
        context.canvas.height = 600 // window.innerHeight.toInt()
        modalCont!!.appendChild(canvas)

        // Text Input
        inp.placeholder = "Enter Code Here"
        inp.type = "String"
        document.getElementById("InputGoesHere")!!.appendChild(inp)

        // Start Button
        button.innerText = "Input"
        document.getElementById("ButtonGoesHere")!!.appendChild(button)

        canvasState = CanvasState(canvas)

        // Event Listeners
        contButton.addEventListener("click", {
            continueFlow(canvasState)
        })

        button.addEventListener("click", {
            continueFlow(canvasState)
        })

    }
}

