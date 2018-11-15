package voronoiDiagram.models

import javafx.scene.shape.Line
import voronoiDiagram.MAX

class Line(var startX: Double, var startY: Double, var endX: Double, var endY: Double) {

    val start = Point(startX, startY)
    val end = Point(endX, endY)

    val getFxLine
        get() = Line(startX, MAX - startY, endX, MAX - endY)

    val out
        get() = "E ${startX.toInt()} ${startY.toInt()} ${endX.toInt()} ${endY.toInt()}"

    override fun toString(): String {
        return "{(${startX.toInt()}, ${startY.toInt()})(${endX.toInt()}, ${endY.toInt()})} "
    }
}