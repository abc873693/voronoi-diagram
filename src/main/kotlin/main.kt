package voronoiDiagram

import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import tornadofx.View
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import javafx.stage.Stage
import tornadofx.*
import voronoiDiagram.libs.Utils
import voronoiDiagram.models.*
import java.io.File


fun main(args: Array<String>) {
    launch(MainApp::class.java)
}

class MainApp : App(HomePage::class, Styles::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

class Styles : Stylesheet() {
    init {
        label {
            translateX = Dimension(1.0, Dimension.LinearUnits.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

private const val INPUT_DATA = "InputData"
private const val OUTPUT_DATA = "OutputData"
public const val PANEL_MIN = 0.0
public const val PANEL_MAX = 600.0

class HomePage : View() {

    private lateinit var label: Label
    lateinit var inputData: Label
    lateinit var outputData: Label
    lateinit var groups: Group
    private var testDataList: ArrayList<TestData> = ArrayList()
    private var currentTestDataIndex = 0
    private var points: ArrayList<Point> = ArrayList()
    var outputText = OUTPUT_DATA

    private var convexHullEnabled = false
    private var stepByStepEnabled = false

    private var vd: VoronoiDiagram = VoronoiDiagram(points)
    private var vdList: ArrayList<VoronoiDiagram> = ArrayList()

    init {
    }

    private fun setLabelCss(inlineCss: InlineCss) {
        inlineCss.apply {
            backgroundColor = MultiValue()
            backgroundColor += c("#cecece", 0.0)
            fontSize = Dimension(16.0, Dimension.LinearUnits.px)
        }
    }

    override var root = generateForm()

    private fun generateForm(): HBox {
        return hbox(20) {
            maxHeight = 600.0
            minHeight = 600.0
            minWidth = 1200.0
            maxWidth = 1200.0
            stackpane {
                groups = group {
                    rectangle {
                        fill = Color.WHITE
                        width = PANEL_MAX
                        height = PANEL_MAX
                    }
                }
                val canvas = canvas {
                    height = PANEL_MAX
                    width = PANEL_MAX
                }
                canvas.setOnMouseClicked { evt ->
                    addPoint(evt)
                }
            }
            vbox(20) {
                minWidth = 100.0
                button("開啟檔案") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("文字文件", "*.txt"))
                        val files: List<File> = chooseFile("選取輸入檔", filters, FileChooserMode.Single)
                        if (files.isNotEmpty())
                            files.first().apply {
                                runAsync {

                                } ui {
                                    clean()
                                    testDataList = Utils.parseInputData(this)
                                    currentTestDataIndex = 0
                                    testDataList[currentTestDataIndex].points.forEach { point ->
                                        groups.add(point.getCircle())
                                    }
                                    updateInputData()
                                }
                            }
                    }
                }
                button("下一筆") {
                    action {
                        if (testDataList.size == 0)
                            label.text = "尚未選取檔案"
                        else {
                            clean()
                            currentTestDataIndex++
                            if (currentTestDataIndex < testDataList.size) {
                                testDataList[currentTestDataIndex].points.forEach { point ->
                                    groups.add(point.getCircle())
                                }
                                updateInputData()
                            } else
                                label.text = "讀檔模式已讀到結尾"
                        }
                    }
                }
                button("輸出檔案") {
                    action {
                        val path = chooseDirectory("選取輸出路徑")
                        val pathName = "${path?.absolutePath}\\out.txt"
                        print(pathName)
                        val file = File(pathName)
                        file.printWriter().use { out ->
                            points.forEach {
                                out.println(it.out)
                            }
                            vd.lines.forEach {
                                out.println(it.out)
                            }
                        }
                    }
                }
                button("執行") {
                    action {
                        execute()
                    }
                }
                button("Step by step") {
                    action {
                        stepByStep()
                    }
                }
                button("Convex Hull") {
                    action {
                        convexHullEnabled = !convexHullEnabled
                        updatePanel()
                    }
                }
                button("清空畫布") {
                    action {
                        testDataList.clear()
                        clean()
                    }
                }
                button("讀取輸出檔") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("文字文件", "*.txt"))
                        val files: List<File> = chooseFile("選取輸出檔", filters, FileChooserMode.Single)
                        if (files.isNotEmpty())
                            files.first().apply {
                                runAsync {

                                } ui {
                                    clean()
                                    val data = Utils.parseOutputData(this)
                                    outputData.text = "OutputData = "
                                    data.lines.forEach { line ->
                                        groups.add(line.getFxLine())
                                        outputData.text += "\n${line.toString()}"
                                    }
                                    data.points.forEach { point ->
                                        groups.add(point.getCircle())
                                    }
                                    updateInputData()
                                }
                            }
                    }
                }
            }
            label = label {
                minWidth = 100.0
                style {
                    setLabelCss(this)
                }
            }
            inputData = label(INPUT_DATA) {
                minWidth = 100.0
                style {
                    setLabelCss(this)
                }
            }
            outputData = label(outputText) {
                minWidth = 250.0
                style {
                    setLabelCss(this)
                }
            }
        }
    }

    private fun addPoint(evt: MouseEvent) {
        print("${evt.sceneX} ${evt.sceneY}\n")
        runAsync {

        } ui {
            val panelX = evt.sceneX
            val panelY = evt.sceneY
            points.add(Point(panelX, panelY))
            groups.add(points.last().getCircle())
            groups.add(points.last().getLabel())
            updateInputData()
        }
    }


    private fun updateInputData() {
        if (testDataList.size != 0) {
            label.text = "讀檔模式\n目前第 ${currentTestDataIndex + 1} 筆"
            points = testDataList[currentTestDataIndex].points
        } else if (!stepByStepEnabled) {
            label.text = "手動模式"
        }
        inputData.text = INPUT_DATA
        val list = points.sortedWith(compareBy({ it.x }, { it.y }))
        points.clear()
        list.forEach { point ->
            points.add(point)
            inputData.text += "\n${point.toString()} "
        }
        println("updateInputData vdList size = ${vdList.size}")
        if (vdList.size == 1) {
            runAsync {

            } ui {
                outputData.text = OUTPUT_DATA
                vdList.first().lines.forEach {
                    groups.add(it.getFxLine())
                    outputData.text += "\n${it.toString()} "
                }
            }
        }
    }

    private fun execute() {
        stepByStepEnabled = false
        if (points.size == 0) return
        while (vdList.size != 1) {
            stepByStep()
        }
        stepByStep()
    }

    var ranges = arrayOf(1, 2, 4, 9, 18, 37, 75, 150, 300, 600)
    var rangeIndex = 0


    private fun stepByStep() {
        val vdNextList: ArrayList<VoronoiDiagram> = ArrayList()
        if (!stepByStepEnabled) {
            val list = points.sortedWith(compareBy({ it.x }, { it.y }))
            points.clear()
            list.forEachIndexed { index, point ->
                if (index != 0) {
                    if (!(list[index - 1].x == point.x && list[index - 1].y == point.y))
                        points.add(point)
                } else points.add(point)
            }
            vd = VoronoiDiagram(points)
            vdList.addAll(vd.divide())
            stepByStepEnabled = true
            rangeIndex = 0
        }
        var hasConquer = false
        while (!hasConquer && ranges[rangeIndex] != 600) {
            vdNextList.clear()
            var i = 0
            while (i < (vdList.size - 1)) {
                //println("index $i")
                val first = (vdList[i].points.last().x / ranges[rangeIndex]).toInt()
                val second = (vdList[i + 1].points.first().x / ranges[rangeIndex]).toInt()
                //println("first = $first ,second = $second")
                if (second - first <= 1) {
                    vdNextList.add(VoronoiDiagram.conquer(vdList[i], vdList[i + 1]))
                    i += 2
                    hasConquer = true
                } else {
                    vdNextList.add(vdList[i])
                    i++
                }
                if (i == vdList.size - 1)
                    vdNextList.add(vdList[i])
            }
            rangeIndex++
        }
        //println("step by step ranges = ${ranges[rangeIndex]} vdList = ${vdList.size} vdNextList = ${vdNextList.size}\n")
        label.text = "step by step \nranges = ${ranges[rangeIndex]}vdList = ${vdList.size}\n"
        if (vdNextList.size != 0) {
            vdList.clear()
            vdList.addAll(vdNextList)
            //println("vdNextList = ${sumVD(vdNextList)} ${vd.points.size}")
            /*println("index = $rangeIndex")
            vdList.forEachIndexed { index, voronoiDiagram ->
                println("$index -> ${voronoiDiagram.points}")
            }*/
        }
        updatePanel()
    }

    private fun updatePanel() {
        root.clear()
        root += generateForm()
        if (convexHullEnabled) {
            val vd = VoronoiDiagram(points)
            val result = vd.convexHull()
            result.forEachIndexed { index, _ ->
                val line =
                    if (index != result.size - 1)
                        MidLine(result[index], result[index + 1])
                    else MidLine(result[0], result[index])
                groups.add(line.getFxLine())
            }
            result.forEachIndexed { index, point ->
                val line =
                    if (index != result.size - 1)
                        MidLine(result[index], result[index + 1])
                    else MidLine(result[0], result[index])
                groups.add(line.getConvexHullLine())
            }
        }
        if (stepByStepEnabled) {
            vdList.forEachIndexed { index, voronoiDiagram ->
                println("voronoiDiagram.lines ${voronoiDiagram.lines.size}")
                if (voronoiDiagram.lines.isNotEmpty()) groups.add(voronoiDiagram.lines.first().getFxLine())
                val convexHull = voronoiDiagram.convexHull()
                convexHull.forEachIndexed { i, _ ->
                    val line =
                        if (i != convexHull.size - 1)
                            MidLine(convexHull[i], convexHull[i + 1])
                        else MidLine(convexHull[0], convexHull[i])
                    groups.add(line.getConvexHullLine())
                }
                var range = 0
                val lines: ArrayList<MidLine> = ArrayList()
                if (ranges.size - 1 != rangeIndex) {
                    while (range < 600) {
                        range += ranges[rangeIndex]
                        val line = MidLine(
                            Point(range.toDouble(), PANEL_MIN),
                            Point(range.toDouble(), PANEL_MAX)
                        )
                        lines.add(line)
                    }
                    lines.forEachIndexed { i, line ->
                        if (lines.size - 1 != i) {
                            groups.add(line.getDivideLine())
                        }
                    }
                }
                voronoiDiagram.lines.forEach { line ->
                    println("lines ${line.toString()} isVertical = ${line.isVertical()}")
                    groups.add(line.getFxLine())
                }
                voronoiDiagram.points.forEach {
                    //println(it.color)
                    groups.add(it.getCircle())
                    groups.add(it.getLabel())
                }
            }
            if (vdList.size == 1) {
                if (reset == 0) vdList.first().lines.forEach {
                    it.resetColor()
                } else reset = 0
                vdList.first().points.forEach {
                    it.resetColor()
                }
            }
        } else {
            points.forEach { point ->
                groups.add(point.getCircle())
                groups.add(point.getLabel())
            }
        }
        updateInputData()
    }

    var reset = 1

    private fun sumVD(list: ArrayList<VoronoiDiagram>): Int {
        var sum = 0
        list.forEach {
            sum += it.points.size
        }
        return sum
    }

    private fun clean() {
        root.clear()
        root += generateForm()
        points.clear()
        vdList.clear()
        inputData.text = INPUT_DATA
        outputData.text = OUTPUT_DATA
        convexHullEnabled = false
        stepByStepEnabled = false
        reset = 1
    }
}