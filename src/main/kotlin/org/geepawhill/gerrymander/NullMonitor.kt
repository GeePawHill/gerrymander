package org.geepawhill.gerrymander

class NullMonitor : Monitor {
    override fun place(add: Placement) {
    }

    override fun backtrack(remove: Placement) {
    }

    override fun reset(
        ominos: List<Omino>,
        width: Int,
        height: Int
    ) {
    }
}