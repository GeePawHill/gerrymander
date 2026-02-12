package org.geepawhill.gerrymander


class PlacementLinks(val randoms: RandomWrapper) {
    val map = mutableMapOf<Coords, MutableSet<Placement>>()
    val size get() = map.size

    fun clear() = map.clear()

    fun copy(): PlacementLinks {
        val result = PlacementLinks(RandomWrapper())
        map.flatMap {
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

    fun remove(placement: Placement): Set<Coords> {
        val orphanedCoordinates = mutableSetOf<Coords>()
        for (cell in placement) {
            if (!map.containsKey(cell)) continue
            val set = this[cell]
            set.remove(placement)
            if (set.isEmpty()) {
                orphanedCoordinates += cell
                map.remove(cell)
            }
        }
        return orphanedCoordinates
    }

    fun least(): Placement {
        val fewestPlacements = map.entries.sortedBy { it.value.size }.first().value
        return fewestPlacements.elementAt(randoms.nextInt(fewestPlacements.size))
    }

    operator fun get(coords: Coords): MutableSet<Placement> {
        return map.getOrDefault(coords, mutableSetOf())
    }
}