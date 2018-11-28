package voronoiDiagram.models

import voronoiDiagram.libs.Utils
import kotlin.collections.ArrayList
import java.util.Vector




class VoronoiDiagram(private val points: ArrayList<Point>) {

    val lines: ArrayList<Line> = ArrayList()
    val convexHullPoints: ArrayList<Point> = ArrayList()
    var leftmostIndex = 0

    init {
        leftmostIndex = findLeftmost()
    }

    fun execute() {
        points.forEach {
            println(it.toString())
        }
        if (points.size == 2) {

        } else {
            //TODO
        }
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
}