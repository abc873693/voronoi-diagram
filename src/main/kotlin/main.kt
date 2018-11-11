package javafx.test

import javafx.application.Application.launch
import javafx.scene.control.Label
import javafx.scene.paint.Color
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

    lateinit var label: Label
    var testDataList: ArrayList<TestData> = ArrayList()

    override val root = form {
        vbox(20) {
            hbox(20) {
                button("開啟檔案") {
                    action {
                        val filters = arrayOf(FileChooser.ExtensionFilter("文字文件", "*.txt"))
                        val files: List<File> = chooseFile("選取輸入檔", filters, FileChooserMode.Single)
                        files.first().apply {
                            runAsync {

                            } ui {
                                label.text = this.absolutePath
                                /*this.forEachLine { text ->
                                    if (text.isNotEmpty() && text.first() != '#') {
                                        splitNumbers.addAll(Utils.splitData(text))
                                    }
                                }*/
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

                    }
                }
                button("清空畫布") {
                    action {

                    }
                }
            }
            label = label {
                style {
                    backgroundColor = MultiValue()
                    backgroundColor += c("#cecece", 0.0)
                }
            }
            val canvas = canvas {
                layoutY = 100.0
                group {
                    line {
                        startX = 600.0
                        startY = 100.0
                        endX = 200.0
                        endY = 300.0
                    }
                    height = 600.0
                    width = 600.0
                }
            }
            canvas.setOnMouseClicked { evt ->
                print("${evt.sceneX} ${evt.sceneY}\n")
                canvas.add(
                    rectangle {
                        fill = Color.BLUE
                        width = 2.0
                        height = 2.0
                        arcWidth = 20.0
                        arcHeight = 20.0
                        x = evt.sceneX
                        y = evt.sceneY
                    }
                )
                runAsync {

                } ui { loadedText ->
                    canvas.add(
                        rectangle {
                            fill = Color.BLUE
                            width = 2.0
                            height = 2.0
                            arcWidth = 20.0
                            arcHeight = 20.0
                            x = evt.sceneX
                            y = evt.sceneY
                        }
                    )
                }
            }
        }
    }
}