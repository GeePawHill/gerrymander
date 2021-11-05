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

    fun run(order: Int, width: Int, height: Int): List<Move> {
        prepare(order, width, height)
        while (!isSolved) step()
        return moves
    }

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
        restoreExcludedPlacements(move)
        addPlacementToExamined(move)
        resetOrphanedCoordinates(move)
    }

    private fun resetOrphanedCoordinates(move: Move) {
        orphanedCoordinates.clear()
        for (cell in move.placement) {
            if (links[cell].isEmpty()) orphanedCoordinates += cell
        }
    }

    private fun addPlacementToExamined(move: Move) {
        if (moves.isEmpty()) examined.add(move.placement)
        else moves.last().examined.add(move.placement)
    }

    private fun restoreExcludedPlacements(move: Move) {
        for (placement in move.collisions) links.add(placement)
        for (placement in move.examined) links.add(placement)
    }

    fun move(placement: Placement): Move {
        links.remove(placement)
        val collisions = findCollisions(placement)
        for (cell in placement) links.remove(cell)
        collisions.forEach {
            orphanedCoordinates += links.remove(it)
        }
        val result = Move(placement, collisions)
        moves += result
        return result
    }

    private fun findCollisions(placement: Placement): Set<Placement> {
        val collisions = mutableSetOf<Placement>()
        for (cell in placement) {
            for (collision in links[cell]) {
                collisions += collision
            }
        }
        return collisions
    }

}