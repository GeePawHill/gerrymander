package org.geepawhill.gerrymander

class Move(val links: Set<Link>, val collisions: Set<Link>) {
    val examined = mutableSetOf<Link>()
}