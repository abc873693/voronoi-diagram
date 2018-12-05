package voronoiDiagram.models

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.style

open class Line(var start: Point, var end: Point) {

    constructor (startX: Double, startY: Double, endX: Double, endY: Double) : this(
        Point(startX, startY),
        Point(endX, endY)
    )

    val getFxLine
        get() = Line(start.x, start.y, end.x, end.y)

    fun getConvexHullLine(): Line {
        val line = Line(start.x, start.y, end.x, end.y)
        line.style {
            stroke = Color.GREEN
        }
        return line
    }

    fun getDivideLine(): Line {
        val line = Line(start.x, start.y, end.x, end.y)
        line.style {
            stroke = Color.GOLD
        }
        return line
    }

    val out
        get() = "E ${start.x.toInt()} ${start.y.toInt()} ${end.x.toInt()} ${end.y.toInt()}"

    override fun toString(): String {
        return "{(${start.x.toInt()}, ${start.y.toInt()})(${end.x.toInt()}, ${end.y.toInt()})} "
    }
}