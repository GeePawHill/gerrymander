package org.geepawhill.gerrymander

data class Omino private constructor(private val base: MutableSet<Coords> = mutableSetOf()) : Set<Coords> by base {

    companion object {
        operator fun invoke(): Omino {
            return Omino()
        }

        operator fun invoke(coords: Set<Coords>): Omino {
            val leastX = coords.minByOrNull { it.x }!!.x
            val leastY = coords.minByOrNull { it.y }!!.y
            val normalized = coords.map { Coords(it.x - leastX, it.y - leastY) }
            val sorted = normalized.toSortedSet(Coords.comparator)
            return Omino(sorted)
        }

        operator fun invoke(vararg coords: Coords): Omino = invoke(coords.toSet())
        operator fun invoke(base: Omino, vararg coords: Coords) = invoke(base + coords)
    }
}