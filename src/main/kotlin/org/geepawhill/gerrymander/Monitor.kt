package org.geepawhill.gerrymander

interface Monitor {
    fun place(add: Placement)
    fun backtrack(remove: Placement)
    fun reset(ominos: List<Omino>, width: Int, height: Int)
}