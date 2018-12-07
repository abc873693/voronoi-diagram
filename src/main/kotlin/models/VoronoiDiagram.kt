package voronoiDiagram.models

import javafx.scene.paint.Color
import voronoiDiagram.libs.Utils
import kotlin.collections.ArrayList


open class VoronoiDiagram(val points: ArrayList<Point>) {

    var lines: ArrayList<MidLine> = ArrayList()
    val convexHullPoints: ArrayList<Point> = ArrayList()
    var leftmostIndex = 0
    var startIndex = 0
    var endIndex = 0

    init {
        leftmostIndex = findLeftmost()
        convexHull()
        endIndex = points.size - 1
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
            if ((midLine.start.x == a.x && midLine.start.y == a.y) && (midLine.end.x == b.x && midLine.end.y == b.y) ||
                (midLine.start.x == b.x && midLine.start.y == b.y) && (midLine.end.x == a.x && midLine.end.y == a.y)
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
        if (startIndex != endIndex)
            convexHullLostPoints.add(points[startIndex])
        convexHullLostPoints.add(points[endIndex])
        return convexHullLostPoints
    }

    fun setColor(color: Color) {
        lines.forEach {
            it.color = color
        }
        points.forEach {
            it.color = color
        }
    }

    fun resetLineColor() {
        lines.forEach {
            it.resetColor()
        }
    }

    companion object {
        fun conquer(left: VoronoiDiagram, right: VoronoiDiagram): VoronoiDiagram {
            val points: ArrayList<Point> = ArrayList()
            println("----------------")
            left.setColor(Color.PURPLE)
            right.setColor(Color.YELLOWGREEN)
            left.points.forEach {
                it.color = Color.PURPLE
                points.add(it)
            }
            right.points.forEach {
                it.color = Color.YELLOWGREEN
                points.add(it)
            }
            /* points.addAll(left.points)
             points.addAll(right.points)*/
            val result = VoronoiDiagram(points)
            val lPoints = left.findIndex(result)
            val rPoints = right.findIndex(result)
            var l = 0
            var r = 0
            Utils.sortPointsByY(lPoints)
            Utils.sortPointsByY(rPoints)
            println("left startIndex = ${left.startIndex} endIndex = ${left.endIndex}")
            println("right startIndex = ${right.startIndex} endIndex = ${right.endIndex}")
            println("${lPoints}")
            println("${rPoints}")
            var c: Point? = null
            var next: Point? = null
            do {
                val lineA = Utils.getMidLine(lPoints[l], rPoints[r])
                val last = next
                val d = c
                println("l  = $l r  = $r")
                println("leftPoint = ${lPoints[l]} rightPoint = ${rPoints[r]}")
                println("lineA  = ${lineA.toString()}")
                if (l == lPoints.size - 1 && r == rPoints.size - 1) {
                    if (d != null)
                        if (d.x in 0.0..600.0 && d.y in 0.0..600.0)
                            if (lineA.isVertical())
                                lineA.start = d
                            else {
                                if (lineA.slope < 0)
                                    lineA.start = d
                                else
                                    lineA.end = d
                            }
                    l++
                    r++
                } else {
                    if (l == lPoints.size - 1 && r < rPoints.size - 1) {
                        c = Utils.findIntersection(lineA, Utils.getMidLine(rPoints[r], rPoints[r + 1]))
                        /*val rIndex = right.findLineIndex(rPoints[r], rPoints[r + 1])
                        right.lines[rIndex].start = c*/
                        right.lines[0].start = c
                        next = rPoints[r + 1]
                        r++
                    } else if (l < lPoints.size - 1 && r == rPoints.size - 1) {
                        c = Utils.findIntersection(lineA, Utils.getMidLine(lPoints[l], lPoints[l + 1]))
                        //println("left ${left.lines[0].toString()}")
                        left.lines[0].end = c
                        //println("left ${left.lines[0].toString()}")
                        next = lPoints[l + 1]
                        l++
                    } else {
                        if (lPoints[l + 1].y <= rPoints[r + 1].y) {
                            val a = Utils.findIntersection(lineA, Utils.getMidLine(lPoints[l], lPoints[l + 1]))
                            val b = Utils.findIntersection(lineA, Utils.getMidLine(rPoints[r], rPoints[l + 1]))
                            l++
                            next = lPoints[l]
                            c = if (a.y < b.y) {
                                a
                            } else {
                                b
                            }
                            left.lines[0].end = c
                        } else {
                            val a = Utils.findIntersection(lineA, Utils.getMidLine(lPoints[l], lPoints[r + 1]))
                            val b = Utils.findIntersection(lineA, Utils.getMidLine(rPoints[r], rPoints[r + 1]))
                            r++
                            next = rPoints[r]
                            c = if (a.y < b.y) {
                                a
                            } else {
                                b
                            }
                            right.lines[0].start = c
                        }
                        if (result.lines.isNotEmpty())
                            if(d!=null)
                            if (result.lines.last().isVertical())
                                result.lines.last().end = d
                            else {
                                if (result.lines.last().slope < 0)
                                    result.lines.last().end = d
                                else
                                    result.lines.last().start = d
                            }
                    }
                    if (c.x in 0.0..600.0 && c.y in 0.0..600.0) {
                        if (last == null && d == null) {
                            println("top ${lineA.toString()}")
                            if (lineA.isVertical())
                                lineA.end = c
                            else {
                                if (lineA.slope < 0)
                                    lineA.end = c
                                else
                                    lineA.start = c
                            }
                            println("top ${lineA.toString()}")

                        } else if (last != null && d != null) {
                            // if(lineA.isVertical())
                            lineA.fix()
                            lineA.start = d
                            //lineA.cut(last, d)
                            //lineA.end = c
                            lineA.end = c
                            println("${last.toString()} ${d.toString()} ")
                            println("${next.toString()} ${c.toString()} ")
                        }
                    }
                    //lineA.cut(next, c)
                }
                lineA.color = Color.PURPLE
                result.lines.add(lineA)
            } while (l < lPoints.size && r < rPoints.size)
            left.resetLineColor()
            right.resetLineColor()
            result.lines.addAll(left.lines)
            result.lines.addAll(right.lines)
            println(result.lines.size)
            result.sortPoints()
            return result
        }
    }
}