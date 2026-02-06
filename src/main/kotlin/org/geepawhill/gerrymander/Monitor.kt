package org.geepawhill.gerrymander

interface Monitor {
    fun place(add: Placement)
    fun backtrack(remove: Placement)
}