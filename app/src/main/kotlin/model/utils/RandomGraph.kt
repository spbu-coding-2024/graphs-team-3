package model.utils

import model.graph.Graph

fun randomGraph(
    isDirected: Boolean,
    isWeighted: Boolean,
    vertexCount: Int,
    edgeMaxCount: Int,
    maxWeight: Long = 1,
): Graph {
    var resultGraph = Graph(isDirected)
    var edges = mutableSetOf<Pair<Int, Int>>()

    for (i in 0 until vertexCount) {
        resultGraph.addVertex(i.toString())
    }

    for (i in 0 until vertexCount) {
        for (j in (0 until vertexCount).shuffled().take((1..edgeMaxCount).random())) {
            if (isDirected && edges.contains(j to i)) {
                continue
            }
            resultGraph.addEdge(i, j, (1..maxWeight).random())
            edges.add(i to j)
        }
    }

    return resultGraph
}
