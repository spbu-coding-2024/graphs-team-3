package viewmodel.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import org.gephi.graph.api.GraphView
import view.graph.GraphView
import viewmodel.graph.GraphViweModel
import viewmodel.representation.RepresentationStrategy

class MainScreenViewModel (
    graph: Graph,
    private val representationStrategy: RepresentationStrategy
) {
    private var _showVerticesLabels = mutableStateOf(false)
    var showVerticesLabels: Boolean
        get() = _showVerticesLabels.value
        set(value) {
            _showVerticesLabels.value = value
        }

    private var _showEdgesWeights = mutableStateOf(false)
    var showEdgesWeights: Boolean
        get() = _showEdgesWeights.value
        set(value) {
            _showEdgesWeights.value = value
        }

    private var _showVerticesId = mutableStateOf(false)
    var showVerticesId: Boolean
        get() = _showVerticesId.value
        set(value) {
            _showVerticesId.value = value
        }


    val graphViewModel: GraphViweModel = GraphViweModel(graph, _showVerticesLabels, _showEdgesWeights, _showVerticesId)

    init {
        representationStrategy.place(800.0, 600.0, graphViewModel.vertices)
    }

    fun resetGraphView() {
        representationStrategy.place(800.0, 600.0, graphViewModel.vertices)
        graphViewModel.vertices.forEach { v -> v.color = Color.Gray }
    }
}