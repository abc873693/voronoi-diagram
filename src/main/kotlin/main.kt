package voronoiDiagram

import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import tornadofx.View
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import tornadofx.*
import voronoiDiagram.libs.Utils
import voronoiDiagram.models.*
import java.io.File


fun main(args: Array<String>) {
    launch(MainApp::class.java)
}

class MainApp : App(HomePage::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            translateX = Dimension(1.0,Dimension.LinearUnits.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

private const val INPUT_DATA = "InputData"
private const val OUTPUT_DATA = "OutputData"

class HomePage : View() {

    private lateinit var label: Label
    lateinit var inputData: Label
    lateinit var outputData: Label
    lateinit var groups: Group
    private var testDataList: ArrayList<TestData> = ArrayList()
    private var currentTestDataIndex = 0
    private var points: ArrayList<Point> = ArrayList()
    private var lines: ArrayList<Line> = ArrayList()

    private var convexHullEnabled = false

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
            stackpane {
                groups = group {
                    rectangle {
                        fill = Color.WHITE
                        width = 600.0
                        height = 600.0
                    }
                }
                val canvas = canvas {
                    height = 600.0
                    width = 600.0
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
                            lines.forEach {
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
                                    points = data.points
                                    lines = data.lines
                                    outputData.text = "OutputData = "
                                    lines.forEach { line ->
                                        groups.add(line.getFxLine)
                                        outputData.text += "\n${line.toString()}"
                                    }
                                    points.forEach { point ->
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
            outputData = label(OUTPUT_DATA) {
                minWidth = 200.0
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
        } else {
            label.text = "手動模式"
        }
        inputData.text = INPUT_DATA
        points.forEach { point ->
            inputData.text += "\n${point.toString()} "
        }
    }

    private fun execute() {
        Utils.sortPoints(points)
        val vd = VoronoiDiagram3Point(points)
        vd.execute()
        outputData.text = OUTPUT_DATA
        vd.lines.forEach {
            groups.add(it.getFxLine)
            outputData.text += "\n${it.toString()} "
        }
        lines = vd.lines
        inputData.text = INPUT_DATA
        points.forEach { point ->
            inputData.text += "\n${point.toString()} "
        }
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
                        Line(result[index], result[index + 1])
                    else Line(result[0], result[index])
                groups.add(line.getFxLine)
            }
            result.forEachIndexed { index, point ->
                val line =
                    if (index != result.size - 1)
                        Line(result[index], result[index + 1])
                    else Line(result[0], result[index])
                groups.add(line.getFxLine)
            }
        }
        points.forEach { point ->
            groups.add(point.getCircle())
            groups.add(point.getLabel())
        }
        updateInputData()
    }

    private fun clean() {
        root.clear()
        root += generateForm()
        points.clear()
        inputData.text = INPUT_DATA
        outputData.text = OUTPUT_DATA
    }
}