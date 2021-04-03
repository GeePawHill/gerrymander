package org.geepawhill.gerrymander

import tornadofx.View
import tornadofx.label
import tornadofx.stackpane

class MainView : View("My View") {
    override val root = stackpane {
        label {
            text = "Test"
        }
    }
}
