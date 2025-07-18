package algorithms

import model.algo.findCommunities
import model.graph.Graph
import model.graph.Vertex
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CommunitiesTest {
    /**
     * Test checks whether graph is complete there is certainly one community
     */
    @Test
    fun `one community, complete graph`() {
        val graph = Graph()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        graph.addVertex("3")

        graph.addEdge(1,0)
        graph.addEdge(1,2)
        graph.addEdge(1,3)
        graph.addEdge(0,2)
        graph.addEdge(0,3)
        graph.addEdge(2,3)

        assertEquals(1, findCommunities(graph).size)
    }

    /**
     * Each vertex from 5000 is its own community
     */
    @Test
    fun `5000 communities, no edges`() {
        val graph = Graph()
        for (i in 1..5000) {
            graph.addVertex(i.toString())
        }
        val result = findCommunities(graph).size
        assertEquals( 5000, result)
    }

    /**
     * No graph - no communities
     */
    @Test
    fun `empty graph, no communities`() {
        val graph = Graph()
        assertTrue(findCommunities(graph).isEmpty())
    }

    /**
     * three vertices connected with each other
     */
    @Test
    fun `two communities connected with one edge`() {
        val graph = Graph()
        val firstComm = mutableSetOf<Vertex>()
        val secondComm = mutableSetOf<Vertex>()

        firstComm.add(graph.addVertex("0"))
        firstComm.add(graph.addVertex("1"))
        firstComm.add(graph.addVertex("2"))
        secondComm.add(graph.addVertex("3"))
        secondComm.add(graph.addVertex("4"))
        secondComm.add(graph.addVertex("5"))

        graph.addEdge(1,0)
        graph.addEdge(0,2)
        graph.addEdge(2,1)
        graph.addEdge(3,4)
        graph.addEdge(4,5)
        graph.addEdge(5,3)
        graph.addEdge(0,3)

        assertEquals(firstComm, findCommunities(graph).getValue(0).toSet())
        assertEquals(secondComm, findCommunities(graph).getValue(1).toSet())
    }

    /**
     * One vertex - its own community
     */
    @Test
    fun `single isolated vertex is its own community`() {
        val graph = Graph()
        graph.addVertex("0")
        assertEquals(1, findCommunities(graph).size)
    }
}