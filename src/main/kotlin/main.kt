package javafx.test

import javafx.application.Application.launch
import javafx.scene.paint.Color
import tornadofx.View
import tornadofx.hbox
import javafx.scene.text.FontWeight
import tornadofx.*
import kotlin.math.sin


fun main(args: Array<String>) {
    launch(HelloWorldApp::class.java)
}

class HelloWorldApp : App(HelloWorld::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

class HelloWorld : View() {
    override val root = stackpane {
        val canvas = canvas {
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