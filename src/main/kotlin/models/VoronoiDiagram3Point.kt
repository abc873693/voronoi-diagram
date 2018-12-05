package voronoiDiagram.models

import voronoiDiagram.libs.Utils

class VoronoiDiagram3Point(points: ArrayList<Point>) : VoronoiDiagram(points) {

    override fun execute() {
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
            val angle012 = Utils.getAngle(points[0], points[1], points[2])
            val angle021 = Utils.getAngle(points[0], points[1], points[2])
            val angle102 = Utils.getAngle(points[1], points[0], points[2])
            println("angle012 $angle012")
            println("angle021 $angle021")
            println("angle102 $angle102")
            println("middlePoint01 ${middlePoint01.toString()}")
            println("middlePoint12 ${middlePoint12.toString()}")
            println("middlePoint02 ${middlePoint02.toString()}")
            println("lines0 ${lines[0].toString()}")
            println("lines1 ${lines[1].toString()}")
            println("lines2 ${lines[2].toString()}")
            if (intersection01.x in 0.0..600.0 && intersection01.y in 0.0..600.0) {
                /*if (points[2].y in points[0].y..points[1].y || points[0].y >points[1].y) {
                    lines[0].end.x = intersection01.x
                    lines[0].end.y = intersection01.y
                } else {
                    lines[0].start.x = intersection01.x
                    lines[0].start.y = intersection01.y
                }
                if (points[0].y in points[1].y..points[2].y || points[1].y>points[2].y) {
                    lines[1].end.x = intersection12.x
                    lines[1].end.y = intersection12.y
                } else {
                    lines[1].start.x = intersection12.x
                    lines[1].start.y = intersection12.y
                }
                if (points[1].y in points[0].y..points[2].y || points[0].y < points[2].y) {
                    lines[2].end.x = intersection02.x
                    lines[2].end.y = intersection02.y
                } else {
                    lines[2].start.x = intersection02.x
                    lines[2].start.y = intersection02.y
                }*/
                if (lines[0].start.x == 0.0) {
                    if (points[2].x > intersection01.x) {
                        lines[0].end.x = intersection01.x
                        lines[0].end.y = intersection01.y
                    } else {
                        lines[0].start.x = intersection01.x
                        lines[0].start.y = intersection01.y
                    }
                } else if (lines[0].start.y == 0.0) {
                    if (points[2].y < intersection01.y) {
                        lines[0].end.x = intersection01.x
                        lines[0].end.y = intersection01.y
                    } else {
                        lines[0].start.x = intersection01.x
                        lines[0].start.y = intersection01.y
                    }
                } else if (lines[0].start.y == 600.0) {
                    if (points[2].y > intersection01.y) {
                        lines[0].start.x = intersection01.x
                        lines[0].start.y = intersection01.y
                    } else {
                        lines[0].end.x = intersection01.x
                        lines[0].end.y = intersection01.y
                    }
                }

                if (lines[1].start.x == 0.0) {
                    if (points[0].x > intersection12.x) {
                        lines[1].end.x = intersection12.x
                        lines[1].end.y = intersection12.y
                    } else {
                        lines[1].start.x = intersection12.x
                        lines[1].start.y = intersection12.y
                    }
                } else if (lines[1].start.y == 0.0) {
                    if (points[0].x < middlePoint12.x) {
                        lines[1].start.x = intersection12.x
                        lines[1].start.y = intersection12.y
                    } else {
                        lines[1].end.x = intersection12.x
                        lines[1].end.y = intersection12.y
                    }
                } else if (lines[1].start.y == 600.0) {
                    if (points[0].x > intersection12.x) {
                        lines[1].end.x = intersection12.x
                        lines[1].end.y = intersection12.y
                    } else {
                        lines[1].start.x = intersection12.x
                        lines[1].start.y = intersection12.y
                    }
                }

                if (lines[2].start.x == 0.0) {
                    if (points[1].y > intersection02.y) {
                        lines[2].end.x = intersection02.x
                        lines[2].end.y = intersection02.y
                    } else {
                        lines[2].start.x = intersection02.x
                        lines[2].start.y = intersection02.y
                    }
                } else if (lines[2].start.y == 0.0) {
                    if (points[1].y < intersection02.y) {
                        lines[2].start.x = intersection02.x
                        lines[2].start.y = intersection02.y
                    } else {
                        lines[2].end.x = intersection02.x
                        lines[2].end.y = intersection02.y
                    }
                } else if (lines[2].start.y == 600.0) {
                    if (points[1].y < intersection02.y) {
                        lines[2].end.x = intersection02.x
                        lines[2].end.y = intersection02.y
                    } else {
                        lines[2].start.x = intersection02.x
                        lines[2].start.y = intersection02.y
                    }
                }
            } else {
                lines.removeAt(2)
            }
        } else {
            //TODO
        }
    }
}