package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.algo.findBridges
import model.algo.fordBellman
import model.algo.findCrucialVertices
import model.graph.Graph
import model.graph.Vertex
import viewmodel.colors.ColorTheme
import java.util.Vector

class GraphViewModel(
    val graph: Graph,
    showVertexLabels: State<Boolean>,
    showEdgeWeights: State<Boolean>,
    showVertexId: State<Boolean>,
) {
    internal val verticesMap =
        graph.vertices.associateWith { vertex ->
            VertexViewModel(0.dp, 0.dp, ColorTheme.VertexDefaultColor, vertex, showVertexLabels, showVertexId)
        }

    internal val edgesMap =
        graph.edges.associateWith { edge ->
            val first =
                verticesMap[edge.vertices.first]
                    ?: throw IllegalStateException("VertexView for ${edge.vertices.first} not found")
            val second =
                verticesMap[edge.vertices.second]
                    ?: throw IllegalStateException("VertexView for ${edge.vertices.second} not found")
            EdgeViewModel(first, second, ColorTheme.EdgeDefaultColor, edge, showEdgeWeights, graph.isDirected)
        }

    val vertices: Collection<VertexViewModel>
        get() = verticesMap.values

    val edges: Collection<EdgeViewModel>
        get() = edgesMap.values

    fun fordBellman(
        firstId: Int,
        secondId: Int,
    ) {
        val result =
            fordBellman(
                graph,
                graph.getVertex(firstId) ?: throw IllegalStateException("No vertex with id $firstId in graph"),
                graph.getVertex(secondId) ?: throw IllegalStateException("No vertex with id $secondId in graph"),
            )
        val path = result.first
        val cycle = result.second
        val isCycle = result.third

        val verticesForColoring = if (isCycle) {
            cycle ?: Vector<Vertex>()
        } else {
            path ?: Vector<Vertex>()
        }

        if (verticesForColoring.isEmpty()) {
            throw IllegalStateException("No path from vertex $firstId to vertex $secondId")
        }

        verticesForColoring.forEach { vertex ->
            verticesMap[vertex]?.color = ColorTheme.VertexPickedColor
        }

        var i = 0
        while (i < verticesForColoring.size - 1) {
            edgesMap[graph.getEdge(verticesForColoring[i], verticesForColoring[i + 1])]?.color = ColorTheme.EdgePickedColor
            i++
        }
    }

    fun findBridges() {
        val edgesForColoring = findBridges(graph)
        edgesForColoring.forEach { edge ->
            edgesMap[edge]?.color = ColorTheme.EdgePickedColor
        }
    }
    fun findCrucialVertices() {
        val crucialVertices = graph.findCrucialVertices()
        if (crucialVertices.isNotEmpty()) {
            resetColors()
            crucialVertices.forEach { vertex ->
                verticesMap[vertex]?.color = ColorTheme.VertexPickedColor
            }
        } else {
            throw IllegalStateException("No crucial vertices found (graph is empty)")
        }
    }


    fun highlightCycle(cycle: List<Vertex>) {
        resetColors()

        for (i in cycle.indices) {
            val currentVertex = cycle[i]
            val nextVertex = cycle[(i + 1) % cycle.size] //% нужен для случая, чтобы соединить size-1 вершину с исходной 0 (n%n==0)
            val edge = graph.getEdge(currentVertex, nextVertex)
            if (edge != null) {
                edgesMap[edge]?.color = ColorTheme.EdgePickedColor
            }
        }
    }


    fun resetColors() {
        verticesMap.values.forEach { it.color = ColorTheme.VertexDefaultColor }
        edgesMap.values.forEach { it.color = ColorTheme.EdgeDefaultColor }
    }
}