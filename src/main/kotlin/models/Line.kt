package voronoiDiagram.models

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.style

open class Line(var startX: Double, var startY: Double, var endX: Double, var endY: Double) {

    val start = Point(startX, startY)
    val end = Point(endX, endY)

    constructor (a: Point, b: Point) : this(a.x, a.y, b.x, b.y)

    val getFxLine
        get() = Line(startX, startY, endX, endY)

    fun getConvexHullLine(): Line {
        val line = Line(startX, startY, endX, endY)
        line.style {
            stroke = Color.GREEN
        }
        return line
    }

    fun getDivideLine(): Line {
        val line = Line(startX, startY, endX, endY)
        line.style {
            stroke = Color.GOLD
        }
        return line
    }

    val out
        get() = "E ${startX.toInt()} ${startY.toInt()} ${endX.toInt()} ${endY.toInt()}"

    override fun toString(): String {
        return "{(${startX.toInt()}, ${startY.toInt()})(${endX.toInt()}, ${endY.toInt()})} "
    }
}