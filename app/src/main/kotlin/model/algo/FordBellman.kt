package model.algo

import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex
import java.util.Vector
import kotlin.math.max

fun fordBellman(
    graph: Graph,
    start: Vertex,
    end: Vertex,
): Triple<Vector<Vertex>?, Vector<Vertex>?, Boolean> {
    val dist = hashMapOf<Vertex, Long>()
    val path = hashMapOf<Vertex, Vertex?>()
    graph.vertices.forEach { vertex ->
        dist[vertex] = Long.MAX_VALUE
        path[vertex] = null
    }
    dist[start] = 0
    var i = 0
    var cycleFind: Vertex? = null
    while (i < graph.vertices.size) {
        cycleFind = null
        var wasRelaxed = false
        for (edge in graph.edges) {
            val relaxResult = relaxation(graph, edge, dist, path)
            cycleFind = relaxResult.first
            if (relaxResult.second) {
                wasRelaxed = true
            }
        }
        if (!wasRelaxed) {
            break
        }
        i++
    }

    if (dist[end]!! == Long.MAX_VALUE) {
        return Triple(null, null, false)
    }

    var cycle = Vector<Vertex>()

    if (cycleFind != null) {
        cycle = findCycle(graph, cycleFind, path)
    }

    val result = pathRestore(graph, start, end, path, cycle)
    return Triple(result, cycle, result == null)
}

fun relaxation(
    graph: Graph,
    edge: Edge,
    dist: HashMap<Vertex, Long>,
    path: HashMap<Vertex, Vertex?>,
): Pair<Vertex?, Boolean> {
    var cycleFind: Vertex? = null
    var wasRelaxed = false
    if (dist[edge.vertices.first]!! < Long.MAX_VALUE) {
        if (dist[edge.vertices.second]!! > dist[edge.vertices.first]!! + edge.weight) {
            // предотвращение переполнения при наличии отрицательного цикла
            dist[edge.vertices.second] = max(Long.MIN_VALUE, dist[edge.vertices.first]!! + edge.weight)
            path[edge.vertices.second] = edge.vertices.first
            cycleFind = edge.vertices.second
            wasRelaxed = true
        }
    }
    if (!graph.isDirected) {
        if (dist[edge.vertices.second]!! < Long.MAX_VALUE) {
            if (dist[edge.vertices.first]!! > dist[edge.vertices.second]!! + edge.weight) {
                // предотвращение переполнения при наличии отрицательного цикла
                dist[edge.vertices.first] = max(Long.MIN_VALUE, dist[edge.vertices.second]!! + edge.weight)
                path[edge.vertices.first] = edge.vertices.second
                cycleFind = edge.vertices.first
                wasRelaxed = true
            }
        }
    }
    return cycleFind to wasRelaxed
}

fun findCycle(
    graph: Graph,
    startVertex: Vertex,
    path: HashMap<Vertex, Vertex?>,
): Vector<Vertex> {
    val cycle = Vector<Vertex>()
    var currentVertex: Vertex? = startVertex
    for (i in 0..graph.vertices.size) {
        currentVertex = path[currentVertex]!!
    }
    var current = currentVertex
    do {
        cycle.addFirst(current)
        current = path[current]!!
    } while (current != currentVertex)
    return cycle
}

fun pathRestore(
    graph: Graph,
    start: Vertex,
    end: Vertex,
    path: HashMap<Vertex, Vertex?>,
    cycle: Vector<Vertex>,
): Vector<Vertex>? {
    var result = Vector<Vertex>()
    var current = end
    result.addFirst(current)
    while (current != start) {
        val prev = path[current]!!
        result.addFirst(prev)
        if (cycle.contains(prev)) {
            return null
        }
        current = prev
    }
    return result
}
