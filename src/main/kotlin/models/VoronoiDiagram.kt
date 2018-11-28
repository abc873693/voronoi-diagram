package voronoiDiagram.models

import voronoiDiagram.libs.Utils
import kotlin.collections.ArrayList
import java.util.Vector


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
            println(" left = ${left.points.size} right = ${right.points.size}")
            val points: ArrayList<Point> = ArrayList()
            points.addAll(left.points)
            points.addAll(right.points)
            val result = VoronoiDiagram(points)
            result.lines.addAll(left.lines)
            result.lines.addAll(right.lines)
            for (i in 0 until left.points.size) {
                for (j in 0 until right.points.size) {
                    result.lines.add(Utils.getMidLine(left.points[i], right.points[j]))
                }
            }
            //result.execute()
            return result
        }
    }
}