package algorithms

import model.algo.fordBellman.fordBellman
import model.graph.Graph
import model.graph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.Vector

class FordBellmanTest {
    // Tests shortest path in a simple undirected unweighted graph (all edges weight = 1)
    @Test
    fun `simple test undirected unweighted graph`() {
        val graph = Graph()
        val way: Vector<Vertex> = Vector()
        val start = graph.addVertex("0")
        way.add(start)
        way.add(graph.addVertex("1"))
        way.add(graph.addVertex("2"))
        val end = graph.addVertex("3")
        way.add(end)

        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(2, 3)

        val result = fordBellman(graph, start, end)
        assertFalse(result.third)
        way.forEach {
            assertEquals(it, result.first?.get(0))
            result.first?.remove(it)
        }
    }

    // Tests optimal path selection in undirected weighted graph with varying edge weights
    @Test
    fun `simple test undirected weighted graph`() {
        val graph = Graph()
        val way: Vector<Vertex> = Vector()
        val start = graph.addVertex("0")
        way.add(start)
        way.add(graph.addVertex("1"))
        graph.addVertex("2")
        val end = graph.addVertex("3")
        way.add(end)

        graph.addEdge(0, 1, 1)
        graph.addEdge(0, 2, 3)
        graph.addEdge(1, 3, 2)
        graph.addEdge(2, 3, 1)

        val result = fordBellman(graph, start, end)
        assertFalse(result.third)
        way.forEach {
            assertEquals(it, result.first?.get(0))
            result.first?.remove(it)
        }
    }

    // Tests pathfinding in directed weighted graph verifying correct handling of edge directions
    @Test
    fun `simple test directed weighted graph`() {
        val graph = Graph(true)
        val way: Vector<Vertex> = Vector()
        val start = graph.addVertex("0")
        way.add(start)
        way.add(graph.addVertex("1"))
        way.add(graph.addVertex("2"))
        graph.addVertex("3")
        val end = graph.addVertex("2")
        way.add(end)

        graph.addEdge(0, 1, 11)
        graph.addEdge(1, 2, 1)
        graph.addEdge(2, 4, 1)
        graph.addEdge(1, 4, 10)
        graph.addEdge(0, 3, 20)
        graph.addEdge(3, 4, 30)

        val result = fordBellman(graph, start, end)
        assertFalse(result.third)
        way.forEach {
            assertEquals(it, result.first?.get(0))
            result.first?.remove(it)
        }
    }

    // Verifies correct handling when no path exists between start and end vertices
    @Test
    fun `no path test`() {
        val graph = Graph(true)
        val start = graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        val end = graph.addVertex("3")

        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(0, 2)
        graph.addEdge(3, 1)

        var result = fordBellman(graph, start, end)
        assertFalse(result.third)
        assertEquals(result.first, null)
    }

    // Tests optimal path selection when multiple route options are available
    @Test
    fun `three path test`() {
        val graph = Graph()
        val way: Vector<Vertex> = Vector()
        val start = graph.addVertex("0")
        way.add(start)
        graph.addVertex("1")
        way.add(graph.addVertex("2"))
        way.add(graph.addVertex("3"))
        way.add(graph.addVertex("4"))
        val end = graph.addVertex("5")
        way.add(end)

        graph.addEdge(0, 1, 2)
        graph.addEdge(0, 2, 1)
        graph.addEdge(2, 3, 1)
        graph.addEdge(3, 4, 1)
        graph.addEdge(3, 5, 4)
        graph.addEdge(4, 5, 1)
        graph.addEdge(1, 5, 3)

        val result = fordBellman(graph, start, end)
        assertFalse(result.third)
        way.forEach {
            assertEquals(it, result.first?.get(0))
            result.first?.remove(it)
        }
    }

    // Ensures negative cycle not affecting the path is correctly ignored
    @Test
    fun `negative cycle not on path`() {
        val graph = Graph(true)
        val way: Vector<Vertex> = Vector()
        val start = graph.addVertex("0")
        way.add(start)
        val mid = graph.addVertex("1")
        way.add(graph.addVertex("2"))
        way.add(mid)
        val end = graph.addVertex("3")
        way.add(end)
        graph.addVertex("4")
        graph.addVertex("5")
        graph.addVertex("6")

        graph.addEdge(0, 1, 4)
        graph.addEdge(0, 2, 1)
        graph.addEdge(2, 1, 1)
        graph.addEdge(1, 3, 7)
        graph.addEdge(4, 0, 1)
        graph.addEdge(4, 5, -1)
        graph.addEdge(5, 6, -1)
        graph.addEdge(6, 4, -1)

        val result = fordBellman(graph, start, end)
        assertFalse(result.third)
        way.forEach {
            assertEquals(it, result.first?.get(0))
            result.first?.remove(it)
        }
    }

    // Tests detection of negative cycle involving the start vertex
    @Test
    fun `negative cycle at start`() {
        val graph = Graph(true)
        val cycle: MutableSet<Vertex> = mutableSetOf()
        val start = graph.addVertex("0")
        cycle.add(graph.addVertex("1"))
        cycle.add(graph.addVertex("2"))
        cycle.add(start)
        val end = graph.addVertex("3")

        graph.addEdge(0, 1, -1)
        graph.addEdge(1, 2, -1)
        graph.addEdge(2, 0, -1)
        graph.addEdge(1, 3, 1)

        val result = fordBellman(graph, start, end)
        val resCycle = mutableSetOf<Vertex>()
        result.second?.forEach { resCycle.add(it) }
        assertEquals(result.third, true)
        assertEquals(resCycle.size, cycle.size)
        cycle.forEach {
            assertEquals(cycle.contains(it), true)
            result.second?.remove(it)
        }
    }

    // Tests detection of negative cycle in intermediate path segments
    @Test
    fun `negative cycle at the middle`() {
        val graph = Graph(true)
        val cycle: MutableSet<Vertex> = mutableSetOf()
        val start = graph.addVertex("0")
        cycle.add(graph.addVertex("1"))
        cycle.add(graph.addVertex("2"))
        cycle.add(graph.addVertex("3"))
        val end = graph.addVertex("4")

        graph.addEdge(0, 1, 1)
        graph.addEdge(1, 2, -1)
        graph.addEdge(2, 3, -1)
        graph.addEdge(3, 1, -1)
        graph.addEdge(2, 4, 1)

        val result = fordBellman(graph, start, end)
        val resCycle = mutableSetOf<Vertex>()
        result.second?.forEach { resCycle.add(it) }
        assertEquals(result.third, true)
        assertEquals(resCycle.size, cycle.size)
        cycle.forEach {
            assertEquals(cycle.contains(it), true)
            result.second?.remove(it)
        }
    }

    // Tests detection of negative cycle affecting the end of the path
    @Test
    fun `negative cycle at the end`() {
        val graph = Graph(true)
        val cycle: MutableSet<Vertex> = mutableSetOf()
        val start = graph.addVertex("0")
        graph.addVertex("1")
        cycle.add(graph.addVertex("2"))
        cycle.add(graph.addVertex("3"))
        val end = graph.addVertex("4")
        cycle.add(end)

        graph.addEdge(0, 1, 1)
        graph.addEdge(1, 2, 1)
        graph.addEdge(2, 3, -1)
        graph.addEdge(3, 4, -1)
        graph.addEdge(4, 2, -1)

        val result = fordBellman(graph, start, end)
        val resCycle = mutableSetOf<Vertex>()
        result.second?.forEach { resCycle.add(it) }
        assertEquals(result.third, true)
        assertEquals(resCycle.size, cycle.size)
        cycle.forEach {
            assertEquals(cycle.contains(it), true)
            result.second?.remove(it)
        }
    }
}
