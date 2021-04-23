package org.geepawhill.gerrymander

class Solver {
    val moves = mutableListOf<Move>()
    val map = PlacementMap()

    val examined = mutableSetOf<Placement>()

    fun prepare(order: Int, width: Int, height: Int) {
        if ((width * height) % order != 0) throw IllegalArgumentException("Order and width and height don't work.")
        moves.clear()
        examined.clear()
        map.clear()
        val size = Coords(width, height)
        Omino.fixed(order).forEach { omino ->
            omino.placements(size).forEach { placement ->
                map.add(placement)
            }
        }
    }

    fun backtrack() {
        if (moves.isEmpty()) return
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
        for (cell in placement) {
            for (collision in map[cell]) {
                map.remove(collision, newlyEmptied)
                collisions += collision
            }
        }
        val result = Move(placement, collisions)
        moves += result
        return result
    }

}