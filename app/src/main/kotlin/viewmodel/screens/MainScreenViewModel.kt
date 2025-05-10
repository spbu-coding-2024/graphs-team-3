package viewmodel.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import viewmodel.graph.GraphViweModel
import viewmodel.representation.RepresentationStrategy

class MainScreenViewModel(
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

    private val _exceptionDialog = mutableStateOf(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog


    val graphViewModel: GraphViweModel = GraphViweModel(graph, _showVerticesLabels, _showEdgesWeights, _showVerticesId)

    init {
        representationStrategy.place(800.0, 600.0, graphViewModel)
    }

    fun setExceptionDialog(exceptionDialog: Boolean) {
        _exceptionDialog.value = exceptionDialog
    }

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    fun resetGraphView() {
        representationStrategy.place(800.0, 600.0, graphViewModel)
        graphViewModel.vertices.forEach { v -> v.color = Color.Gray }
    }
}
