package model.algo

import model.graph.Graph
import model.graph.Vertex
import java.util.LinkedList
import java.util.Queue


fun Graph.computeBetweennessCentrality(): Map<Vertex, Double> {
    val centrality = mutableMapOf<Vertex, Double>()
    vertices.forEach { v -> centrality[v] = 0.0 }

    for (s in vertices) {
        val stack = mutableListOf<Vertex>()
        val predecessors = mutableMapOf<Vertex, MutableList<Vertex>>()
        val sigma = mutableMapOf<Vertex, Int>()
        val distance = mutableMapOf<Vertex, Int>()

        vertices.forEach { w -> predecessors[w] = mutableListOf() }
        vertices.forEach { w -> sigma[w] = 0 }
        vertices.forEach { w -> distance[w] = -1 }

        sigma[s] = 1
        distance[s] = 0

        val queue: Queue<Vertex> = LinkedList()
        queue.add(s)


        while (queue.isNotEmpty()) {
            val v = queue.poll()
            stack.add(v)

            val neighbors = if (isDirected) {
                edges.filter { it.vertices.first == v }.map { it.vertices.second }
            } else {
                edges.filter { it.vertices.first == v || it.vertices.second == v }
                    .map { if (it.vertices.first == v) it.vertices.second else it.vertices.first }
            }

            for (w in neighbors) {
                distance[w]?.let {
                    if (it < 0) {
                        queue.add(w)
                        distance[w] = distance[v]!! + 1
                    }
                }
                if (distance[w] == distance[v]!! + 1) {
                    sigma[w] = sigma[w]!! + sigma[v]!!
                    predecessors[w]!!.add(v)
                }
            }
        }

        val delta = mutableMapOf<Vertex, Double>()
        vertices.forEach { w -> delta[w] = 0.0 }

        while (stack.isNotEmpty()) {
            val w = stack.removeAt(stack.size - 1)
            for (v in predecessors[w] ?: emptyList()) {
                delta[v] = delta[v]!! + (sigma[v]!!.toDouble() / sigma[w]!!.toDouble()) * (1 + delta[w]!!)
            }
            if (w != s) {
                centrality[w] = centrality[w]!! + delta[w]!!
            }
        }
    }

    return centrality
}


fun Graph.findCrucialVertices(): List<Vertex> {
    val centrality = computeBetweennessCentrality()
    if (centrality.isEmpty()) return emptyList()

    val maxCentrality = centrality.maxOfOrNull { it.value } ?: return emptyList()
    return centrality.filter { it.value == maxCentrality }.keys.toList()
}