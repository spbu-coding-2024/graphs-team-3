package model.algo

import model.graph.Graph
import model.graph.Vertex

fun findSCC(graph: Graph): Set<Set<Vertex>> {
    val adjacencyList = graph.toAdjacencyList()
    val revAdjacencyList: MutableMap<Vertex, MutableList<Vertex>> = mutableMapOf()
    for (vertex in adjacencyList.keys) {
        revAdjacencyList[vertex] = mutableListOf()
    }
    for ((from, neighbor) in adjacencyList) {
        for (to in neighbor) {
            revAdjacencyList[to]?.add(from)
        }
    }

    val visited = mutableSetOf<Vertex>()
    val stack = ArrayDeque<Vertex>()
    val result = mutableSetOf<Set<Vertex>>()

    fun dfs(vertex: Vertex) {
        visited += vertex
        for (neighbor in adjacencyList[vertex]?: throw IllegalStateException()) {
            if (neighbor !in visited) dfs(neighbor)
        }
        stack.addFirst(vertex)
    }

    for (vertex in adjacencyList.keys) if (vertex !in visited) dfs(vertex)

    visited.clear()
    fun findComponents(vertex: Vertex, component: MutableSet<Vertex>) {
        visited += vertex
        component += vertex
        for (neighbor in revAdjacencyList[vertex]?: throw IllegalStateException()) {
            if (neighbor !in visited) findComponents(neighbor, component)
        }
    }

    while (stack.isNotEmpty()) {
        val vertex = stack.removeFirst()
        if (vertex !in visited) {
            val component = mutableSetOf<Vertex>()
            findComponents(vertex, component)
            result += component
        }
    }

    return result
}

