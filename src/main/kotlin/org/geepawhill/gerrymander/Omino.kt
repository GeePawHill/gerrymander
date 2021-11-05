package org.geepawhill.gerrymander

typealias Placement = Set<Coords>

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

    fun placement(grid: Coords, at: Coords): Placement {
        val xlate = Coords(at.x - first().x, at.y)
        return map { it.translate(xlate) }.filter { it.x >= 0 && it.y >= 0 && it.x < grid.x && it.y < grid.y }.toSet()
    }

    fun placements(grid: Coords): Set<Placement> {
        return (0 until grid.x).flatMap { x ->
            (0 until grid.y).map { y ->
                placement(grid, Coords(x, y))
            }
        }.filter { size == it.size }.toSet()
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