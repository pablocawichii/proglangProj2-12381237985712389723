package project2

import org.w3c.dom.*
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.*



val inp = document.createElement("input") as HTMLInputElement
val button = document.createElement("button") as HTMLButtonElement
val canvas = document.createElement("canvas") as HTMLCanvasElement

var derivation : HTMLDivElement = document.createElement("div") as HTMLDivElement


val context: CanvasRenderingContext2D
    get() {
        return canvas.getContext("2d") as CanvasRenderingContext2D
    }

class CanvasState(val canvas: HTMLCanvasElement) {
    var width = canvas.width
    var height = canvas.height
    val context = project2.context
    var changed = true
    val interval = 1000 / 30
    var xMax = 10;
    var yMax = 10;
    var graph = false

    init {

        window.setInterval({
            draw()
        }, interval)
    }

    fun isGraph() {
        graph = true
    }

    fun notGraph() {
        graph = true
    }

    fun drawGraph() {

        // Y - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("Y-Axis", 35.0, 35.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 50.0, 1.0, 450.0)

        // X - Axis
        context.strokeStyle = "#000000"
        context.lineWidth = 1.0
        context.strokeText("X-Axis", 515.0, 500.0)

        context.fillStyle = "#000000"
        context.fillRect(50.0, 500.0, 450.0, 1.0)

        if(yMax >= 20) {
            var k = 0
            for (i in 0 until yMax + 1) {
                if(i%(yMax/10) == 0){
                    context.strokeStyle = "#000000"
                    context.lineWidth = 1.0
                    context.strokeText(i.toString(), 25.0, 500.0 - ((450.0/yMax) * k))
                    k+= yMax/10;
                }
            }
        } else {
            var k = 0
            for (i in 0 until yMax + 1 ) {
                context.strokeStyle = "#000000"
                context.lineWidth = 1.0
                context.strokeText(i.toString(), 25.0, 500.0 - ((450.0/yMax) * k))
                k++
            }
        }

        if(xMax >= 20) {

            var k = 0
            for (i in 0 until xMax + 1){
                if(i%(xMax/10) == 0){
                    context.strokeStyle = "#000000"
                    context.lineWidth = 1.0
                    context.strokeText(i.toString(), 50.0 + ((450.0/xMax ) * k), 550.0)
                    k+= xMax/10;
                }
            }
        } else {
            var k = 0
            for (i in 0 until xMax + 1){
                context.strokeStyle = "#000000"
                context.lineWidth = 1.0
                context.strokeText(i.toString(), 50.0 + ((450.0/xMax) * k), 550.0)
                k++
            }
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

        clear()

        // Add drawings
    }
}

var lineString = ""

fun processInput(tokens: Array<String>) : Boolean {
// MUST ADD OUTPUTS FOR THESE

    derivation.innerHTML = "Program<br>"

    if(!tokens[0].matches("start")) {
        return generateError(0, "Program Does not Begin With 'start'", tokens)
    }

    if(!tokens[tokens.size-1].matches("stop")) {

        return generateError(tokens.size-1, "Program Does not End With 'stop'", tokens)
    }

    derivation.innerHTML += "Program -&gt; start &lt;plot_data&gt; stop<br>"

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

            var coord1 = tokens[posIndex+1]
            var coord2 = tokens[posIndex+3]

            if(tokens[posIndex].matches("bar")) {
                lineString = lineString.replace("&lt;plot&gt;", "bar &lt;x&gt;&lt;y&gt;,&lt;y&gt;")
                generateLine(lineString)

                if(!checkCoord(coord1, posIndex, tokens)){
                    return false;
                }

                if(coord2.length != 1) {
                    generateError(posIndex, "Coord Y is not correct length", tokens)
                    return false;
                }
                if(!checkY(coord2[0])){
                    generateError(posIndex, "Coord Y is not valid", tokens)
                    return false;
                }
                lineString = lineString.replaceFirst("&lt;y&gt;", coord2[0].toString())
                generateLine(lineString)


            } else {
                lineString = lineString.replace("&lt;plot&gt;", "edge &lt;x&gt;&lt;y&gt;,&lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                // Process Edge
                if(!checkCoord(coord1, posIndex, tokens)){
                    return false;
                }
                if(!checkCoord(coord2, posIndex, tokens)){
                    return false;
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


            var coord = tokens[posIndex+1]
            if(tokens[posIndex].matches("fill")){
                lineString = lineString.replace("&lt;plot&gt;", "fill &lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                //Process Fill
                if(!checkCoord(coord, posIndex, tokens)){
                    return false;
                }

            } else {
                lineString = lineString.replace("&lt;plot&gt;", "axis &lt;x&gt;&lt;y&gt;")
                generateLine(lineString)
                if(!checkCoord(coord, posIndex, tokens)){
                    return false;
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
        return false;
    }
    if(!checkX(str[0])){
        generateError(posIndex, "Coord X is not valid", tokens)
        return false;
    }
    lineString = lineString.replaceFirst("&lt;x&gt;", str[0].toString())
    generateLine(lineString)

    if(!checkY(str[1])){
        generateError(posIndex, "Coord Y is not valid", tokens)
        return false;
    }
    lineString = lineString.replaceFirst("&lt;y&gt;", str[1].toString())
    generateLine(lineString)

    return true;
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
    var outputStr = "Program -> $str <br>"

    derivation.innerHTML += outputStr

    (document.getElementById("derivation") as HTMLDivElement).innerHTML = derivation.innerHTML

}

fun generateError(errorPos: Int, errorMessage: String, tokens: Array<String>) : Boolean {
//    start bar a3,5; edge a3,b5 stop
    var errorStr = ""
    var i = 0
    while (!(errorPos+i > tokens.size - 1 || tokens[errorPos+i].matches(";") )){
        errorStr += tokens[errorPos+i] + " "
        i++
    }
    generateLine("<span style='color:red;'>" + errorMessage + "</span>")
    generateLine("<span style='color:red;'>" + errorStr + "</span>")
    return false;
}

fun parseString(str: String) : Array<String>  {
    
    return str
            .split( Regex("((?<=[;, ])|(?=[;, ]))") )
            .filterNot { it.matches(" ") }
            .toTypedArray()

}

class canvasTextItem(val str: String, val x: Double, val y: Double, val state: CanvasState,val parent: canvasTextItem?) {
    val textSize = 1;
    val color = "#FFF";
    val lineColor = "#000"

    fun draw(state: CanvasState) {
        val  context = state.context
        context.save()
        context.strokeStyle = color
        context.strokeText(str, x, y)
        context.strokeStyle = lineColor
        context.beginPath()
        context.moveTo(x,y)
        if(parent != null) {
            context.lineTo(parent.x, parent.y)
        }

        context.stroke()
        context.restore()

    }


}

fun parseTree(tokens: Array<String>) {
//    val canvasTree = document.createElement("canvas") as HTMLCanvasElement
//
    val parseTreeDiv = document.getElementById("parseTree") as HTMLDivElement
//    val contextTree = canvasTree.getContext("2d") as CanvasRenderingContext2D
//    contextTree.canvas.width = 600 // window.innerWidth.toInt()
//    contextTree.canvas.height = 600 // window.innerHeight.toInt()
    canvas.hidden = false;
    parseTreeDiv!!.appendChild(canvas)

    val canvasState = CanvasState(canvas);


    var htmlString : Array<Array<String>> = Array(1) { i -> Array(1) { i -> "Program" } }
//            "<h2>Parse Tree</h2><table style='width: 100;text-align: center'>"


    // high scope x and y,
    // x will be calculated dynamicall
    // y will slowly fall
    // First find amount of statements based on ;
    // only need a single array
    // Array to hold <plot>
    // Loop through and find if bin, edge, ect.
    // create and place into canvas, do entire segment at bin
    //

    console.log(htmlString)

    inner@ for (i in 1..tokens.size - 1) {
        if (tokens[i].matches(";")) {
//            htmlString += "<tr><td colspan=auto>plot</td><td colspan=auto>;</td><td colspan=auto>plot_data</td></tr>"
        }
    }

//        htmlString += "<tr><p></p></tr>"

    var posIndex = 1
    while(posIndex < tokens.size - 1) {


        if (tokens[posIndex].matches("bar") || tokens[posIndex].matches("edge")) {

            var coord1 = tokens[posIndex + 1]
            var coord2 = tokens[posIndex + 3]

            if (tokens[posIndex].matches("bar")) {

            } else {

            }

            posIndex += 5
        } else if (tokens[posIndex].matches("axis") || tokens[posIndex].matches("fill")) {

            var coord = tokens[posIndex + 1]
            if (tokens[posIndex].matches("fill")) {

            } else {

                // Process Axis
            }

            posIndex += 3
        }
    }
    console.log("Asdf")
//    parseTreeDiv.innerHTML = canvas.outerHTML // htmlString + "</table>"
}

fun main(args: Array<String>) {
    window.onload = {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = 600 // window.innerWidth.toInt()
        context.canvas.height = 600 // window.innerHeight.toInt()
        context.canvas.hidden = true;
        document.body!!.appendChild(canvas)

        inp.placeholder = "Enter Code Here"
        inp.type = "String"
        document.body!!.appendChild(inp)

        button.innerText = "Input"
        document.body!!.appendChild(button)

        val parseTreeDiv = document.getElementById("parseTree") as HTMLDivElement

        val canvasState = CanvasState(canvas);

        (document.getElementById("derivation") as HTMLDivElement).innerHTML = "Derivation"
//        context.beginPath()
//        context.lineWidth = 5.0
//        context.moveTo(position.x, position.y)
//        context.lineTo(Kotlin.centre.x, Kotlin.centre.y)
//        context.strokeStyle = "#FFF"
//        context.stroke()
//        context.closePath()

        button.addEventListener("click", {
            val tokens = parseString(inp.value);
            console.log(tokens);
            if (!processInput(tokens)) {
                parseTreeDiv.innerHTML = "<p> Parse Tree could not be created </p>"
                canvas.hidden = true;
            } else {
                parseTree(tokens);
                canvas.hidden = false;

            }

        })

        console.log(parseString("This is a string; XD"))
    }
}