package voronoiDiagram.models

import javafx.scene.paint.Color
import voronoiDiagram.libs.Utils
import kotlin.collections.ArrayList


open class VoronoiDiagram(val points: ArrayList<Point>) {

    var lines: ArrayList<MidLine> = ArrayList()
    val convexHullPoints: ArrayList<Point> = ArrayList()
    var leftmostIndex = 0
    var startIndex = 0
    var endIndex = -1

    init {
        leftmostIndex = findLeftmost()
        convexHull()
        endIndex = points.size - 1
        points.forEach {
            it.color = Color.RED
        }
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
        convexHullPoints.clear()
        convexHullPoints.addAll(hull)
        return hull.toTypedArray()
    }

    private fun findLeftmost(): Int {
        var l = 0
        for (i in 1 until points.size)
            if (points[i].x < points[l].x)
                l = i
        return l
    }

    fun findIndex(merged: VoronoiDiagram): ArrayList<Point> {
        val convexHullLostPoints: ArrayList<Point> = ArrayList()
        convexHullPoints.forEachIndexed { index, point ->
            val p = merged.convexHullPoints.find { it.x == point.x && it.y == point.y }
            if (p == null) {
                if (startIndex == 0) startIndex = index
                else endIndex = index
                convexHullLostPoints.add(point)
                val q = merged.points.find { it.x == point.x && it.y == point.y }
                q?.color = Color.PURPLE
            }
        }
        if (startIndex != 0) startIndex--
        if (endIndex != points.size - 1) endIndex++
        convexHullLostPoints.add(points[startIndex])
        convexHullLostPoints.add(points[endIndex])
        return convexHullLostPoints
    }

    companion object {
        fun conquer(left: VoronoiDiagram, right: VoronoiDiagram): VoronoiDiagram {
            val points: ArrayList<Point> = ArrayList()
            points.addAll(left.points)
            points.addAll(right.points)
            val result = VoronoiDiagram(points)
            val lPoints = left.findIndex(result)
            val rPoints = right.findIndex(result)
            var l = 0
            var r = 0
            println("${lPoints.size}")
            println("${rPoints.size}")
            do {
                val leftPoint: Point = lPoints[l]
                val rightPoint: Point = rPoints[r]
                val lineA = Utils.getMidLine(leftPoint, rightPoint)
                var lineB: MidLine
                if (l < lPoints.size && r < rPoints.size) {
                    val a = Utils.findIntersection(lineA, Utils.getMidLine(lPoints[l], lPoints[l + 1]))
                    val b = Utils.findIntersection(lineA, Utils.getMidLine(rPoints[r], rPoints[r + 1]))
                    if (a.y > b.y) {
                        l++
                        println("a = ${a.toString()}")
                        lineA.startX = a.x
                        lineA.startY = a.y
                        println(lineA.toString())
                    } else {
                        r++
                        println("b = ${b.toString()}")
                        lineA.startX = b.x
                        lineA.startY = b.y
                    }
                    if(l ==lPoints.size-1)
                    {
                        result.lines.add(Utils.getMidLine(lPoints.last(), rPoints[r]))
                        break
                    }
                    if(r ==rPoints.size-1)
                    {
                        result.lines.add(Utils.getMidLine(lPoints[l], rPoints.last()))
                        break
                    }
                }
                result.lines.add(lineA)
            } while (l < lPoints.size && r < rPoints.size)
            println(result.lines.size)
            result.lines.addAll(left.lines)
            result.lines.addAll(right.lines)
            result.sortPoints()
            return result
        }
    }
}