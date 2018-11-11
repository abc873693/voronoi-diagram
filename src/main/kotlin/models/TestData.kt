package javafx.test.models

class TestData(val size: Int) {
    val points: ArrayList<Point> = ArrayList()

    override fun toString(): String {
        var text = "\n"
        text += "Data length = $size"
        points.forEachIndexed { index, point ->
            text += "\nP${index + 1} = ( ${point.x} , ${point.y} )"
        }
        return text
    }
}