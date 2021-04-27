package org.geepawhill.gerrymander

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View("Gerrymandering Game") {

    val orderProperty = SimpleIntegerProperty(3)
    val widthProperty = SimpleIntegerProperty(2)
    val heightProperty = SimpleIntegerProperty(3)
    val countProperty = SimpleIntegerProperty(0)
    val targetProperty = SimpleIntegerProperty(0)

    lateinit var ominos: FlowPane
    override val root = anchorpane {
        background = Background(BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets(3.0)))
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
                    button("Go") {
                        action {
                            updateOminos(orderProperty.value)
                        }
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

    init {
        primaryStage.isMaximized = true
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
            paddingAll = 25.0
            for (coords in omino) {
                rectangle(coords.x * CELL_SIZE + 1, coords.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2) {
                    fill = Color.BLUE
                }
            }
        }
    }

    companion object {
        const val CELL_SIZE = 30.0

        fun anchorAll(node: Node) {
            AnchorPane.setBottomAnchor(node, 0.0)
            AnchorPane.setTopAnchor(node, 0.0)
            AnchorPane.setLeftAnchor(node, 0.0)
            AnchorPane.setRightAnchor(node, 0.0)
        }
    }
}
