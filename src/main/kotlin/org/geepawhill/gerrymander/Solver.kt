package org.geepawhill.gerrymander

import tornadofx.*
import kotlin.random.Random

class Solver(val randoms: Random) {
    val moves = observableListOf<Move>()
    val links = PlacementLinks(randoms)
    val orphanedCoordinates = mutableSetOf<Coords>()

    val examined = mutableSetOf<Placement>()
    val isSolved get() = moves.size == target
    val needsBacktrack get() = orphanedCoordinates.isNotEmpty()

    var target = Int.MAX_VALUE

    fun prepare(order: Int, width: Int, height: Int) {
        if ((width * height) % order != 0) throw IllegalArgumentException("Order and width and height don't work.")
        target = (width * height) / order
        moves.clear()
        examined.clear()
        links.clear()
        orphanedCoordinates.clear()
        val size = Coords(width, height)
        Omino.fixed(order).forEach { omino ->
            omino.placements(size).forEach { placement ->
                links.add(placement)
            }
        }
    }

    fun step() {
        if (needsBacktrack) backtrack()
        else if (isSolved) return
        else move()
    }

    fun move() {
        if (moves.isEmpty()) move(pick())
        else move(pickLeast())
    }

    fun pick(): Placement = links.random()

    fun pickLeast(): Placement = links.least()

    fun backtrack() {
        if (moves.isEmpty()) return
        val move = moves.removeLast()
        for (placement in move.collisions) links.add(placement)
        for (placement in move.examined) links.add(placement)
        if (moves.isEmpty()) examined.add(move.placement)
        else moves.last().examined.add(move.placement)
        orphanedCoordinates.clear()
        for (cell in move.placement) {
            if (links[cell].isEmpty()) orphanedCoordinates += cell
        }
    }

    fun move(placement: Placement): Move {
        // remove this placement from all cells
        // Ignore orphans
        links.remove(placement)
        val toRemove = findCollisions(placement)
        val collisions = mutableSetOf<Placement>()
        // remove these cells
        for (cell in placement) links.remove(cell)
        // remove the collisions, notice if we empty a cell
        for (collision in toRemove) {
            orphanedCoordinates += links.remove(collision)
            collisions += collision
        }
        val result = Move(placement, collisions)
        moves += result
        return result
    }

    private fun findCollisions(placement: Placement): MutableSet<Placement> {
        val toRemove = mutableSetOf<Placement>()
        // find every placement that touches one of these cells
        for (cell in placement) {
            for (collision in links[cell]) {
                toRemove += collision
            }
        }
        return toRemove
    }

}