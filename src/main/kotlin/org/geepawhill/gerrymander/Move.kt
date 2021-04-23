package org.geepawhill.gerrymander

class Move(val placement: Placement, val collisions: Set<Placement>) {
    val examined = mutableSetOf<Placement>()
}