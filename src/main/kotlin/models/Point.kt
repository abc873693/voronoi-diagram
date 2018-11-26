package voronoiDiagram.models

import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Point(var x: Double, var y: Double) {

    val length
        get() = Math.sqrt(x * x + y * y)

    fun getCircle(): Circle {
        val circle = Circle(x, y, 3.0)
        circle.fill = Color.RED
        return circle
    }

    val out
        get() = "P ${x.toInt()} ${y.toInt()}"

    override fun toString(): String {
        return "(${x.toInt()} , ${y.toInt()})"
    }
}