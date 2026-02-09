package org.geepawhill.gerrymander

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*
import kotlin.random.Random

class MainView : View("Gerrymandering Game"), Monitor {

    val solver = Solver(Random(0), this)
    val orderProperty = SimpleIntegerProperty(6)
    val widthProperty = SimpleIntegerProperty(32)
    val heightProperty = SimpleIntegerProperty(18)
    val countProperty = SimpleIntegerProperty(0)
    val solutionMap = mutableMapOf<Coords, StackPane>()
    val moves = mutableListOf<Placement>()

    val solvedProperty = SimpleStringProperty("")
    val stepCountProperty = SimpleIntegerProperty(0)

    lateinit var ominos: FlowPane
    lateinit var solution: VBox

    override val root = anchorpane {
        hgrow = Priority.ALWAYS
        borderpane {
            anchorAll(this)
            left = controlsView()
            center = gridView()
        }
    }

    private fun gridView(): VBox = vbox {
        vbox {
            solution = this
        }
        hbox {
            label(solvedProperty)
            label(stepCountProperty)
        }
    }

    private fun stepAction() {
        solver.step()
    }

    private fun runAction() {
        layout(orderProperty.value, widthProperty.value, heightProperty.value)
        val fixedPlus = mutableSetOf<Omino>()
        fixedPlus.addAll(Omino.fixed(1))
        val fixed = Omino.fixed(6)
        fixedPlus.addAll(fixed)
        moves.clear()
        runAsync {
            solver.run(
                fixedPlus,
                widthProperty.value,
                heightProperty.value
            )
        } ui {
            if (solver.isSolved) solvedProperty.value = "Solved it!"
            else solvedProperty.value = "Insoluble"

        }
    }

    private fun controlsView(): Form = form {
        fieldset("Layout") {
            hbox {
                button("Layout") {
                    action {
                        layout(orderProperty.value, widthProperty.value, heightProperty.value)
                    }
                }
                button("Run") {
                    action {
                        runAction()
                    }
                }
                button("Step") {
                    action {
                        stepAction()
                    }
                }
            }
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

    init {
        primaryStage.isMaximized = true
    }

    fun layout(order: Int, width: Int, height: Int) {
        val districts = (width * height) / order
        adjustColors(districts)
        updateOminos(orderProperty.value)
        solver.reset(Omino.fixed(orderProperty.value), width, height)
        updateGrid(width, height)
    }

    fun adjustColors(districts: Int) {
        while (colors.size < districts) {
            val color = Color(
                Random.nextDouble(),
                Random.nextDouble(),
                Random.nextDouble(),
                1.0
            )
            colors += color
        }
    }

    fun updateGrid(width: Int, height: Int) {
        solutionMap.clear()
        with(solution) {
            children.clear()
            for (row in 0 until height) {
                hbox {
                    for (column in 0 until width) {
                        stackpane {
                            solutionMap[Coords(column, row)] = this
                            rectangle(
                                SOLUTION_SIZE * column,
                                SOLUTION_SIZE * row,
                                SOLUTION_SIZE - 2,
                                SOLUTION_SIZE - 2
                            ) {
                                fill = Color.DARKGRAY
                                stroke = Color.WHITE
                            }
                            label("")
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

    override fun place(add: Placement) {
        runLater {
            moves.add(add)
            stepCountProperty += 1
            add.forEach { coords ->
                with(solutionMap[coords]!!) {
                    with(children.first() as Rectangle) {
                        fill = getColor(moves.size - 1)
                    }
                    with(children.last() as Label) {
                        text = (moves.size - 1).toString()
                    }
                }
            }

        }
    }

    override fun backtrack(remove: Placement) {
        runLater {
            moves.remove(remove)
            stepCountProperty += 1
            remove.forEach { coords ->
                with(solutionMap[coords]!!) {
                    with(children.first() as Rectangle) {
                        fill = Color.WHITE
                    }
                    with(children.last() as Label) {
                        text = "X"
                    }
                }
            }

        }
    }

    override fun reset(
        ominos: List<Omino>,
        width: Int,
        height: Int
    ) {

    }

    companion object {
        const val CELL_SIZE = 20.0
        const val SOLUTION_SIZE = 40.0
        val colors = mutableListOf(
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.CORAL,
            Color.DARKSEAGREEN,
            Color.ORANGE
        )

        fun getColor(index: Int): Color {
            while (index >= colors.size) {
                val color = Color(
                    Random.nextDouble(),
                    Random.nextDouble(),
                    Random.nextDouble(),
                    1.0
                )
                colors += color
            }
            return colors[index]
        }

        fun anchorAll(node: Node) {
            AnchorPane.setBottomAnchor(node, 0.0)
            AnchorPane.setTopAnchor(node, 0.0)
            AnchorPane.setLeftAnchor(node, 0.0)
            AnchorPane.setRightAnchor(node, 0.0)
        }
    }
}
