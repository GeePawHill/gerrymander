package org.geepawhill.gerrymander

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHEAST,
    SOUTHEAST,
    NORTHWEST,
    SOUTHWEST;

    companion object {
        val orthogonals = setOf(NORTH, SOUTH, EAST, WEST)
    }
}

