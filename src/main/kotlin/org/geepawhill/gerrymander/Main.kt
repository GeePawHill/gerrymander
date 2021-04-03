package org.geepawhill.gerrymander

import tornadofx.App
import tornadofx.launch

class Main : App(MainView::class, Styles::class)

fun main() {
    launch<Main>()
}