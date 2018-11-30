package voronoiDiagram.models

import voronoiDiagram.libs.Utils

class MidLine(var a: Point, var b: Point, startX: Double, startY: Double, endX: Double, endY: Double) :
    Line(startX, startY, endX, endY) {


    fun isVertical(): Boolean {
        return Math.abs(Utils.getSlope(a, b)) >= 1
    }
}