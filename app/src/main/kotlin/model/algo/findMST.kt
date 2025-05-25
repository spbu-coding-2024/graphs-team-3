package model.algo

import model.graph.Graph
import model.graph.Edge

fun findMST(graph: Graph): Set<Edge> {
    require(!graph.isDirected) { "MST is only implemented for undirected graphs" }

    val mstEdges = mutableSetOf<Edge>()
    if (graph.vertices.isEmpty()) return mstEdges
    val sortedEdges = graph.edges.sortedBy { it.weight }

    val verticesList = graph.vertices.toList()
    val idToIndex = verticesList
        .mapIndexed { index, vertex -> vertex.id to index }
        .toMap()

    val parents = IntArray(verticesList.size) { -1 }

    fun findRoot(vertexId: Int): Int {
        if (parents[vertexId] < 0) return vertexId
        parents[vertexId] = findRoot(parents[vertexId])
        return parents[vertexId]
    }

    fun unionSets(firstId: Int, secondId: Int) {
        val rootFirst = findRoot(firstId)
        val rootSecond = findRoot(secondId)
        if (rootFirst == rootSecond) return

        if (parents[rootFirst] < parents[rootSecond]) {
            parents[rootFirst] += parents[rootSecond]
            parents[rootSecond] = rootFirst
        } else {
            parents[rootSecond] += parents[rootFirst]
            parents[rootFirst] = rootSecond
        }
    }

    for (edge in sortedEdges) {
        val sourceId: Int = idToIndex[edge.vertices.first.id]!!
        val destinationId = idToIndex[edge.vertices.second.id]!!

        if (findRoot(sourceId) != findRoot(destinationId)) {
            mstEdges += edge
            unionSets(sourceId, destinationId)

            if (mstEdges.size == verticesList.size - 1) break
        }
    }

    if (mstEdges.size != verticesList.size - 1) {
        throw IllegalStateException("Graph is not connected")
    }

    return mstEdges
}