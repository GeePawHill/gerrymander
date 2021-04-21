package org.geepawhill.gerrymander

class Move(val links: Set<Placement>, val collisions: Set<Placement>) {
    val examined = mutableSetOf<Placement>()
}