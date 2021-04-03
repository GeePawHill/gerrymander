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

fun Direction.opposite(): Direction = when (this) {
    Direction.NORTH -> Direction.SOUTH
    Direction.SOUTH -> Direction.NORTH
    Direction.EAST -> Direction.WEST
    Direction.WEST -> Direction.EAST
    Direction.NORTHEAST -> Direction.SOUTHWEST
    Direction.NORTHWEST -> Direction.SOUTHEAST
    Direction.SOUTHWEST -> Direction.NORTHEAST
    Direction.SOUTHEAST -> Direction.NORTHWEST
}

fun Direction.neighbors(): Pair<Direction, Direction> = when (this) {
    Direction.SOUTH, Direction.NORTH -> Pair(Direction.EAST, Direction.WEST)
    Direction.EAST, Direction.WEST -> Pair(Direction.SOUTH, Direction.NORTH)
    Direction.NORTHEAST, Direction.SOUTHWEST -> Pair(Direction.NORTHWEST, Direction.SOUTHEAST)
    Direction.NORTHWEST, Direction.SOUTHEAST -> Pair(Direction.SOUTHWEST, Direction.NORTHEAST)
}

