package voronoiDiagram.models

import voronoiDiagram.libs.Utils

class VoronoiDiagram3Point(private val points: ArrayList<Point>) {

    val lines: ArrayList<Line> = ArrayList()

    fun execute() {
        points.sortedWith(compareBy({ it.y }, { it.x }))
        print("points")
        points.forEach {
            print("${it.toString()}\n")
        }
        if (points.size == 2) {
            val pointA = points[0]
            val pointB = points[1]
            lines.add(Utils.getMidLine(pointA, pointB))
        } else if (points.size == 3) {
            lines.add(Utils.getMidLine(points[0], points[1]))
            lines.add(Utils.getMidLine(points[1], points[2]))
            lines.add(Utils.getMidLine(points[0], points[2]))
            val intersection01 = Utils.findIntersection(lines[0], lines[1])
            val intersection12 = Utils.findIntersection(lines[1], lines[2])
            val intersection02 = Utils.findIntersection(lines[0], lines[2])
            if (intersection01.x in 0.0..600.0 && intersection01.y in 0.0..600.0) {
                if (points[2].y < intersection01.y) {
                    lines[0].endX = intersection01.x
                    lines[0].endY = intersection01.y
                } else {
                    lines[0].startX = intersection01.x
                    lines[0].startY = intersection01.y
                }
                if (points[0].y < intersection12.y) {
                    lines[1].endX = intersection12.x
                    lines[1].endY = intersection12.y
                } else {
                    lines[1].startX = intersection12.x
                    lines[1].startY = intersection12.y
                }
                if (points[1].y < intersection02.y) {
                    lines[2].endX = intersection02.x
                    lines[2].endY = intersection02.y
                } else {
                    lines[2].startX = intersection02.x
                    lines[2].startY = intersection02.y
                }
            }else{
                lines.removeAt(2)
            }
        } else {
            //TODO
        }
    }
}