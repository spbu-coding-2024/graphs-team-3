package integration

import androidx.compose.runtime.mutableStateOf
import model.graph.Edge
import model.graph.Graph
import org.junit.jupiter.api.Test
import viewmodel.colors.ColorTheme
import viewmodel.graph.GraphViewModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class FordBellmanAndBridgeFindIntedrationTest {
    @Test
    fun `algorithm coloring test`() {
        val bridges = mutableSetOf<Edge>()
        val firstId = 0
        val secondId = 7
        val thirdId = 9
        val graph = Graph()
        for (i in 0..9) {
            graph.addVertex(i.toString())
        }
        bridges.add(graph.addEdge(0, 1))
        bridges.add(graph.addEdge(0, 2))
        graph.addEdge(1, 3)
        graph.addEdge(1, 4)
        graph.addEdge(3, 5)
        graph.addEdge(3, 7)
        graph.addEdge(4, 5)
        bridges.add(graph.addEdge(5, 6))
        graph.addEdge(5, 8)
        graph.addEdge(7, 8)

        val graphViewModel = GraphViewModel(
            graph,
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false)
        )

        graphViewModel.findBridges()

        graphViewModel._edges.keys.forEach { edge ->
            if (bridges.contains(edge)) {
                assertEquals(graphViewModel._edges[edge]?.color, ColorTheme.edgePickedColor)
            } else {
                assertEquals(graphViewModel._edges[edge]?.color, ColorTheme.edgeDefaultColor)
            }
        }

        graphViewModel.fordBellman(firstId, secondId)

        graphViewModel._vertices.keys.forEach { vertex ->
            if (vertex.id == 0 || vertex.id == 1 || vertex.id == 3 || vertex.id == 7) {
                assertEquals(graphViewModel._vertices[vertex]?.color, ColorTheme.vertexPickedColor)
            } else {
                assertEquals(graphViewModel._vertices[vertex]?.color, ColorTheme.vertexDefaultColor)
            }
        }

        val exception = assertThrows<IllegalStateException> { graphViewModel.fordBellman(firstId, thirdId) }
        assertEquals(exception.message, "No path from vertex $firstId to vertex $thirdId")
    }
}