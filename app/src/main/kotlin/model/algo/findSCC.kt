package model.algo

import model.graph.Graph
import model.graph.Vertex

fun findSCC(graph: Graph): Set<Set<Vertex>> {
    val adjacencyList = graph.toAdjacencyList()
    val revAdjacencyList: MutableMap<Vertex, MutableList<Vertex>> = mutableMapOf()
    for (u in adjacencyList.keys) {
        revAdjacencyList[u] = mutableListOf()
    }
    for ((u, near) in adjacencyList) {
        for (v in near) {
            revAdjacencyList[v]?.add(u)
        }
    }

    val visited = mutableSetOf<Vertex>()
    val stack = ArrayDeque<Vertex>()
    val result = mutableSetOf<Set<Vertex>>()

    fun dfs(u: Vertex) {
        visited += u
        for (v in adjacencyList[u]?: throw IllegalStateException()) {
            if (v !in visited) dfs(v)
        }
        stack.addFirst(u)
    }

    for (u in adjacencyList.keys) if (u !in visited) dfs(u)

    visited.clear()
    fun findComponents(u: Vertex, component: MutableSet<Vertex>) {
        visited += u
        component += u
        for (v in revAdjacencyList[u]?: throw IllegalStateException()) {
            if (v !in visited) findComponents(v, component)
        }
    }

    while (stack.isNotEmpty()) {
        val u = stack.removeFirst()
        if (u !in visited) {
            val component = mutableSetOf<Vertex>()
            findComponents(u, component)
            result += component
        }
    }

    return result
}

