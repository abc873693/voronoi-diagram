package voronoiDiagram.libs

import voronoiDiagram.models.Line
import voronoiDiagram.models.Point
import voronoiDiagram.models.TestData
import java.io.File

object Utils {

    fun parseData(file: File): ArrayList<TestData> {
        val results: ArrayList<TestData> = ArrayList()
        var size = 0
        var stop = false
        file.forEachLine { text ->
            if (!stop && text.isNotEmpty() && text.first() != '#') {
                if (size == 0) {
                    size = text.toInt()
                    results.add(TestData(size))
                    if (size == 0) stop = true
                } else {
                    size--
                    val textNumbers = text.split(' ')
                    results.last().points.add(Point(textNumbers[0].toDouble(), textNumbers[1].toDouble()))
                }
            }
        }
        return results
    }

    fun getMidPoint(pointA: Point, pointB: Point): Point {
        return Point((pointA.x + pointB.x) / 2, (pointA.y + pointB.y) / 2)
    }

    fun getSlope(pointA: Point, pointB: Point): Double {
        return (pointA.y - pointB.y) / (pointA.x - pointB.x)
    }

    fun getMidLine(pointA: Point, pointB: Point): Line {
        val midPoint = getMidPoint(pointA, pointB)
        val slope = getSlope(pointA, pointB)
        var startX = 0.0
        var endX = 600.0
        var startY = (-(1 / slope)) * (startX - midPoint.x) + midPoint.y
        var endY = (-(1 / slope)) * (endX - midPoint.x) + midPoint.y
        print("${startX.toInt()}, ${startY.toInt()}, ${endX.toInt()}, ${endY.toInt()}\n")
        if (startY < 0.0 || startY > 600.0) {
            startY = 600.0
            startX = (startY - midPoint.y) / (-(1 / slope)) + midPoint.x
        }
        if (endY < 0.0 || endY > 600.0) {
            endY = 0.0
            endX = (endY - midPoint.y) / (-(1 / slope)) + midPoint.x
        }
        print("${startX.toInt()}, ${startY.toInt()}, ${endX.toInt()}, ${endY.toInt()}\n")
        return Line(startX, startY, endX, endY)
    }

    fun findIntersection(l1: Line, l2: Line): Point {
        val a1 = l1.end.y - l1.start.y
        val b1 = l1.start.x - l1.end.x
        val c1 = a1 * l1.start.x + b1 * l1.start.y

        val a2 = l2.end.y - l2.start.y
        val b2 = l2.start.x - l2.end.x
        val c2 = a2 * l2.start.x + b2 * l2.start.y

        val delta = a1 * b2 - a2 * b1
        return Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
    }
}