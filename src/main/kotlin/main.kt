package javafx.test

import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.View
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import javafx.test.libs.Utils
import javafx.test.models.TestData
import tornadofx.*
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

class HomePage : View() {

    val BASE_X = 10
    val BASE_Y = 100

    lateinit var label: Label
    private lateinit var groups: Group
    private lateinit var stackpane: StackPane
    private var testDataList: ArrayList<TestData> = ArrayList()

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
                                        print(testData.toString())
                                    }
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
                    backgroundColor = MultiValue()
                    backgroundColor += c("#cecece", 0.0)
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
                    print("${evt.sceneX} ${evt.sceneY}\n")
                    runAsync {

                    } ui { loadedText ->
                        groups.add(circle {
                            centerX = evt.sceneX - BASE_X
                            centerY = evt.sceneY - BASE_Y
                            radius = 0.5
                        })
                    }
                }
            }
        }
    }

    private fun execute() {


    }

    private fun clean() {
        this@HomePage.stackpane.clear()
        this@HomePage.stackpane = generatePanel()
    }

    private lateinit var backgroundRectangle: Rectangle

    private fun generatePanel(): StackPane {
        return stackpane {
            groups = group {
                backgroundRectangle = rectangle {
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
                print("${evt.sceneX} ${evt.sceneY}\n")
                runAsync {

                } ui { loadedText ->
                    print("stackpane layout ${stackpane.layoutX} ${stackpane.layoutY}\n")
                    print("stackpane scale ${stackpane.scaleX} ${stackpane.scaleY}\n")
                    groups.add(circle {
                        centerX = evt.sceneX - backgroundRectangle.layoutX
                        centerY = evt.sceneY - backgroundRectangle.layoutY
                        radius = 0.5
                    })
                }
            }
        }
    }
}