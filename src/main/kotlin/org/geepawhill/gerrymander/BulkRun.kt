package org.geepawhill.gerrymander

import kotlin.random.Random

class BulkRun {
    val stepHistogram = HashMap<Int, Int>()

    fun run(count: Int) {
        var soFar = 0
        while (soFar++ < count) {
            val solver = Solver(Random(soFar))
            solver.prepare(5, 5, 5)
            solver.map.dump()
            var limit = 100
            var step = 0
            while (!solver.isSolved && step < limit) {
                solver.step()
                step++
            }
            validateSolver(solver, 5, 5)
            stepHistogram[step] = stepHistogram.getOrDefault(step, 0) + 1
        }
        stepHistogram.keys.sorted().forEach {
            println("Steps: $it (${stepHistogram[it]})")
        }
    }

    fun validateSolver(solver: Solver, width: Int, height: Int) {
        val coords = solver.moves.flatMap { it.placement }.toSet()
        if (coords.size != width * height) throw Exception("Bad!")
    }
}