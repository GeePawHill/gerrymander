package org.geepawhill.gerrymander

import tornadofx.*

class Solver(val randoms: RandomWrapper, val monitor: Monitor) {
    var stepCount = 0
    val moves = observableListOf<Move>()
    val links = PlacementLinks(randoms)
    val orphanedCoordinates = mutableSetOf<Coords>()
    val finder = GroupFinder()

    val examined = mutableSetOf<Placement>()
    val isSolved get() = links.size == 0 && orphanedCoordinates.isEmpty()
    var needsBacktrack = false

    fun reset(ominoes: Set<Omino>, width: Int, height: Int) {
        makeLinks(ominoes, width, height)
        restart()
        monitor.reset(ominoes.toList(), width, height)
    }

    fun restart() {
        moves.clear()
        examined.clear()
        orphanedCoordinates.clear()
    }

    fun run(ominoes: Set<Omino>, width: Int, height: Int): List<Move> {
        stepCount = 0
        reset(ominoes, width, height)
        while (!isSolved) {
            step()
            stepCount++
        }
        return moves
    }

    private fun makeLinks(
        ominos: Set<Omino>,
        width: Int,
        height: Int,
    ) {
        links.clear()
        val size = Coords(width, height)
        ominos.forEach { omino ->
            omino.placements(size).forEach { placement ->
                links.add(placement)
            }
        }
    }

    fun step() {
        if (isSolved) return
        if (needsBacktrack) backtrack()
        else move()
    }

    fun move() {
        val placement = pickLeast()
        move(placement)
        monitor.place(placement)
    }

    fun pickLeast(): Placement = links.least()

    fun backtrack() {
        needsBacktrack = false
        if (moves.isEmpty()) return
        val move = moves.removeLast()
        monitor.backtrack(move.placement)
        restoreExcludedPlacements(move)
        if (moves.isEmpty()) {
            examined.forEach { links.add(it) }
        }
        addPlacementToExamined(move)
        resetOrphanedCoordinates(move)
        if (orphanedCoordinates.isNotEmpty()) {
            needsBacktrack = true
        }

        if (orphanedCoordinates.isNotEmpty() && moves.isEmpty()) {
            throw RuntimeException("Whatever.")
        }
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
        if (orphanedCoordinates.isNotEmpty()) {
            needsBacktrack = true
            return result
        }
        val groups = finder.find(links.map.keys.toMutableList())
        if (groups.size > 1) {
            groups.forEach {
                if (it.size % 6 != 0) {
                    needsBacktrack = true
                    return result
                }
            }
        }
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