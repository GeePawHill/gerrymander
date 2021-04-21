package org.geepawhill.gerrymander

class PlacementMap {
    val map = mutableMapOf<Coords, MutableSet<Placement>>()
    val size get() = map.size

    fun add(placement: Placement) {
        for (coords in placement) {
            val dest = map.getOrPut(coords) { mutableSetOf() }
            dest += placement
        }
    }

    operator fun get(coords: Coords): Set<Placement> {
        return map.getOrDefault(coords, mutableSetOf())
    }
}