package org.geepawhill.gerrymander

data class Omino private constructor(private val base: Set<Coords> = setOf()) : Set<Coords> by base {

    fun expand(): Set<Omino> {
        val result = mutableSetOf<Omino>()
        for (coord in this) {
            for (neighbor in coord.neighbors) {
                if (contains(neighbor)) continue
                result += Omino(this, neighbor)
            }
        }
        return result
    }

    fun links(grid: Coords, at: Coords): Set<Coords> {
        val xlate = Coords(at.x - first().x, at.y)
        val result = map { it.translate(xlate) }.filter { it.x >= 0 && it.y >= 0 && it.x < grid.x && it.y < grid.y }
        return if (size == result.size) result.toSet()
        else setOf()
    }

    fun ascii() {
        println()
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (contains(Coords(x, y))) print("*")
                else print(" ")
            }
            println()
        }
    }

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

        fun fixed(order: Int): Set<Omino> {
            if (order == 1) return setOf(Omino(Coords(0, 0)))
            val result = mutableSetOf<Omino>()
            fixed(order - 1).forEach { result += it.expand() }
            return result
        }
    }
}