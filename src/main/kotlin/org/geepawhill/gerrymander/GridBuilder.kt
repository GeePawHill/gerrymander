package org.geepawhill.gerrymander

class GridBuilder {
    fun emptys(vararg strings: String): List<Coords> {
        val result = mutableListOf<Coords>()
        strings.forEachIndexed { y, string ->
            string.forEachIndexed { x, c ->
                when (c) {
                    '*' -> {}
                    else -> result.add(Coords(x, y))
                }
            }
        }
        return result
    }
}