package model.algo.findBridges

import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex
import kotlin.math.min

fun findBridges(graph: Graph): Set<Edge> {
    var time = 0
    val timeIn = mutableMapOf<Vertex, Int>()
    val ret = mutableMapOf<Vertex, Int>()
    val isVisited = mutableMapOf<Vertex, Boolean>()
    graph.vertices.forEach { vertex ->
        timeIn[vertex] = -1
        ret[vertex] = -1
        isVisited[vertex] = false
    }
    val adjacencyList = graph.toAdjacencyList()
    var briges = mutableSetOf<Edge>()

    fun dfs(current: Vertex, parent: Vertex? = null) {
        timeIn[current] = time++
        ret[current] = time - 1
        isVisited[current] = true
        for (adjacentVertex in adjacencyList[current] ?: throw IllegalStateException()) {
            if (adjacentVertex == parent) continue
            if (isVisited[adjacentVertex] ?: throw IllegalStateException()) {
                ret[current] = min(
                    ret[current] ?: throw IllegalStateException(),
                    timeIn[adjacentVertex] ?: throw IllegalStateException()
                )
            } else {
                dfs(adjacentVertex, current)
                ret[current] = min(
                    ret[current] ?: throw IllegalStateException(),
                    ret[adjacentVertex] ?: throw IllegalStateException()
                )
                if ((ret[adjacentVertex] ?: throw IllegalStateException()) > (timeIn[current]
                        ?: throw IllegalStateException())
                ) {
                    briges.add(
                        graph.getEdge(current, adjacentVertex) ?: throw IllegalStateException("No such vertex in graph")
                    )
                }
            }
        }
    }

    for (vertex in graph.vertices) {
        if (!(isVisited[vertex] ?: throw IllegalStateException())) {
            dfs(vertex)
        }
    }
    return briges
}