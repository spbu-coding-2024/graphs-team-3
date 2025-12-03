package model.algo

import model.graph.Graph
import model.graph.Vertex

fun Graph.findCyclesStartingFrom(startVertexId: Int): List<List<Vertex>> {
    val cycles = mutableListOf<List<Vertex>>()
    val startVertex = getVertex(startVertexId) ?: return emptyList()

    fun dfs(
        current: Vertex,
        path: MutableList<Vertex>,
        recStack: MutableSet<Vertex>
    ) {
        path.add(current)
        recStack.add(current)

        val neighbors = if (isDirected) {
            edges.filter { it.vertices.first == current }.map { it.vertices.second }
        } else {
            edges
                .filter { it.vertices.first == current || it.vertices.second == current }
                .map { if (it.vertices.first == current) it.vertices.second else it.vertices.first }
        }

        for (neighbor in neighbors) {
            if (neighbor == startVertex && path.size > 2) {
                // Цикл завершён: начинается и заканчивается в startVertex
                cycles.add((path + neighbor).toList())
            } else if (neighbor !in recStack) {
                dfs(neighbor, path, recStack)
            }
        }

        // Backtracking
        path.removeAt(path.size - 1)
        recStack.remove(current)
    }

    dfs(startVertex, mutableListOf(), mutableSetOf())

    return cycles
}