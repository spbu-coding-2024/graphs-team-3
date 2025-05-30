package algorithms

import model.algo.findBridges
import model.graph.Edge
import model.graph.Graph
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FindBridgesTest {
    /**
     * Graph without bridges
     */
    @Test
    fun `no bridges in graph`() {
        val graph = Graph()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")
        graph.addVertex("4")

        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(2, 3)
        graph.addEdge(3, 1)
        graph.addEdge(0, 4)
        graph.addEdge(1, 4)
        graph.addEdge(2, 4)
        graph.addEdge(3, 4)

        val result = findBridges(graph)

        assertTrue(result.isEmpty())
    }

    /**
     * One bridge in graph
     */
    @Test
    fun `one bridge in graph`() {
        val graph = Graph()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")
        graph.addVertex("4")
        graph.addVertex("5")

        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(2, 0)

        graph.addEdge(3, 4)
        graph.addEdge(4, 5)
        graph.addEdge(5, 3)

        val bridge = graph.addEdge(1, 4)

        val result = findBridges(graph)

        assertTrue(result.contains(bridge))
        assertTrue(result.size == 1)
    }

    /**
     * Graph has a point of articulation -> two bridges (edges from a point of articulatiom)
     */
    @Test
    fun `there is a point of articulation in the graph`() {
        val graph = Graph()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")
        graph.addVertex("4")
        graph.addVertex("5")
        graph.addVertex("6")

        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(2, 0)

        graph.addEdge(3, 4)
        graph.addEdge(4, 5)
        graph.addEdge(5, 3)

        val firstBridge = graph.addEdge(1, 6)
        val secondBridge = graph.addEdge(6, 4)

        val result = findBridges(graph)

        assertTrue(result.contains(firstBridge))
        assertTrue(result.contains(secondBridge))
        assertTrue(result.size == 2)
    }

    /**
     * In this case graph is a tree -> all edges are bridges
     */
    @Test
    fun `all edges in tree are bridges`() {
        val graph = Graph()

        val edgeSet: MutableSet<Edge> = mutableSetOf()

        for (i in 0..12) {
            graph.addVertex(i.toString())
        }

        edgeSet.add(graph.addEdge(0, 1))
        edgeSet.add(graph.addEdge(0, 2))
        edgeSet.add(graph.addEdge(1, 3))
        edgeSet.add(graph.addEdge(1, 4))
        edgeSet.add(graph.addEdge(1, 5))
        edgeSet.add(graph.addEdge(2, 6))
        edgeSet.add(graph.addEdge(2, 7))
        edgeSet.add(graph.addEdge(4, 8))
        edgeSet.add(graph.addEdge(4, 9))
        edgeSet.add(graph.addEdge(4, 10))
        edgeSet.add(graph.addEdge(5, 11))
        edgeSet.add(graph.addEdge(5, 12))

        val result = findBridges(graph)

        result.forEach {
            assertTrue(edgeSet.contains(it))
            edgeSet.remove(it)
        }
    }
}
