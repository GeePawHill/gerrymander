package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OminoTest {
    val zeroZero = Omino(Coords(0, 0))
    val gridSize = Coords(5, 5)

    @Test
    fun `empty constructor`() {
        assertThat(Omino()).isEmpty()
    }

    @Test
    fun `constructor from set of coords()`() {
        assertThat(zeroZero).contains(Coords(0, 0))
    }

    @Test
    fun `constructor as clone`() {
        assertThat(Omino(zeroZero)).contains(Coords(0, 0))
    }

    @Test
    fun `constructor as extender`() {
        assertThat(Omino(zeroZero, Coords(1, 0))).containsExactly(Coords(0, 0), Coords(1, 0))
    }

    @Test
    fun `normalizes on x`() {
        assertThat(Omino(setOf(Coords(-1, 0)))).containsExactly(Coords(0, 0))
        assertThat(Omino(setOf(Coords(5, 0)))).containsExactly(Coords(0, 0))
    }

    @Test
    fun `normalizes on y`() {
        assertThat(Omino(setOf(Coords(0, -1)))).containsExactly(Coords(0, 0))
        assertThat(Omino(setOf(Coords(0, 5)))).containsExactly(Coords(0, 0))
    }

    @Test
    fun `sorts by x,y`() {
        assertThat(Omino(Coords(1, 0), Coords(0, 0))).containsExactly(Coords(0, 0), Coords(1, 0))
        assertThat(Omino(Coords(0, 1), Coords(0, 0))).containsExactly(Coords(0, 0), Coords(0, 1))
        assertThat(Omino(Coords(0, 2), Coords(0, 1), Coords(0, 0))).containsExactly(
            Coords(0, 0),
            Coords(0, 1),
            Coords(0, 2)
        )
    }

    @Test
    fun `normalizes all`() {
        assertThat(Omino(setOf(Coords(0, -1), Coords(1, -1)))).containsExactly(Coords(0, 0), Coords(1, 0))
    }

    @Test
    fun `extension constructor normalizes()`() {
        assertThat(Omino(zeroZero, Coords(-1, 0))).containsExactly(Coords(0, 0), Coords(1, 0))
    }

    @Test
    fun `data class confirmation`() {
        assertThat(Omino(Coords(0, 0))).isEqualTo(Omino(Coords(0, 0)))
        assertThat(Omino(Coords(0, 0))).isNotEqualTo(Omino(Coords(0, 0), Coords(0, 1)))
    }

    @Test
    fun `expands from one into two ominos`() {
        assertThat(zeroZero.expand()).containsExactly(
            Omino(Coords(0, 0), Coords(0, 1)),
            Omino(Coords(0, 0), Coords(1, 0))
        )
    }

    @Test
    fun `fixed generates correct sizes`() {
        assertThat(Omino.fixed(1).size).isEqualTo(1)
        assertThat(Omino.fixed(2).size).isEqualTo(2)
        assertThat(Omino.fixed(3).size).isEqualTo(6)
    }

    @Test
    fun `no placement if negative x`() {
        assertThat(zeroZero.placement(gridSize, Coords(-1, 0))).isEmpty()
    }

    @Test
    fun `no placement if negative y`() {
        assertThat(zeroZero.placement(gridSize, Coords(0, -1))).isEmpty()
    }

    @Test
    fun `no placement if x bigger than width`() {
        assertThat(zeroZero.placement(gridSize, Coords(5, 0))).isEmpty()
    }

    @Test
    fun `no placement if y bigger than height`() {
        assertThat(zeroZero.placement(gridSize, Coords(0, 5))).isEmpty()
    }

    @Test
    fun `correct placement if bigger than one coord`() {
        assertThat(Omino(zeroZero, Coords(1, 0)).placement(gridSize, Coords(0, 0))).isEqualTo(
            setOf(Coords(0, 0), Coords(1, 0))
        )
    }

    @Test
    fun `correct placement on l-shaped omino`() {
        val altGridSize = Coords(6, 6)
        val tileA = Coords(2, 4)
        val tileB = Coords(2, 5)
        val tileC = Coords(3, 4)
        val cornerL = Omino(zeroZero, Coords(0, 1), Coords(1, 0))
        assertThat(cornerL.placement(altGridSize, tileA)).containsExactly(
            tileA, tileB, tileC,
        )
    }

    @Test
    fun `all placements for omino`() {
        val actual = Omino(zeroZero, Coords(1, 0)).placements(Coords(2, 2))
        assertThat(actual).containsExactly(
            setOf(Coords(0, 0), Coords(1, 0)),
            setOf(Coords(0, 1), Coords(1, 1)),
        )
    }
}