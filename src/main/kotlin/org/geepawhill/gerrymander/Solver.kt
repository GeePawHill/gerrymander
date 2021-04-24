package org.geepawhill.gerrymander


class Solver {
    val moves = mutableListOf<Move>()
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
        val placement = pick()
        move(placement)
    }

    fun pick(): Placement {
        val placement = map.random()
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
    }

    fun move(placement: Placement): Move {
        map.remove(placement, mutableSetOf())
        val collisions = mutableSetOf<Placement>()
        val newlyEmptied = mutableSetOf<Coords>()
        val toRemove = mutableSetOf<Placement>()
        for (cell in placement) {
            for (collision in map[cell]) {
                toRemove += collision
            }
        }
        for (collision in toRemove) {
            map.remove(collision, newlyEmptied)
            collisions += collision
        }
        val result = Move(placement, collisions)
        moves += result
        return result
    }

}