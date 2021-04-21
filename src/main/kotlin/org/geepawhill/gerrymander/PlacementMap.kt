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