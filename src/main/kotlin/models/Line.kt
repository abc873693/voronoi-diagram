package voronoiDiagram.models

import javafx.scene.shape.Line

class Line(var startX: Double, var startY: Double, var endX: Double, var endY: Double) {

    val start = Point(startX, startY)
    val end = Point(endX, endY)

    val getFxLine
        get() = Line(startX, startY, endX, endY)

    override fun toString(): String {
        return "L ($startX, $startY) ($endX, $endY)"
    }
}