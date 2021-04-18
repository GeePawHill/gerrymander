package org.geepawhill.gerrymander

import com.google.common.collect.ArrayListMultimap

class Solver {

    val cellToPlacement = ArrayListMultimap.create<Coords, Link>()

    fun solve(order: Int, size: Coords) {

    }

    val moves = mutableListOf<Move>()
    val examined = mutableSetOf<Link>()
    val links = mutableListOf<Link>()

    fun backtrack() {
        if (moves.isEmpty()) return
        val placement = moves.removeLast()
        if (moves.isEmpty()) examined += placement.links
        else moves.last().examined += placement.links
        placement.examined.forEach { links += it }
        placement.collisions.forEach { links += it }
    }

    fun backtrackIfNeeded(): Boolean {
        while (links.isEmpty() && moves.isNotEmpty()) {
            backtrack()
        }
        return links.isNotEmpty()
    }
}