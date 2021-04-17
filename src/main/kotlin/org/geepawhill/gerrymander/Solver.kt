package org.geepawhill.gerrymander

class Solver(val order: Int, size: Coords) {

    val examined = mutableSetOf<Link>()
    val placements = mutableListOf<Move>()
    val links = mutableListOf<Link>()
    val dead = mutableListOf<Link>()

    fun backtrack() {
        if (placements.isEmpty()) return
        val placement = placements.removeLast()
        if (placements.isEmpty()) examined += placement.links
        else placements.last().examined += placement.links
        placement.examined.forEach { links += it }
        placement.collisions.forEach { links += it }
    }

    fun backtrackIfNeeded(): Boolean {
        while (links.isEmpty() && placements.isNotEmpty()) {
            backtrack()
        }
        return links.isNotEmpty()
    }
}