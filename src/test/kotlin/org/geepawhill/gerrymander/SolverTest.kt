package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolverTest {
    val size = Coords(2, 2)
    val solver = Solver(2, size)

    val fixed = Omino.fixed(2)
    val links = fixed.flatMap { it.links(size) }

    @Test
    fun `backtrack restores collisions and dead`() {
        val placement = Placement(setOf(links[0]), setOf(links[1]))
        solver.placements += placement
        solver.backtrack()
        assertThat(solver.links).contains(links[1])
        assertThat(solver.dead).contains(links[0])
    }

    @Test
    fun `backtrackIfNeeded backtracks with no links`() {
        val placement = Placement(setOf(links[0]), setOf(links[1]))
        solver.placements += placement
        assertThat(solver.backtrackIfNeeded()).isTrue()
        assertThat(solver.links).isNotEmpty()
    }

    @Test
    fun `backtrackIfNeeded no-ops if there are links`() {
        val placement = Placement(setOf(links[0]), setOf(links[1]))
        solver.placements += placement
        solver.links += links[2]
        assertThat(solver.backtrackIfNeeded()).isTrue()
        assertThat(solver.links).containsExactly(links[2])
    }

    @Test
    fun `backtrackIfNeeded fails if there's no links`() {
        assertThat(solver.backtrackIfNeeded()).isFalse()
    }
}