package org.geepawhill.gerrymander

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*

class MainView : View("Gerrymandering Game") {

    val solver = Solver()
    val orderProperty = SimpleIntegerProperty(3)
    val widthProperty = SimpleIntegerProperty(2)
    val heightProperty = SimpleIntegerProperty(3)
    val countProperty = SimpleIntegerProperty(0)
    val targetProperty = SimpleIntegerProperty(0)
    val solutionMap = mutableMapOf<Coords, Rectangle>()

    lateinit var ominos: FlowPane
    lateinit var solution: VBox

    override val root = anchorpane {
//        background = Background(BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets(3.0)))
        hgrow = Priority.ALWAYS
        borderpane {
            anchorAll(this)
            left = form {
                fieldset("Layout") {
                    field("Order") {
                        textfield(orderProperty)
                    }
                    field("Width") {
                        textfield(widthProperty)
                    }
                    field("Height") {
                        textfield(heightProperty)
                    }
                    field("Ominos") {
                        label(countProperty)
                    }
                    field("Target") {
                        label(targetProperty)
                    }
                    button("Layout") {
                        action {
                            layout(orderProperty.value, widthProperty.value, heightProperty.value)
                        }
                    }
                }
                scrollpane {
                    flowpane {
                        anchorAll(this)
                        ominos = this
                    }
                }
            }
            center = vbox {
                vbox {
                    solution = this
                }
                stackpane {
                    alignment = Pos.CENTER
                    button("Step") {
                        action {
                            solver.step()
                            for (entry in solutionMap) {
                                with(entry.value) {
                                    fill = Color.DARKGRAY
                                }
                            }

                            solver.moves.withIndex().forEach {
                                for (coords in it.value.placement) {
                                    with(solutionMap[coords]!!) {
                                        fill = COLORS[it.index]
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        primaryStage.isMaximized = true
    }

    fun layout(order: Int, width: Int, height: Int) {
        if (((width * height) % order) != 0) {
            alert(
                Alert.AlertType.ERROR,
                "Invalid Layout",
                "The order ($order) doesn't partition the grid ($height X $width)"
            )
            return
        }
        updateOminos(orderProperty.value)
        solver.prepare(order, width, height)
        updateGrid(width, height)
    }

    fun updateGrid(width: Int, height: Int) {
        solutionMap.clear()
        with(solution) {
            children.clear()
            for (row in 0 until height) {
                hbox {
                    for (column in 0 until width) {
                        rectangle(SOLUTION_SIZE * column, SOLUTION_SIZE * row, SOLUTION_SIZE - 2, SOLUTION_SIZE - 2) {
                            solutionMap[Coords(column, row)] = this
                            fill = Color.DARKGRAY
                            stroke = Color.WHITE
                        }
                    }
                }
            }
        }
    }

    fun updateOminos(order: Int) {
        val all = Omino.fixed(order)
        countProperty.value = all.size
        with(ominos) {
            children.clear()
            for (omino in all) {
                makeOminoView(order, omino)
            }
        }
    }

    fun FlowPane.makeOminoView(order: Int, omino: Omino) {
        pane {
            paddingAll = 10.0
            for (coords in omino) {
                rectangle(coords.x * CELL_SIZE + 1, coords.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2) {
                    fill = Color.BLUE
                }
            }
        }
    }

    companion object {
        const val CELL_SIZE = 20.0
        const val SOLUTION_SIZE = 40.0
        val COLORS = listOf(
            Color.LAVENDER,
            Color.RED,
            Color.CHOCOLATE,
            Color.CORAL,
            Color.DARKSEAGREEN,
            Color.ORANGE
        )

        fun anchorAll(node: Node) {
            AnchorPane.setBottomAnchor(node, 0.0)
            AnchorPane.setTopAnchor(node, 0.0)
            AnchorPane.setLeftAnchor(node, 0.0)
            AnchorPane.setRightAnchor(node, 0.0)
        }
    }
}
