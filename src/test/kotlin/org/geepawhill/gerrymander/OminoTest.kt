package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OminoTest {
    val zeroZero = Omino(Coords(0, 0))

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
        Omino.fixed(3).forEach { it.ascii() }
        assertThat(Omino.fixed(3).size).isEqualTo(6)
    }
}