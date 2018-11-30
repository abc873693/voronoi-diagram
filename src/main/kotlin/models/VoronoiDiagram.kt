package voronoiDiagram.models

import voronoiDiagram.libs.Utils
import kotlin.collections.ArrayList


open class VoronoiDiagram(val points: ArrayList<Point>) {

    var lines: ArrayList<MidLine> = ArrayList()
    val convexHullPoints: ArrayList<Point> = ArrayList()
    var leftmostIndex = 0

    init {
        leftmostIndex = findLeftmost()
    }

    open fun execute() {
        points.forEach {
            println(it.toString())
        }
        if (points.size == 2) {

        } else {
            //TODO
        }
    }

    fun sortPoints() {
        val list = points.sortedWith(compareBy({ it.x }, { it.y }))
        points.clear()
        list.forEach { point ->
            points.add(point)
        }
    }

    fun findLineIndex(a: Point, b: Point): Int {
        lines.forEachIndexed { index, midLine ->
            if ((midLine.startX == a.x && midLine.startY == a.y) && (midLine.endX == b.x && midLine.endY == b.y) ||
                (midLine.startX == b.x && midLine.startY == b.y) && (midLine.endX == a.x && midLine.endY == a.y)
            )
                return index
        }
        return -1
    }

    fun divide(): ArrayList<VoronoiDiagram> {
        val list = ArrayList<VoronoiDiagram>()
        points.forEach {
            val points = ArrayList<Point>()
            points.add(it)
            list.add(VoronoiDiagram(points))
        }
        return list
    }

    fun convexHull(): Array<Point> {
        sortPoints()
        if (points.size < 3) return points.toTypedArray()
        val hull = ArrayList<Point>()
        var p = leftmostIndex
        var q: Int
        do {
            hull.add(points[p])
            q = (p + 1) % points.size
            for (i in 0 until points.size) {
                if (Utils.orientation(points[p], points[i], points[q]) == 2)
                    q = i
            }
            p = q
        } while (p != leftmostIndex)
        return hull.toTypedArray()
    }

    private fun findLeftmost(): Int {
        var l = 0
        for (i in 1 until points.size)
            if (points[i].x < points[l].x)
                l = i
        return l
    }

    companion object {
        fun conquer(left: VoronoiDiagram, right: VoronoiDiagram): VoronoiDiagram {
            val points: ArrayList<Point> = ArrayList()
            points.addAll(left.points)
            points.addAll(right.points)
            val result = VoronoiDiagram(points)
            var l = left.points.size - 1
            var r = 0
            do {
                val leftPoint: Point = left.points[l]
                val rightPoint: Point = right.points[r]
                val lineA = Utils.getMidLine(leftPoint, rightPoint)
                var lineB: MidLine
                if (l >= 0 && r < right.points.size) {
                    var point: Point
                    if (left.points[l].y < right.points[r].y) {
                        l--
                    } else {
                        r++
                    }
                }
                result.lines.add(lineA)
            } while (l < 0 && r >= right.points.size)
            result.lines.addAll(left.lines)
            result.lines.addAll(right.lines)
            result.sortPoints()
            return result
        }
    }
}