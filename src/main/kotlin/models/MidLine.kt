package voronoiDiagram.models

import javafx.scene.paint.Color
import javafx.scene.shape.Line
import tornadofx.style
import voronoiDiagram.libs.Utils

enum class Position {
    TOP, BOTTOM, LEFT, RIGHT, IN_LINE
}

open class MidLine(var start: Point, var end: Point) {

    var color: Color = Color.BLACK
    val originStart: Point
    val originEnd: Point
    var isHP = true

    constructor (startX: Double, startY: Double, endX: Double, endY: Double) : this(
        Point(startX, startY),
        Point(endX, endY)
    )

    constructor (start: Point, end: Point, a: Point, b: Point) : this(
        start, end
    )

    init {
        if (isVertical()) {
            if (start.y > end.y) {
                this.start = this.end.also { this.end = this.start }
            }
        } else {
            if (start.x > end.x) {
                this.start = this.end.also { this.end = this.start }
            }
        }
        originStart = Point(start.x, start.y)
        originEnd = Point(end.x, end.y)
    }

    fun fix() {
        if (isVertical()) {
            if (start.y > end.y) {
                this.start = this.end.also { this.end = this.start }
            }
        } else {
            if (start.x > end.x) {
                this.start = this.end.also { this.end = this.start }
            }
        }
    }

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

    fun cut(next: Point, intersection: Point) {
        println("line ${toString()} next = ${next.toString()} ${isWhere(next)}")
        when (isWhere(next)) {
            Position.TOP ->
                end = Point(intersection.x, intersection.y)
            Position.BOTTOM ->
                start = Point(intersection.x, intersection.y)
            Position.LEFT ->
                end = Point(intersection.x, intersection.y)
            Position.RIGHT ->
                start = Point(intersection.x, intersection.y)
            else -> {
            }
        }
    }

    fun isWhere(p: Point): Position {
        if (isVertical()) {
            val slope = Utils.getSlope(originStart, originEnd)
            val slopeToP = Utils.getSlope(originStart, p)
            return when {
                slopeToP < slope -> Position.RIGHT
                slopeToP > slope -> Position.LEFT
                else -> Position.IN_LINE
            }
        } else {
            val slope = Utils.getSlope(originStart, originEnd)
            val slopeToP = Utils.getSlope(originStart, p)
            return when {
                slopeToP < slope -> Position.TOP
                slopeToP > slope -> Position.BOTTOM
                else -> Position.IN_LINE
            }
        }
    }

    fun isVertical(): Boolean {
        return Math.abs(Utils.getSlope(start, end)) >= 1
    }

    val out
        get() = "E ${start.x.toInt()} ${start.y.toInt()} ${end.x.toInt()} ${end.y.toInt()}"

    override fun toString(): String {
        return "{(${start.x.toInt()}, ${start.y.toInt()})(${end.x.toInt()}, ${end.y.toInt()})} "
    }
}