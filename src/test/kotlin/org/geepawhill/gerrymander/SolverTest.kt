package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.random.Random

class SolverTest {
    val size = Coords(2, 2)
    val solver = Solver(Random.Default)

    val placements = Omino.fixed(2).flatMap { it.placements(size) }

    @Test
    fun `backtrack restores collisions`() {
        val placement = Move(emptySet(), setOf(placements[0]))
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.links[placements[0].first()]).contains(placements[0])
    }

    @Test
    fun `bulk test`() {
        val runner = BulkRun()
        runner.run(10000)
    }

    @Test
    fun `backtrack restores examined`() {
        val move = Move(emptySet(), emptySet())
        move.examined += placements[0]
        solver.moves += move
        solver.backtrack()
        assertThat(solver.links[placements[0].first()]).contains(placements[0])
    }

    @Test
    fun `backtrack on one-item stack remembers examined`() {
        val placement = Move(placements[0], emptySet())
        solver.moves += placement
        solver.backtrack()
        assertThat(solver.examined).contains(placements[0])
    }

    @Disabled
    @Test
    fun `link counts`() {
        val solver = Solver(Random.Default)
        solver.reset(5, 10, 10)
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

    @Test
    fun `move notices newlyEmptied`() {
        solver.links.add(placements[2])
        solver.move(placements[0])
        assertThat(solver.orphanedCoordinates).isNotEmpty()
    }

    @Test
    fun `backtrack from first move does not ruin my life`() {
        solver.reset(3, 2, 3)
        val clone = solver.links.copy()
        val lshape = setOf(
            Coords(0, 1), Coords(1, 0), Coords(1, 1)
        )
        solver.move(lshape)
        solver.step()
        assertThat(solver.examined).containsExactly(lshape)
        clone.remove(lshape)
        assertThat(solver.links.map).isEqualTo(clone.map)
        assertThat(solver.orphanedCoordinates).isEmpty()
    }

    @Test
    fun `backtrack notices when it leaves a newly-emptied cell`() {
        solver.reset(2, 8, 1)
        val sideways = Omino(Coords(0, 0), Coords(1, 0))
        val center = sideways.placement(Coords(8, 1), Coords(3, 0))
        val left = sideways.placement(Coords(8, 1), Coords(0, 0))
        solver.move(center)
        solver.move(left)
        solver.step()
        solver.step()
        assertThat(solver.moves).isEmpty()
    }

    @Test
    fun `one-answer solver test`() {
        solver.reset(2, 2, 1)
        solver.step()
        assertThat(solver.isSolved).isTrue()
    }

    @RepeatedTest(100)
    fun `2x3 solver test`() {
        solver.reset(2, 2, 3)
        while (!solver.isSolved) solver.step()
    }
}
