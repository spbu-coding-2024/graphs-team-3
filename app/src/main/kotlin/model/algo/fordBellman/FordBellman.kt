package model.algo.fordBellman

import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex
import kotlin.math.max

fun fordBellman(graph: Graph, start: Vertex, end: Vertex): Set<Edge>? {
    val dist = hashMapOf<Vertex, Long>()
    val path = hashMapOf<Vertex, Edge>()
    val dummyEdge = Edge(-1, Vertex("dummy", -1) to Vertex("dummy", -1))
    graph.vertices.forEach { vertex ->
        dist[vertex] = Long.MAX_VALUE
        path[vertex] = dummyEdge
    }
    dist[start] = 0
    var i = 0
    var checkNegativeCycle = false
    while (i < graph.vertices.size) {
        checkNegativeCycle = false
        var wasRelaxed = false
        for (edge in graph.edges) {
            if (dist[edge.vertices.first]!! < Long.MAX_VALUE) {
                if (dist[edge.vertices.second]!! > dist[edge.vertices.first]!! + edge.weight) {
                    // предотвращение переполнения при наличии отрицательного цикла
                    dist[edge.vertices.second] = max(Long.MIN_VALUE, dist[edge.vertices.first]!! + edge.weight)
                    path[edge.vertices.second] = edge
                    checkNegativeCycle = true
                    wasRelaxed = true
                }
            }
        }
        if (!wasRelaxed) {
            break
        }
        i ++
    }

    if (!checkNegativeCycle) {
        if (dist[end]!! == Long.MAX_VALUE) {
            return null
        }
        val result = mutableSetOf<Edge>()
        var current = end
        while(current != start) {
            val edge = path[current]!!
            if (edge == dummyEdge) {
                return null
            }
            val next = if (current == edge.vertices.first) edge.vertices.second else edge.vertices.first
            result.add(edge)
            current = next
        }
        return result
    }

    return null
}