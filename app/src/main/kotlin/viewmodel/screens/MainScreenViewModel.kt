package viewmodel.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.representation.RepresentationStrategy
import model.algo.*
import model.algo.findBridges.findBridges
import model.algo.findSCC
import model.io.sqlite.SqliteRepository
import kotlin.random.Random

class MainScreenViewModel (
    private val graph: Graph,
    private val representationStrategy: RepresentationStrategy,
    private var sqliteRepo: SqliteRepository? = null,
    private var savedId: Int? = null,
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


    val graphViewModel: GraphViewModel = GraphViewModel(graph, _showVerticesLabels, _showEdgesWeights, _showVerticesId)

    init {
        representationStrategy.place(800.0, 600.0, graphViewModel)
    }

    private fun resetColors() {
        graphViewModel.vertices.forEach { v -> v.color = Color.Gray }
        graphViewModel.edges.forEach { e -> e.color = Color.Black }
    }

    fun resetGraphView() {
        representationStrategy.place(800.0, 600.0, graphViewModel)
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

    fun saveToDb(name: String? = null) {
        sqliteRepo ?: return
        savedId = if (savedId == null)
            sqliteRepo!!.save(graph, name)
        else {
            sqliteRepo!!.update(savedId!!, graph, name)
            savedId
        }
    }

    private fun generatePalette(count: Int): List<Color> {
        return List(count) { i ->
            val hue = i * 360f / count
            val saturation = 0.7f
            val value = 0.9f
            Color.hsv(hue, saturation, value)
        }
    }
}