package model.algo

import model.graph.Graph
import model.graph.Vertex

fun labelPropagation(graph: Graph, maxIterations: Int = 100): Map<Int, List<Vertex>> {
    val labels = graph.vertices.associateWith { it.id }.toMutableMap()

    val adjacencyList = graph.toAdjacencyList()

    var iteration = 0
    var changed: Boolean

    do {
        iteration++
        changed = false

        for (vertex in graph.vertices.shuffled()) {
            val neighbors = adjacencyList[vertex] ?: continue
            if (neighbors.isEmpty()) continue

            val mostCommon = neighbors
                .mapNotNull { labels[it] }
                .groupingBy { it }
                .eachCount()
                .maxWithOrNull(compareBy<Map.Entry<Int, Int>>({ it.value }, { -it.key }))
                ?.key ?: continue

            if (labels[vertex] != mostCommon) {
                labels[vertex] = mostCommon
                changed = true
            }
        }
    } while (changed && iteration < maxIterations)

    return labels.entries.groupBy({ it.value }, { it.key })
}