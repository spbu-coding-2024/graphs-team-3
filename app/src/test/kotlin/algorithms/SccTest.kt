package algorithms

import model.algo.findSCC
import model.graph.Graph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SccTest {
    @Test
    fun `empty graph no scc`() {
        assertTrue(findSCC(Graph()).isEmpty())
    }

    @Test
    fun `single vertex graph`() {
        val graph = Graph()
        graph.addVertex("0")
        val scc = findSCC(graph)
        assertEquals(1, scc.size)
        assertEquals(1, scc.first().size)
    }

    @Test
    fun `5000 scc, no edges`() {
        val graph = Graph()
        for (i in 1..5000) {
            graph.addVertex(i.toString())
        }
        val result = findSCC(graph).size
        assertEquals( 5000, result)
    }

    @Test
    fun `directed cycle forms one scc`() {
        val graph = Graph(true)
        for (i in 0 until 100) {
            graph.addVertex(i.toString())
        }

        for (i in 0 until 100) {
            val from = i
            val to = (i + 1) % 100
            graph.addEdge(from, to)
        }
        val scc = findSCC(graph)
        assertEquals(1, scc.size)
        assertEquals(100, scc.first().size)
    }

    @Test
    fun `chain in directed graph produces 100 scc`() {
        val graph = Graph(true)
        for (i in 0 until 100) {
            graph.addVertex(i.toString())
        }

        for (i in 0 until 99) {
            graph.addEdge(i, i + 1)
        }
        val scc = findSCC(graph)
        assertEquals(100, scc.size)
        assertEquals(1, scc.first().size)
    }
}