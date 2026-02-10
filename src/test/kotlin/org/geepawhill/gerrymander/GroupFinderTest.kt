package org.geepawhill.gerrymander

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class GroupFinderTest {
    val finder = GroupFinder()
    val builder = GridBuilder()

    @Test
    fun emptyGrid() {
        val emptys = builder.emptys("...")
        val groups = finder.find(emptys)
        assertThat(groups.size).isEqualTo(1)
        assertThat(groups.first().size).isEqualTo(3)
    }

    @Test
    fun noPartition() {
        val emptys = builder.emptys("*..")
        val groups = finder.find(emptys)
        assertThat(groups.size).isEqualTo(1)
        assertThat(groups.first().size).isEqualTo(2)
    }

    @Test
    fun onePartition() {
        val emptys = builder.emptys(".*.")
        val groups = finder.find(emptys)
        assertThat(groups.size).isEqualTo(2)
        assertThat(groups.first().size).isEqualTo(1)
        assertThat(groups.last().size).isEqualTo(1)
    }

    @Test
    fun largerCase() {
        val emptys = builder.emptys(
            "..*..",
            "..***",
            "..*.."
        )
        val groups = finder.find(emptys)
        assertThat(groups.size).isEqualTo(3)
        assertThat(groups.first().size).isEqualTo(6)
        assertThat(groups[1].size).isEqualTo(2)
        assertThat(groups.last().size).isEqualTo(2)
    }
}