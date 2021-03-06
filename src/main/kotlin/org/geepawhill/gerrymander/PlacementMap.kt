package org.geepawhill.gerrymander

import java.util.*


class PlacementMap {
    val map = mutableMapOf<Coords, MutableSet<Placement>>()
    val randoms = Random()
    val size get() = map.size

    fun clear() = map.clear()

    fun copy(): PlacementMap {
        val result = PlacementMap()
        val placements = map.flatMap {
            it.value
        }.toSet().forEach { result.add(it) }
        return result
    }

    fun add(placement: Placement) {
        for (coords in placement) {
            val dest = map.getOrPut(coords) { mutableSetOf() }
            dest += placement
        }
    }

    fun random(): Placement {
        val keys = map.keys
        val key = keys.elementAt(randoms.nextInt(keys.size))
        val placements = map[key]!!
        return placements.elementAt(randoms.nextInt(placements.size))
    }

    fun remove(cell: Coords) {
        map.remove(cell)
    }

    fun remove(placement: Placement, emptied: MutableSet<Coords>) {
        for (cell in placement) {
            if (!map.containsKey(cell)) continue
            val set = this[cell]
            set.remove(placement)
            if (set.isEmpty()) {
                emptied += cell
                map.remove(cell)
            }
        }
    }

    fun least(): Placement {
        return map.entries.sortedBy { it.value.size }.first().value.first()
    }

    operator fun get(coords: Coords): MutableSet<Placement> {
        return map.getOrDefault(coords, mutableSetOf())
    }
}