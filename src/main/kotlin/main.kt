package voronoiDiagram

import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.View
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import tornadofx.*
import voronoiDiagram.libs.Utils
import voronoiDiagram.models.Point
import voronoiDiagram.models.TestData
import java.io.File


fun main(args: Array<String>) {
    launch(MainApp::class.java)
}

class MainApp : App(HomePage::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

private const val BASE_X = 10.0
private const val BASE_Y = 180.0

class HomePage : View() {

    private lateinit var label: Label
    lateinit var inputData: Label
    lateinit var outputData: Label
    lateinit var groups: Group
    private lateinit var stackpane: StackPane
    private var testDataList: ArrayList<TestData> = ArrayList()
    private var points: ArrayList<Point> = ArrayList()

    init {
    }

    private fun setLabelCss(inlineCss: InlineCss) {
        inlineCss.apply {
            backgroundColor = MultiValue()
            backgroundColor += c("#cecece", 0.0)
            fontSize = Dimension(16.0, Dimension.LinearUnits.px)
        }
    }

    override val root = form {
        vbox(20) {
            hbox(20) {
                button("開啟檔案") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("文字文件", "*.txt"))
                        val files: List<File> = chooseFile("選取輸入檔", filters, FileChooserMode.Single)
                        if (files.isNotEmpty())
                            files.first().apply {
                                runAsync {

                                } ui {
                                    label.text = this.absolutePath
                                    testDataList = Utils.parseData(this)
                                    testDataList.forEach { testData ->

                                    }
                                    points = testDataList.first().points
                                    updateInputData()
                                }
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
                            out.println("First line")
                            out.println("Second line")
                        }
                    }
                }
                button("執行") {
                    action {
                        execute()
                    }
                }
                button("清空畫布") {
                    action {
                        clean()
                    }
                }
            }
            label = label {
                style {
                    setLabelCss(this)
                }
            }
            inputData = label("InputData = ") {
                style {
                    setLabelCss(this)
                }
            }
            outputData = label("OutputData = ") {
                style {
                    setLabelCss(this)
                }
            }
            stackpane = stackpane {
                groups = group {
                    rectangle {
                        fill = Color.WHITE
                        width = 600.0
                        height = 600.0
                    }
                    line {
                        startX = 600.0
                        startY = 100.0
                        endX = 200.0
                        endY = 300.0
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
        }
    }

    private fun addPoint(evt: MouseEvent) {
        print("${evt.sceneX} ${evt.sceneY}\n")
        runAsync {

        } ui {
            val panelX = evt.sceneX - BASE_X
            val panelY = evt.sceneY - BASE_Y
            points.add(Point(panelX, panelY))
            groups.add(Circle(panelX, panelY, 0.5))
            updateInputData()
        }
    }


    private fun updateInputData() {
        inputData.text = "InputData = "
        points.forEach { point ->
            inputData.text += "${point.toString()} "
        }
    }

    private fun execute() {

    }

    private fun clean() {
        this@HomePage.stackpane.clear()
        this@HomePage.stackpane = generatePanel()
        points.clear()
        inputData.text = "InputData = "
        outputData.text = "OutputData = "
    }

    private fun generatePanel(): StackPane {
        return stackpane {
            layoutX = BASE_X
            layoutX = BASE_Y
            groups = group {
                rectangle {
                    fill = Color.WHITE
                    width = 600.0
                    height = 600.0
                }
                line {
                    startX = 600.0
                    startY = 100.0
                    endX = 200.0
                    endY = 300.0
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
    }
}