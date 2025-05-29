package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.unit.dp
import model.graph.Graph
import viewmodel.colors.ColorTheme
import model.algo.fordBellman.fordBellman
import model.graph.Vertex
import java.util.Vector
import model.algo.findBridges.findBridges
class GraphViewModel(
    val graph: Graph,
    showVertexLabels: State<Boolean>,
    showEdgeWeights: State<Boolean>,
    showVertexId: State<Boolean>,
) {

    internal val _vertices = graph.vertices.associateWith { vertex ->
        VertexViewModel(0.dp, 0.dp, ColorTheme.vertexDefaultColor, vertex, showVertexLabels, showVertexId)
    }

    internal val _edges = graph.edges.associateWith { edge ->
        val first = _vertices[edge.vertices.first]
            ?: throw IllegalStateException("VertexView for ${edge.vertices.first} not found")
        val second = _vertices[edge.vertices.second]
            ?: throw IllegalStateException("VertexView for ${edge.vertices.second} not found")
        EdgeViewModel(first, second, ColorTheme.edgeDefaultColor, edge, showEdgeWeights, graph.isDirected)
    }

    val vertices: Collection<VertexViewModel>
        get() = _vertices.values

    val edges: Collection<EdgeViewModel>
        get() = _edges.values

    fun fordBellman(firstId: Int, secondId: Int) {
        val result = fordBellman(
            graph,
            graph.getVertex(firstId) ?: throw IllegalStateException("No vertex with id $firstId in graph"),
            graph.getVertex(secondId) ?: throw IllegalStateException("No vertex with id $secondId in graph")
        )
        val path = result.first
        val cycle = result.second
        val isCycle = result.third

        var verticesForColoring = path ?: Vector<Vertex>()

        if (isCycle) {
            verticesForColoring = cycle ?: Vector<Vertex>()
        }

        if (verticesForColoring.size == 0) {
            throw IllegalStateException("No path from vertex $firstId to vertex $secondId")
        }

        verticesForColoring.forEach { vertex ->
            _vertices[vertex]?.color = ColorTheme.vertexPickedColor
        }

        var i = 0
        while (i < verticesForColoring.size - 1) {
            _edges[graph.getEdge(verticesForColoring[i], verticesForColoring[i + 1])]?.color = ColorTheme.edgePickedColor
            i++
        }
    }

    fun findBridges() {
        var edgesForColoring = findBridges(graph)
        edgesForColoring.forEach { edge ->
            _edges[edge]?.color = ColorTheme.edgePickedColor
        }
    }
}