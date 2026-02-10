package org.geepawhill.gerrymander

class GroupFinder {
    fun find(emptys: List<Coords>): List<MutableSet<Coords>> {
        val working = emptys.toMutableList()
        val groups = mutableListOf<MutableSet<Coords>>()
        while (working.isNotEmpty()) {
            groups.add(findGroup(working))
        }
        return groups
    }

    private fun findGroup(working: MutableList<Coords>): MutableSet<Coords> {
        return findGroup(working, working.first())
    }

    fun findGroup(working: MutableList<Coords>, cell: Coords): MutableSet<Coords> {
        val result = mutableSetOf<Coords>()
        working.remove(cell)
        result.add(cell)
        cell.neighbors
            .filter { working.contains(it) }
            .forEach {
                result.add(it)
                working.remove(it)
                result.addAll(findGroup(working, it))
            }
        return result
    }

}