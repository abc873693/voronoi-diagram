package voronoiDiagram.libs

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
}