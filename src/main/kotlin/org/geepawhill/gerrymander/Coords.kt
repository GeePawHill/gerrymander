package org.geepawhill.gerrymander

data class Coords(val x: Int, val y: Int) {

    val neighbors
        get() = arrayOf(
            get(Direction.NORTH),
            get(Direction.EAST),
            get(Direction.SOUTH),
            get(Direction.WEST)
        )

    operator fun get(direction: Direction) =
        when (direction) {
            Direction.NORTH -> Coords(x, y - 1)
            Direction.SOUTH -> Coords(x, y + 1)
            Direction.EAST -> Coords(x + 1, y)
            Direction.WEST -> Coords(x - 1, y)
            Direction.NORTHEAST -> Coords(x + 1, y - 1)
            Direction.NORTHWEST -> Coords(x - 1, y - 1)
            Direction.SOUTHEAST -> Coords(x + 1, y + 1)
            Direction.SOUTHWEST -> Coords(x - 1, y + 1)
        }

    fun translate(to: Coords): Coords = Coords(x + to.x, y + to.y)

    override fun toString(): String = "($x,$y)"

    companion object {
        val comparator = compareBy<Coords> { it.x }.thenBy { it.y }
    }
}