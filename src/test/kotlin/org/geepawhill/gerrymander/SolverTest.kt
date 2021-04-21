package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolverTest {
    val size = Coords(2, 2)
    val solver = Solver()

    val links = Omino.fixed(2).flatMap { it.placements(size) }

    @Test
    fun `backtrack restores collisions`() {
        val placement = Move(emptySet(), setOf(links[0]))
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.links).contains(links[0])
    }

    @Test
    fun `backtrack restores examined`() {
        val move = Move(emptySet(), emptySet())
        move.examined += links[0]
        solver.moves += move
        solver.backtrack()
        assertThat(solver.links).contains(links[0])
    }

    @Test
    fun `backtrack on one-item stack remembers examined`() {
        val placement = Move(setOf(links[0]), emptySet())
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.examined).contains(links[0])
    }

    @Test
    fun `backtrack on n-item stack puts examined in previous`() {
        val arbitrary = Move(emptySet(), emptySet())
        solver.moves += arbitrary
        val previous = Move(emptySet(), emptySet())
        solver.moves += previous
        val move = Move(setOf(links[0]), emptySet())
        solver.moves += move
        solver.backtrack()
        assertThat(previous.examined).contains(links[0])
    }

    @Test
    fun `backtrackIfNeeded backtracks with no links`() {
        val placement = Move(setOf(links[0]), setOf(links[1]))
        solver.moves += placement
        assertThat(solver.backtrackIfNeeded()).isTrue()
        assertThat(solver.links).isNotEmpty()
    }

    @Test
    fun `backtrackIfNeeded no-ops if there are links`() {
        val placement = Move(setOf(links[0]), setOf(links[1]))
        solver.moves += placement
        solver.links += links[2]
        assertThat(solver.backtrackIfNeeded()).isTrue()
        assertThat(solver.links).containsExactly(links[2])
    }

    @Test
    fun `backtrackIfNeeded fails if there's no links`() {
        assertThat(solver.backtrackIfNeeded()).isFalse()
    }

//    @Test
//    fun `move adds Move with links`() {
//        solver.move(links[0])
//        assertThat(solver.moves.size).isEqualTo(1)
//        assertThat(solver.moves[0].links).contains(links[0])
//    }
}