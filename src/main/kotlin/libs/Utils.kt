package voronoiDiagram.libs

import voronoiDiagram.models.*
import java.io.File
import java.util.*


object Utils {

    fun parseInputData(file: File): ArrayList<TestData> {
        val results: ArrayList<TestData> = ArrayList()
        var size = 0
        var stop = false
        file.forEachLine { text ->
            if (!stop && text.isNotEmpty() && text.first() != '#') {
                if (size == 0) {
                    size = text.toInt()
                    if (size == 0) stop = true
                    else results.add(TestData(size))
                } else {
                    size--
                    val textNumbers = text.split(' ')
                    results.last().points.add(Point(textNumbers[0].toDouble(), textNumbers[1].toDouble()))
                }
            }
        }
        return results
    }

    fun parseOutputData(file: File): OutputData {
        val results = OutputData()
        file.forEachLine { text ->
            if (text.isNotEmpty()) {
                val textNumbers = text.split(' ')
                println(textNumbers)
                if (textNumbers.size > 1) {
                    if (textNumbers.first() == "P") {
                        results.points.add(Point(textNumbers[1].toDouble(), textNumbers[2].toDouble()))
                    } else if (textNumbers.first() == "E") {
                        results.lines.add(
                            MidLine(
                                textNumbers[1].toDouble(),
                                textNumbers[2].toDouble(),
                                textNumbers[3].toDouble(),
                                textNumbers[4].toDouble()
                            )
                        )
                    }
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

    fun getMidLine(pointA: Point, pointB: Point): MidLine {
        val midPoint = getMidPoint(pointA, pointB)
        val slope = getSlope(pointA, pointB)
        var startX = 0.0
        var endX = 600.0
        var startY = (-(1 / slope)) * (startX - midPoint.x) + midPoint.y
        var endY = (-(1 / slope)) * (endX - midPoint.x) + midPoint.y
        //print("before : ${startX.toInt()}, ${startY.toInt()}, ${endX.toInt()}, ${endY.toInt()}\n")
        //print("${startX.toInt()}, ${startY.toInt()}, ${endX.toInt()}, ${endY.toInt()}\n")
        if (startY < 0.0 || startY > 600.0) {
            startY = 600.0
            startX = (startY - midPoint.y) / (-(1 / slope)) + midPoint.x
            if (startX < 0.0 || startX > 600.0) {
                startY = 0.0
                startX = (startY - midPoint.y) / (-(1 / slope)) + midPoint.x
            }
        }
        if (endY < 0.0 || endY > 600.0) {
            endY = 0.0
            endX = (endY - midPoint.y) / (-(1 / slope)) + midPoint.x
            if (endX < 0.0 || endX > 600.0) {
                endY = 600.0
                endX = (endY - midPoint.y) / (-(1 / slope)) + midPoint.x
            }
        }
        //print("after : ${startX.toInt()}, ${startY.toInt()}, ${endX.toInt()}, ${endY.toInt()}\n")
        return MidLine(Point(startX, startY), Point(endX, endY), pointA, pointB)
    }

    fun findIntersection(lineA: MidLine, lineB: MidLine): Point {
        val a1 = lineA.end.y - lineA.start.y
        val b1 = lineA.start.x - lineA.end.x
        val c1 = a1 * lineA.start.x + b1 * lineA.start.y
        val a2 = lineB.end.y - lineB.start.y
        val b2 = lineB.start.x - lineB.end.x
        val c2 = a2 * lineB.start.x + b2 * lineB.start.y
        val delta = a1 * b2 - a2 * b1
        return Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
    }

    fun sortPointsByY(points: ArrayList<Point>) {
        val list = points.sortedWith(compareBy({ it.y }, { it.x }))
        points.clear()
        list.forEach { point ->
            points.add(point)
        }
    }

    fun getAngle(a: Point, b: Point, c: Point): Double {
        val u = Point(a.x - b.x, a.y - b.y)
        val v = Point(c.x - b.x, c.y - b.y)
        val cosX = (u.x * v.x + u.y * v.y) / (u.length * v.length)
        return Math.toDegrees(Math.acos(cosX))
    }

    fun orientation(p: Point, q: Point, r: Point): Int {
        val num = ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)).toInt()

        if (num == 0) return 0
        return if (num > 0) 1 else 2
    }
}