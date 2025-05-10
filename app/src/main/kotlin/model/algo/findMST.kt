package model.algo

import model.graph.Graph
import model.graph.Edge

fun findMST(graph: Graph): Set<Edge> {
    require(!graph.isDirected) { "Graph can not be directed" }
    require(!graph.isNegativeWeight) { "Edges can not have negative weights" }

    val sortedEdges = graph.edges.sortedBy { it.weight }
    val mstEdges = mutableSetOf<Edge>()

    val vertexCount = graph.vertexIdCount

    val parents = IntArray(vertexCount) { -1 }

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
        val sourceId: Int = edge.vertices.first.id
        val destinationId = edge.vertices.second.id

        if (findRoot(sourceId) != findRoot(destinationId)) {
            mstEdges += edge
            unionSets(sourceId, destinationId)

            if (mstEdges.size == graph.vertexIdCount - 1) break
        }
    }

    return mstEdges
}