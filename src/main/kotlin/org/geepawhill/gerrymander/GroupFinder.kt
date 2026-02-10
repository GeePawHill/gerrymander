package org.geepawhill.gerrymander

class GroupFinder {
    val working = mutableListOf<Coords>()
    val groups = mutableListOf<MutableSet<Coords>>()

    fun find(emptys: List<Coords>): List<MutableSet<Coords>> {
        working.clear()
        working.addAll(emptys)
        groups.clear()
        while (working.isNotEmpty()) {
            groups.add(mutableSetOf())
            findGroup(working.first())
        }
        return groups
    }

    fun findGroup(cell: Coords) {
        working.remove(cell)
        groups.last().add(cell)
        cell.neighbors
            .filter { working.contains(it) }
            .forEach {
                findGroup(it)
            }
    }
}