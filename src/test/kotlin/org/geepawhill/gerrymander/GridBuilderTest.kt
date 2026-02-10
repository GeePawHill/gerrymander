package org.geepawhill.gerrymander

import org.junit.jupiter.api.Test

class GridBuilder {
    fun emptys(vararg strings: String): List<Coords> {
        return listOf()
    }
}

class GridBuilderTest {
    val builder: GridBuilder = GridBuilder()

    @Test
    fun empty() {
        val emptys = builder.emptys("   ", "   ", "   ")
    }
}