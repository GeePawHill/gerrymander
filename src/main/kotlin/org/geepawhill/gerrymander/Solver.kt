package org.geepawhill.gerrymander

import tornadofx.*
import kotlin.random.Random

class Solver(val randoms: Random) {
    val moves = observableListOf<Move>()
    val map = PlacementMap(randoms)
    val newlyEmptied = mutableSetOf<Coords>()

    val examined = mutableSetOf<Placement>()
    var target = Int.MAX_VALUE
    val isSolved get() = moves.size == target

    fun prepare(order: Int, width: Int, height: Int) {
        if ((width * height) % order != 0) throw IllegalArgumentException("Order and width and height don't work.")
        target = (width * height) / order
        moves.clear()
        examined.clear()
        map.clear()
        newlyEmptied.clear()
        val size = Coords(width, height)
        Omino.fixed(order).forEach { omino ->
            omino.placements(size).forEach { placement ->
                map.add(placement)
            }
        }
        map.dump()
    }

    fun step() {
        if (needsBacktrack()) backtrack()
        else if (solved()) return
        else move()
    }

    fun needsBacktrack(): Boolean = newlyEmptied.isNotEmpty()

    fun solved(): Boolean = moves.size == target

    fun move() {
        if (moves.isEmpty()) move(pick())
        else move(pickLeast())
    }

    fun pick(): Placement = map.random()

    fun pickLeast(): Placement = map.least()

    fun backtrack() {
        if (moves.isEmpty()) return
        val move = moves.removeLast()
        for (placement in move.collisions) map.add(placement)
        for (placement in move.examined) map.add(placement)
        if (moves.isEmpty()) examined.add(move.placement)
        else moves.last().examined.add(move.placement)
        newlyEmptied.clear()
        for (cell in move.placement) {
            if (map[cell].isEmpty()) newlyEmptied += cell
        }
    }

    fun move(placement: Placement): Move {
        // remove this placement from all cells
        map.remove(placement, mutableSetOf())
        val collisions = mutableSetOf<Placement>()
        val toRemove = mutableSetOf<Placement>()
        // find every placement that touches one of these cells
        for (cell in placement) {
            for (collision in map[cell]) {
                toRemove += collision
            }
        }
        // remove these cells
        for (cell in placement) map.remove(cell)
        // remove the collisions, notice if we empty a cell
        for (collision in toRemove) {
            map.remove(collision, newlyEmptied)
            collisions += collision
        }
        val result = Move(placement, collisions)
        moves += result
        return result
    }

}