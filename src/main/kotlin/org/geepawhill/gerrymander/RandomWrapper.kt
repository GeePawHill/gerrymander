package org.geepawhill.gerrymander

import kotlin.random.Random

class RandomWrapper {
    var count = 0
    var random = Random(0)

    fun nextInt(limit: Int): Int {
        count += 1
        return random.nextInt(limit)
    }

    fun reset(seed: Int, count: Int) {
        random = Random(seed)
        repeat(count) { _ -> nextInt(100) }
    }
}