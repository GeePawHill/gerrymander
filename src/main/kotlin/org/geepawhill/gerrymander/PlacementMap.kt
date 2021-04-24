package org.geepawhill.gerrymander

import java.util.*


class PlacementMap {
    val map = mutableMapOf<Coords, MutableSet<Placement>>()
    val randoms = Random()
    val size get() = map.size

    fun clear() = map.clear()

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

    fun remove(placement: Placement, emptied: MutableSet<Coords>) {
        for (cell in placement) {
            val set = this[cell]
            set.remove(placement)
            if (set.isEmpty()) {
                emptied += cell
                map.remove(cell)
            }
        }
    }

    operator fun get(coords: Coords): MutableSet<Placement> {
        return map.getOrDefault(coords, mutableSetOf())
    }
}