package org.geepawhill.gerrymander

class Solver(val order: Int, size: Coords) {

    val placements = mutableListOf<Placement>()
    val links = mutableListOf<Link>()
    val dead = mutableListOf<Link>()

    fun backtrack() {
        if (placements.isEmpty()) return
        val placement = placements.removeLast()
        placement.collisions.forEach { links += it }
        placement.links.forEach { dead += it }
    }
}