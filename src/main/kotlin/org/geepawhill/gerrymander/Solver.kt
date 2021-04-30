package org.geepawhill.gerrymander

import tornadofx.*


class Solver {
    val moves = observableListOf<Move>()
    val map = PlacementMap()
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
    }

    fun step() {
        if (backtrackBecauseOfNewlyEmptied()) return
        if (solved()) return
        if (backtrackBecauseNoPlacements()) return
        move()
    }

    fun backtrackBecauseOfNewlyEmptied(): Boolean {
        if (moves.isEmpty()) return false
        if (newlyEmptied.isEmpty()) return false
        println("Newly Emptied: ")
        newlyEmptied.forEach {
            println("   $it")
        }
        backtrack()
        return true
    }

    fun solved(): Boolean {
        if (moves.size < target) return false
        return true
    }

    fun backtrackBecauseNoPlacements(): Boolean {
        if (map.size != 0) return false
        while (map.size == 0) backtrack()
        return true
    }

    fun move() {
        if (moves.isEmpty()) move(pick())
        else move(pickLeast())
    }

    fun pick(): Placement {
        val placement = map.random()
        return placement
    }

    fun pickLeast(): Placement {
        val placement = map.least()
        return placement
    }

    fun backtrack() {
        if (moves.isEmpty()) return
        println("backtrack")
        for (move in moves) {
            println(move.placement)
        }
        val move = moves.removeLast()
        for (placement in move.collisions) map.add(placement)
        for (placement in move.examined) map.add(placement)
        if (moves.isEmpty()) examined.add(move.placement)
        else moves.last().examined.add(move.placement)
        newlyEmptied.clear()
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
        println("Newly emptied: $newlyEmptied")
        val result = Move(placement, collisions)
        moves += result
        return result
    }

}