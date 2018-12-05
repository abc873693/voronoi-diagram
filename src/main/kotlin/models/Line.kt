package voronoiDiagram.models

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.style

open class Line(var start: Point, var end: Point) {

    var color: Color = Color.BLACK

    constructor (startX: Double, startY: Double, endX: Double, endY: Double) : this(
        Point(startX, startY),
        Point(endX, endY)
    )

    fun getFxLine(): Line {
        val line = Line(start.x, start.y, end.x, end.y)
        line.style {
            stroke = color
        }
        return line
    }

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

    fun resetColor() {
        color = Color.BLACK
    }

    val out
        get() = "E ${start.x.toInt()} ${start.y.toInt()} ${end.x.toInt()} ${end.y.toInt()}"

    override fun toString(): String {
        return "{(${start.x.toInt()}, ${start.y.toInt()})(${end.x.toInt()}, ${end.y.toInt()})} "
    }
}