package voronoiDiagram.models

import javafx.scene.paint.Color
import voronoiDiagram.libs.Utils

enum class Position {
    TOP, BOTTOM, LEFT, RIGHT, IN_LINE
}

class MidLine(start: Point, end: Point, var a: Point, var b: Point) :
    Line(start, end) {
    val originStart: Point
    val originEnd: Point

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

    fun cut(next: Point, intersection: Point) {
        println("line ${toString()} next = ${next.toString()} ${isWhere(next)}")
        when (isWhere(next)) {
            Position.TOP ->
                end = intersection
            Position.BOTTOM ->
                start = intersection
            Position.LEFT ->
                end = intersection
            Position.RIGHT ->
                start = intersection
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
}