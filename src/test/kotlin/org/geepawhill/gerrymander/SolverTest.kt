package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class SolverTest {
    val size = Coords(2, 2)
    val solver = Solver()

    val placements = Omino.fixed(2).flatMap { it.placements(size) }

    @Test
    fun `backtrack restores collisions`() {
        val placement = Move(emptySet(), setOf(placements[0]))
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.map[placements[0].first()]).contains(placements[0])
    }

    @Test
    fun `backtrack restores examined`() {
        val move = Move(emptySet(), emptySet())
        move.examined += placements[0]
        solver.moves += move
        solver.backtrack()
        assertThat(solver.map[placements[0].first()]).contains(placements[0])
    }

    @Test
    fun `backtrack on one-item stack remembers examined`() {
        val placement = Move(placements[0], emptySet())
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.examined).contains(placements[0])
    }

    @Test
    fun `backtrack on n-item stack puts examined in previous`() {
        val arbitrary = Move(emptySet(), emptySet())
        solver.moves += arbitrary
        val previous = Move(emptySet(), emptySet())
        solver.moves += previous
        val move = Move(placements[0], emptySet())
        solver.moves += move
        solver.backtrack()
        assertThat(previous.examined).contains(placements[0])
    }

    @Test
    fun `move adds Move with placement`() {
        val move = solver.move(placements[0])
        assertThat(solver.moves.last()).isEqualTo(move)

    }

    @Disabled
    @Test
    fun `one-answer solver test`() {
        solver.prepare(2, 2, 1)
        solver.step()
        assertThat(solver.isSolved).isTrue()
    }

    @RepeatedTest(100)
    fun `2x3 solver test`() {
        solver.prepare(2, 2, 3)
        while (!solver.isSolved) solver.step()
        println()
        solver.moves.forEach {
            println(it.placement)
        }
    }
}
