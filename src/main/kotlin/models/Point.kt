package voronoiDiagram.models

class Point(var x: Double, var y: Double) {
    override fun toString(): String {
        return "($x , $y)"
    }
}