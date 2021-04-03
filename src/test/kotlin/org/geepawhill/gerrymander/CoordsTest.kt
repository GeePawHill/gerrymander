package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoordsTest {

    val coords = Coords(0, 0)

    @Test
    fun directions() {
        assertThat(coords[Direction.NORTH]).isEqualTo(Coords(0, -1))
        assertThat(coords[Direction.SOUTH]).isEqualTo(Coords(0, 1))
        assertThat(coords[Direction.EAST]).isEqualTo(Coords(1, 0))
        assertThat(coords[Direction.WEST]).isEqualTo(Coords(-1, 0))
        assertThat(coords[Direction.SOUTHWEST]).isEqualTo(Coords(-1, 1))
        assertThat(coords[Direction.SOUTHEAST]).isEqualTo(Coords(1, 1))
        assertThat(coords[Direction.NORTHWEST]).isEqualTo(Coords(-1, -1))
        assertThat(coords[Direction.NORTHEAST]).isEqualTo(Coords(1, -1))
    }

}