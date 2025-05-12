package viewmodel.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import view.dialogs.FordBellmanDialog
import viewmodel.colors.ColorTheme
import viewmodel.graph.GraphViewModel
import viewmodel.representation.RepresentationStrategy
import model.algo.*
import model.algo.findSCC
import model.io.sqlite.SqliteRepository
import kotlin.random.Random

class MainScreenViewModel (
    private val graph: Graph,
    private val representationStrategy: RepresentationStrategy,
//    private var sqliteRepo: SqliteRepository? = null, // unused
//    private var savedId: Int? = null,
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

    private val _firstId = mutableStateOf<String?>(null)
    val firstId: State<String?> get() = _firstId

    private val _secondId = mutableStateOf<String?>(null)
    val secondId: State<String?> get() = _secondId

    val graphViewModel: GraphViewModel = GraphViewModel(graph, _showVerticesLabels, _showEdgesWeights, _showVerticesId)

    init {
        representationStrategy.place(800.0, 600.0, graphViewModel)
    }

    fun setExceptionDialog(exceptionDialog: Boolean) {
        _exceptionDialog.value = exceptionDialog
    }

    fun setFirstId(firstId: String?) {
        _firstId.value = firstId
    }

    fun setSecondId(secondId: String?) {
        _secondId.value = secondId
    }

    fun clearId() {
        _firstId.value = null
        _secondId.value = null
    }

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    fun setMessage(message: String?) {
        _message.value = message
    }

    fun resetGraphView() {
        representationStrategy.place(800.0, 600.0, graphViewModel)
        graphViewModel.vertices.forEach { v -> v.color = ColorTheme.vertexDefaultColor }
        graphViewModel.edges.forEach { v -> v.color = ColorTheme.edgeDefaultColor }
        resetColors()
    }

    fun showMst() {
        resetColors()
        val mst = findMST(graph)
        graphViewModel.edges.forEach { e ->
            e.color = if (e.origin in mst) Color.Green else Color.LightGray
        }
    }

    fun showCommunities() {
        resetColors()
        val comm = labelPropagation(graph)
        val palette = generatePalette(comm.size)

        comm.values.forEachIndexed { idx, verts ->
            val color = palette[idx]
            verts.forEach { v ->
                graphViewModel.vertices
                    .first { it.origin == v }
                    .color = color
            }
        }
    }

    fun showScc() {
        resetColors()
        val scc = findSCC(graph)
        val palette = generatePalette(scc.size)

        scc.forEachIndexed { idx, component ->
            val color = palette[idx]
            component.forEach { v ->
                graphViewModel.vertices
                    .first { it.origin == v }
                    .color = color
            }
        }
    }

//    fun saveToDb(name: String? = null) { // Unused now
//        sqliteRepo ?: return
//        savedId = if (savedId == null)
//            sqliteRepo!!.save(graph, name)
//        else {
//            sqliteRepo!!.update(savedId!!, graph, name)
//            savedId
//        }
//    }

    private fun generatePalette(count: Int): List<Color> {
        return List(count) { i ->
            val hue = i * 360f / count
            val saturation = 0.7f
            val value = 0.9f
            Color.hsv(hue, saturation, value)
        }
    }

    fun resetColors() {
        graphViewModel.vertices.forEach { v -> v.color = ColorTheme.vertexDefaultColor }
        graphViewModel.edges.forEach { e -> e.color = ColorTheme.edgeDefaultColor }
    }

    fun onFordBellmanRun() {
        try {
            resetColors()
            graphViewModel.fordBellman(
                firstId.value?.toInt()
                    ?: throw IllegalStateException("First vertex id must not be null"),
                secondId.value?.toInt()
                    ?: throw IllegalStateException("Second vertex id must not be null")
            )
            clearId()
        } catch (e: Exception) {
            setMessage(e.message)
            clearId()
            setExceptionDialog(true)
        }
    }
}