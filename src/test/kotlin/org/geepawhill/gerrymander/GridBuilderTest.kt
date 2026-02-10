package org.geepawhill.gerrymander

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class GridBuilderTest {
    val builder: GridBuilder = GridBuilder()

    @Test
    fun empty() {
        val emptys = builder.emptys("  ", "  ")
        assertThat(emptys).containsExactlyInAnyOrder(
            Coords(0, 0),
            Coords(1, 0),
            Coords(0, 1),
            Coords(1, 1)
        )
    }

    @Test
    fun almostEmpty() {
        val emptys = builder.emptys(" *", "  ")
        assertThat(emptys).containsExactlyInAnyOrder(
            Coords(0, 0),
            Coords(0, 1),
            Coords(1, 1)
        )
    }
}