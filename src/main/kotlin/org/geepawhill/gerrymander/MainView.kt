package org.geepawhill.gerrymander

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View("Gerrymandering Game") {

    val orderProperty = SimpleIntegerProperty(5)

    lateinit var ominos: FlowPane
    override val root = anchorpane {
        tabpane {
            anchorAll(this)
            background = Background(BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets(3.0)))
            hgrow = Priority.ALWAYS
            tab("Ominoes") {
                borderpane {
                    left = form {
                        textfield(orderProperty)
                        button("Go") {
                            action {
                                updateOminos(orderProperty.value)
                            }
                        }
                    }
                    center = anchorpane {
                        flowpane {
                            anchorAll(this)
                            label("Ominos here")
                            ominos = this
                        }
                    }
                }
            }
        }
    }

    fun updateOminos(order: Int) {
        val all = Omino.fixed(order)
        with(ominos) {
            children.clear()
            for (omino in all) {
                makeOminoView(order, omino)
            }
        }
    }

    fun FlowPane.makeOminoView(order: Int, omino: Omino) {
        pane {
            paddingAll = 5.0
            for (coords in omino) {
                rectangle(coords.x * CELL_SIZE + 1, coords.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2) {
                    fill = Color.BLUE
                }
            }
        }
    }

    companion object {
        const val CELL_SIZE = 20.0

        fun anchorAll(node: Node) {
            AnchorPane.setBottomAnchor(node, 0.0)
            AnchorPane.setTopAnchor(node, 0.0)
            AnchorPane.setLeftAnchor(node, 0.0)
            AnchorPane.setRightAnchor(node, 0.0)
        }
    }
}
