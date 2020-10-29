package project2

import org.khronos.webgl.*
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window

val inp = document.createElement("input") as HTMLInputElement
val button = document.createElement("button") as HTMLButtonElement
val canvas = document.createElement("canvas") as HTMLCanvasElement
val modalCont = document.createElement("div") as HTMLDivElement
val modal = document.createElement("div") as HTMLDivElement
val modalDiv = document.createElement("div") as HTMLDivElement

var tokens = Array(0) { i -> ""}
var flowState = 0
var derivationError = false
var parsedItems = MutableList<CanvasItem>(0) { i -> CanvasBarItem("  ", "  ") }
var canvasState:CanvasState = CanvasState(canvas)


val context: CanvasRenderingContext2D
    get() {
        return canvas.getContext("2d") as CanvasRenderingContext2D
    }

class Pos (val x: Double, val y: Double) {}

class CanvasState(val canvas: HTMLCanvasElement) {
    var width = canvas.width
    var height = this.canvas.height
    val context = project2.context
    var changed = true
    val interval = 1000 / 30
    var xMax = 9
    var yMax = 9
    var graph = false
    var items = mutableListOf<CanvasItem>()


    init {
        window.setInterval({
            draw()
        }, interval)
    }

    fun setAxis(x: Int, y: Int){
        xMax = x
        yMax = y
    }

    fun addItem(item: CanvasItem) {
        items.add(item)
        changed = true
    }

    fun addItems(newItems: MutableList<CanvasItem>) {
        for(i in newItems) {
            addItem(i)
        }

        changed = true
    }

    fun emptyItems() {
        while(items.lastOrNull() != null) {
            items.removeAt(0)
        }
        clear()
        changed = true
    }

    fun isGraph() {
        graph = true
        changed = true
    }

    fun notGraph() {
        graph = false
        changed = true
    }

    fun drawGraph() {

        // Y - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("Y-Axis", 35.0, 35.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 50.0, 1.0, 450.0)

        context.fillStyle = "#000000"
        context.fillRect(500.0, 50.0, 1.0, 450.0)


        // X - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("X-Axis", 515.0, 500.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 500.0, 450.0, 1.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 50.0, 450.0, 1.0)


        var k = 0
        for (i in 0 until yMax + 1 ) {
            context.strokeStyle = "#000000"
            context.lineWidth = 1.0
            context.strokeText(i.toString(), 25.0, 500.0 - ((450.0/yMax) * k))
            k++
        }


        k = 0
        for (i in 0 until xMax + 1){
            context.strokeStyle = "#000000"
            context.lineWidth = 1.0
            context.strokeText((97 + i).toChar().toString(), 70.0 + ((410.0/xMax) * k), 550.0)
            k++
        }
    }

    fun clear() {
        context.fillStyle = "#D0D0D0"
        context.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
        context.strokeStyle = "#000000"
        context.lineWidth = 4.0
        context.strokeRect(0.0, 0.0, width.toDouble(), height.toDouble())
        if(graph)
            drawGraph()
    }

    fun draw() {
        if (!changed) return

        changed = false

        // Add drawings
        for(item in items) {
            item.draw(this)
        }
    }

}

var lineString = ""
fun processInput(tokens: Array<String>) : Boolean {
    if(tokens[0].matches("STOP")) {
        window.close()
    }

    modal.innerHTML = "Program<br>"

    if(!tokens[0].matches("start")) {
        return generateError(0, "Program Does not Begin With 'start'", tokens)
    }

    if(!tokens[tokens.size-1].matches("stop")) {
        return generateError(tokens.size-1, "Program Does not End With 'stop'", tokens)
    }

    modal.innerHTML += "Program -&gt; start &lt;plot_data&gt; stop<br>"

    lineString = "start &lt;plot_data&gt; stop<br>"
    var posIndex = 1
    while(posIndex < tokens.size){
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

        generateLine(lineString)


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

                // Process Axis
            }

            posIndex+= 3
        }
        else {
            return generateError(posIndex, "This statement does not exist", tokens)
        }

    }


    return true
}

fun checkCoord(str: String, posIndex: Int, tokens: Array<String>) : Boolean {
    if (str.length != 2) {
        generateError(posIndex, "Incorrect Syntax of &lt;x&gt;&lt;y&gt;", tokens)
        return false
    }
    if(!checkX(str[0])){
        generateError(posIndex, "Coord X is not valid", tokens)
        return false
    }
    lineString = lineString.replaceFirst("&lt;x&gt;", str[0].toString())
    generateLine(lineString)

    if(!checkY(str[1])){
        generateError(posIndex, "Coord Y is not valid", tokens)
        return false
    }
    lineString = lineString.replaceFirst("&lt;y&gt;", str[1].toString())
    generateLine(lineString)

    return true
}

fun checkX(char: Char) : Boolean {

    when (char) {
//        !'a','b','c','d','e','f','g','h','i','j' -> return false
        !in "abcdefghij" -> return false
    }

    return true
}

fun checkY(char: Char) : Boolean {

    when (char) {
        !in "0123456789" -> return false
    }

    return true
}

fun generateLine(str: String){
    val outputStr = "Program -> $str <br>"

    modal.innerHTML += outputStr
}

fun generateError(errorPos: Int, errorMessage: String, tokens: Array<String>) : Boolean {

    var errorStr = ""
    var i = 0
    while (!(errorPos+i > tokens.size - 1 || tokens[errorPos+i].matches(";") )){
        errorStr += tokens[errorPos+i] + " "
        i++
    }
    generateLine("<span style='color:red;'>" + errorMessage + "</span>")
    generateLine("<span style='color:red;'>" + errorStr + "</span>")
    derivationError = true
    return false
}

fun parseString(str: String) : Array<String>  {
    
    return str
            .split( Regex("((?<=[;, ])|(?=[;, ]))") )
            .filterNot { it.matches(" ") }
            .toTypedArray()

}

abstract class CanvasItem() {
    val color = "#000"
    val lineColor = "#FFF"

    abstract fun draw(state: CanvasState)

}
class CanvasTextItem(val str: String, var x: Double, val y: Double, val parent: CanvasTextItem?) : CanvasItem() {
    val textSize = 1.0

    override fun draw(state: CanvasState) {
        x -= 130

        val  context = state.context
        context.save()
        context.lineWidth = textSize
        context.strokeStyle = lineColor
        context.beginPath()
        context.moveTo(x,y)
        if(parent != null) {
            context.lineTo(parent.x, parent.y)
        }
        context.stroke()
        context.strokeStyle = color
        context.strokeText(str, x, y)
        context.restore()

    }


}
class CanvasBarItem(val coord1: String, val coord2: String) : CanvasItem() {
    var x: Double = 0.0
    var y: Double = 0.0
    var w: Int = 0
    init {
        x = (coord1[0] - 97).toDouble()
        y = (coord1[1]).toString().toDouble()
        w= (coord2).toInt()
    }
    override fun draw(state: CanvasState) {
        val  context = state.context
        context.save()
        context.strokeStyle = color
        context.lineWidth = 1.0
        var height = ((450.0/state.yMax) * y)
        context.strokeRect(50+((400.0/state.xMax+1) * x),(500-height),((450.0/(state.xMax+1)) * w).toDouble(), height)
    }

}
class CanvasEdgeItem(val coord1: String, val coord2: String) : CanvasItem() {
    var x: Double = (coord1[0] - 97).toDouble()
    var y: Double = (coord1[1]).toString().toDouble()
    var x2: Double = (coord2[0] - 97).toInt().toDouble()
    var y2: Double = (coord2[1]).toString().toDouble()


    override fun draw(state: CanvasState) {
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
class CanvasFillItem(val coord: String) : CanvasItem() {
    var x: Double = 0.0
    var y: Double = 0.0
    init {
        x = (coord[0] - 97).toDouble()
        y = (coord[1]).toString().toDouble()
    }


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

    override fun draw(state: CanvasState) {
        val context = state.context
        val width = context.canvas.width
        var dstImg = context.getImageData(0.0, 0.0, canvas.width * 1.0, canvas.height * 1.0)
        var dstData = dstImg.data

        var startCol = Uint8ClampedArray(4).unsafeCast<Uint16Array>()
        startCol[0] = 208
        startCol[1] = 208
        startCol[2] = 208
        startCol[3] = 255

        var startColor = startCol.unsafeCast<Uint8ClampedArray>()

        var fillColor = Uint8ClampedArray(4).unsafeCast<Uint16Array>()
        fillColor[0] = 255
        fillColor[1] = 255
        fillColor[2] = 255
        fillColor[3] = 255

        var todo = MutableList<Pos>(1) { i -> Pos(50+((400/state.xMax+1) * x), 500 - ((450/state.yMax) * y)) }

        while (todo.size != 0) {
            var pos = todo.removeLast()
            var x = pos.x
            var y = pos.y
            var currentPos = getPixelPos(x, y)


            while (y-- >= 0 && matchStartColor(dstData, currentPos, startColor)) {
                currentPos -= width * 4
            }


            currentPos += width * 4
            ++y
            var reachLeft = false
            var reachRight = false

            while ((y++ < canvas.height - 1) && matchStartColor(dstData, currentPos, startColor)) {
                var dstTemp = dstData
                dstData = colorPixel(dstData, currentPos, fillColor)

                if (x > 0) {
                    if (matchStartColor(dstData, currentPos - 4, startColor)) {
                        if (!reachLeft) {
                            todo.add(Pos(x - 1, y))
                            reachLeft = true
                        }
                    } else if (reachLeft) {
                        reachLeft = false
                    }
                }
                if (x < canvas.width - 1) {
                    if (matchStartColor(dstData, currentPos + 4, startColor)) {
                        if (!reachRight) {
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

        context.putImageData(dstImg, 0.0, 0.0)


    }
}

fun parseTree(tokens: Array<String>) {



    val width = canvasState.width.toDouble()
    var y = 40.0
    var semi = 0

    for (item in parsedItems.asReversed()){
        parsedItems.remove(item)
    }

    var arrOfPlot = MutableList<CanvasTextItem>(1) { i -> CanvasTextItem("Program", width/2, y, null) }
    y += 20

    arrOfPlot.add(CanvasTextItem("start", (width/2)-100, y, arrOfPlot[0]))
    arrOfPlot.add(CanvasTextItem("<plot_data>", width/2, y, arrOfPlot[0]))
    arrOfPlot.add(CanvasTextItem("stop", (width/2)+100, y, arrOfPlot[0]))
    y += 20


    for (i in 0 until tokens.size - 1){
        if (tokens[i].matches(";")){
            inner@ for (k in arrOfPlot.asReversed()){
                if(k.str.matches("<plot_data>")){
                    arrOfPlot.add(CanvasTextItem("<plot>", (k.x)-100, y, k))
                    arrOfPlot.add(CanvasTextItem(";", (k.x), y, k))
                    arrOfPlot.add(CanvasTextItem("<plot_data>", (k.x)+100, y, k))
                    y+= 20
                    semi += 2
                    break@inner
                }
            }
        }
    }

    for (k in arrOfPlot.asReversed()){
        if(k.str.matches("<plot_data>")){
            arrOfPlot.add(CanvasTextItem("<plot>", (k.x), y, k))
            y+= 20
            semi += 2
            break
        }
    }


    var plotPos = MutableList<CanvasTextItem>(0) { i -> arrOfPlot[0]}
    for (item in arrOfPlot) {
        if(item.str.matches("<plot>")){
            plotPos.add(item)
        }
    }

    var posIndex = 1
    var counter = 0
    while(posIndex < tokens.size - 1) {
        var plot : CanvasTextItem = plotPos[counter]

        if (tokens[posIndex].matches("bar") || tokens[posIndex].matches("edge")) {

            var coord1 = tokens[posIndex + 1]
            var coord2 = tokens[posIndex + 3]

            if (tokens[posIndex].matches("bar")) {
                arrOfPlot.add(CanvasTextItem("bar", (plot!!.x)-50, y, plot))
                arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+20, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[0].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))

                parsedItems.add(CanvasBarItem(coord1,coord2))

            } else {
                arrOfPlot.add(CanvasTextItem("edge", (plot!!.x)-55, y, plot))
                arrOfPlot.add(CanvasTextItem("<x>", (plot.x)+20, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[0].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))
                arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+40, y, plot))
                arrOfPlot.add(CanvasTextItem(coord2[1].toString(),(plot.x)+40, y + 20,  arrOfPlot.last()))

                parsedItems.add(CanvasEdgeItem(coord1,coord2))
            }

            arrOfPlot.add(CanvasTextItem("<x>", (plot.x)-30, y, plot))
            arrOfPlot.add(CanvasTextItem(coord1[0].toString(),(plot.x)-30, y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem("<y>", (plot.x)-10, y, plot))
            arrOfPlot.add(CanvasTextItem(coord1[1].toString(),(plot.x)-10, y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem(",", (plot.x)+10, y, plot))


            y+= 20

            posIndex += 5
        } else if (tokens[posIndex].matches("axis") || tokens[posIndex].matches("fill")) {

            var coord = tokens[posIndex + 1]
            if (tokens[posIndex].matches("fill")) {
                arrOfPlot.add(CanvasTextItem("fill", (plot!!.x)-20, y, plot))
                parsedItems.add(CanvasFillItem(coord))
            } else {
                arrOfPlot.add(CanvasTextItem("axis", (plot!!.x)-20, y, plot))

                canvasState.apply {
                    setAxis(coord[0].toInt() - 97, coord[1].toString().toInt())
                }
            }
            arrOfPlot.add(CanvasTextItem("<x>", (plot.x), y, plot))
            arrOfPlot.add(CanvasTextItem(coord[0].toString(),(plot.x), y + 20,  arrOfPlot.last()))
            arrOfPlot.add(CanvasTextItem("<y>", (plot.x)+20, y, plot))
            arrOfPlot.add(CanvasTextItem(coord[1].toString(),(plot.x)+20, y + 20,  arrOfPlot.last()))

            y+= 20


            posIndex += 3
        }

        counter++
    }


    for (item in arrOfPlot){
        canvasState.apply {
            addItem(item)
        }
    }

}

fun continueFlow(canvasState: CanvasState){
    when(flowState) {
        // 0 is browser state
        0 -> {
            tokens = parseString(inp.value)
            canvas.hidden = true

            derivationError = false
            processInput(tokens)

            modalDiv.style.display = "block"
            modal.style.display = "block"

            flowState++
        }
        // 1 is derivation
        1 -> {
            modal.style.display = "none"
            if(!derivationError){
                canvas.hidden = false
                canvasState.apply {
                    emptyItems()
                    notGraph()
                    clear()
                }
                parseTree(tokens)
                flowState++
            } else {
                modalDiv.style.display = "none"
                flowState = 0
            }
        }
        // 2 is parse tree
        2 -> {
            canvasState.apply {
                emptyItems()
                isGraph()
                clear()
                addItems(parsedItems)
                draw()
            }
            flowState++
        }
        // 3 is graphic output
        3 -> {
            modalDiv.style.display = "none"
            flowState = 0
        }
    }
}

// Example code: start bar a3,5; edge a3,b5 ; fill b5 ; axis e9 stop
fun main(args: Array<String>) {
    window.onload = {
        modalDiv.style.display = "none"
        modalDiv.classList.add("modal")
        document.body!!.appendChild(modalDiv)

        val contButton = document.createElement("button") as HTMLButtonElement
        contButton.innerText = "Continue"
        modalDiv!!.appendChild(contButton)

        modalCont.classList.add("modal-content")
        modalCont!!.appendChild(modal)
        modalDiv!!.appendChild(modalCont)

        context.canvas.width = 600 // window.innerWidth.toInt()
        context.canvas.height = 600 // window.innerHeight.toInt()
        modalCont!!.appendChild(canvas)

        inp.placeholder = "Enter Code Here"
        inp.type = "String"
        document.getElementById("InputGoesHere")!!.appendChild(inp)

        button.innerText = "Input"
        document.getElementById("ButtonGoesHere")!!.appendChild(button)

        canvasState = CanvasState(canvas)

        contButton.addEventListener("click", {
            continueFlow(canvasState)
        })

        button.addEventListener("click", {
            continueFlow(canvasState)
        })

    }
}