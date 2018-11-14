package voronoiDiagram.models

class Point(var x: Double, var y: Double) {

    val length
        get() = Math.sqrt(x * x + y * y)

    override fun toString(): String {
        return "($x , $y)"
    }
}