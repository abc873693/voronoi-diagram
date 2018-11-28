package voronoiDiagram.models

import javafx.scene.control.Label
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.FontWeight
import tornadofx.*
import tornadofx.Stylesheet.Companion.label

class Point(var x: Double, var y: Double) {

    val length
        get() = Math.sqrt(x * x + y * y)

    fun getCircle(): Circle {
        val circle = Circle(x, y, 3.0)
        circle.fill = Color.RED
        return circle
    }

    fun getLabel(): Label {
        val label = Label(toString())
        label.layoutX = x + 1
        label.layoutY = y + 1
        label.style {
            backgroundColor = MultiValue()
            backgroundColor += c("#cecece", 0.0)
            textFill = Color.BLUE
            fontSize = Dimension(12.0, Dimension.LinearUnits.px)
        }
        return label
    }

    val out
        get() = "P ${x.toInt()} ${y.toInt()}"

    override fun toString(): String {
        return "(${x.toInt()} , ${y.toInt()})"
    }
}