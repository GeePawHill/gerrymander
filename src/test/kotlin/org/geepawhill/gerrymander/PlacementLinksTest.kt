package org.geepawhill.gerrymander

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PlacementLinksTest {
    private val map = PlacementLinks(Random.Default)
    private val one = Omino(Coords(0, 0)).placement(Coords(5, 5), Coords(2, 2))
    private val two = Omino(Coords(0, 0), Coords(1, 0)).placement(Coords(5, 5), Coords(2, 2))

    @Test
    fun `empty map has zero cells`() {
        assertThat(map.size).isEqualTo(0)
    }

    @Test
    fun `add adds correct entry`() {
        map.add(one)
        assertThat(map.size).isEqualTo(1)
        assertThat(map[Coords(2, 2)]).containsExactly(one)
    }

    @Test
    fun `add adds multiple entries`() {
        map.add(one)
        map.add(two)
        assertThat(map[Coords(2, 2)]).containsExactly(one, two)
    }

    @Test
    fun `remove removes & remembers cleared cells`() {
        map.add(one)
        val emptied = map.remove(one)
        assertThat(emptied).contains(one.first())
    }

    @Test
    fun `remove removes & does not remember non-emptied cells`() {
        map.add(one)
        map.add(two)
        val emptied = map.remove(one)
        assertThat(emptied).isEmpty()
        assertThat(map[one.first()]).contains(two)
    }
}