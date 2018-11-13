package voronoiDiagram.models

import voronoiDiagram.libs.Utils

class VoronoiDiagram3Point(private var points: ArrayList<Point>) {

    val lines: ArrayList<Line> = ArrayList()

    fun execute() {
        Utils.sortPoints(points)
        println("points")
        points.forEach {
            println(it.toString())
        }
        if (points.size == 2) {
            val pointA = points[0]
            val pointB = points[1]
            lines.add(Utils.getMidLine(pointA, pointB))
        } else if (points.size == 3) {
            lines.add(Utils.getMidLine(points[0], points[1]))
            lines.add(Utils.getMidLine(points[1], points[2]))
            lines.add(Utils.getMidLine(points[0], points[2]))
            val middlePoint01 = Utils.getMidPoint(points[0], points[1])
            val middlePoint12 = Utils.getMidPoint(points[1], points[2])
            val middlePoint02 = Utils.getMidPoint(points[0], points[2])
            val intersection01 = Utils.findIntersection(lines[0], lines[1])
            val intersection12 = Utils.findIntersection(lines[1], lines[2])
            val intersection02 = Utils.findIntersection(lines[0], lines[2])
            val slope01 = Utils.getSlope(points[0], points[1])
            val slope12 = Utils.getSlope(points[1], points[2])
            val slope02 = Utils.getSlope(points[0], points[2])
            val slopeMiddlePoint0 = Utils.getSlope(points[0], middlePoint12)
            val slopeMiddlePoint1 = Utils.getSlope(points[1], middlePoint02)
            val slopeMiddlePoint2 = Utils.getSlope(points[2], middlePoint01)
            println("points[0] ${points[0].toString()}")
            println("points[1] ${points[1].toString()}")
            println("points[2] ${points[2].toString()}")
            println("middlePoint01 ${middlePoint01.toString()}")
            println("middlePoint12 ${middlePoint12.toString()}")
            println("middlePoint02 ${middlePoint02.toString()}")
            if (intersection01.x in 0.0..600.0 && intersection01.y in 0.0..600.0) {
                if (slope01 > slopeMiddlePoint2) {
                    lines[0].endX = intersection01.x
                    lines[0].endY = intersection01.y
                } else {
                    lines[0].startX = intersection01.x
                    lines[0].startY = intersection01.y
                }
                if (slope12 > slopeMiddlePoint0) {
                    lines[1].endX = intersection12.x
                    lines[1].endY = intersection12.y
                } else {
                    lines[1].startX = intersection12.x
                    lines[1].startY = intersection12.y
                }
                if (slope02 > slopeMiddlePoint1) {
                    lines[2].endX = intersection02.x
                    lines[2].endY = intersection02.y
                } else {
                    lines[2].startX = intersection02.x
                    lines[2].startY = intersection02.y
                }
            } else {
                lines.removeAt(2)
            }
        } else {
            //TODO
        }
    }
}