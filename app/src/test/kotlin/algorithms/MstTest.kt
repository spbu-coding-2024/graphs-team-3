package algorithms

import model.algo.findMST
import model.graph.Edge
import model.graph.Graph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class MstTest {
    /**
     * Chain produce one mst
     */
    @Test
    fun `all edges in mst`() {
        val graph = Graph()
        val expected = mutableSetOf<Edge>()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")

        expected.add(graph.addEdge(0,1))
        expected.add(graph.addEdge(1,2))
        expected.add(graph.addEdge(2,3))

        val result = findMST(graph)
        assertEquals(expected, result)
    }

    /**
     * If there is no edges there is no mst
     */
    @Test
    fun `graph is not connected`() {
        val graph = Graph()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")

        assertThrows<IllegalStateException> {
            findMST(graph)
        }
    }

    /**
     * Mst cannot be find in directed graphs
     */
    @Test
    fun `directed graph throws exception`() {
        val graph = Graph(true)
        assertThrows<IllegalArgumentException> {
            findMST(graph)
        }
    }

    /**
     * No edges - no mst
     */
    @Test
    fun `single vertex returns empty mst`() {
        val graph = Graph()
        graph.addVertex("0")
        assertTrue(findMST(graph).isEmpty())
    }

    /**
     * Test checks if negative weight is supported
     */
    @Test
    fun `negative weight is supported`() {
        val graph = Graph()
        val expected = mutableSetOf<Edge>()

        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")

        expected.add(graph.addEdge(0, 1, -5))
        expected.add(graph.addEdge(1, 2, 2))

        val result = findMST(graph)
        assertEquals(expected, result)
    }

    /**
     * If there is no graph - mst is empty
     */
    @Test
    fun `empty graph - empty mst`() {
        val graph = Graph()
        assertTrue(findMST(graph).isEmpty())
    }
}