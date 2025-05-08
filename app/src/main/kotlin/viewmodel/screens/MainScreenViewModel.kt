package viewmodel.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import viewmodel.graph.GraphViweModel
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


    val graphViewModel: GraphViweModel = GraphViweModel(graph, _showVerticesLabels, _showEdgesWeights, _showVerticesId)

    init {
        representationStrategy.place(800.0, 600.0, graphViewModel)
    }

    fun resetGraphView() {
        representationStrategy.place(800.0, 600.0, graphViewModel)
        graphViewModel.vertices.forEach { v -> v.color = Color.Gray }
    }

    fun showBridges() {
        val edges = findBridges(graph)
        graphViewModel.edges.forEach { e ->
            if (e.origin in edges) e.color = Color.Red
        }
    }

    fun showMst() {
        val mst = findMST(graph)
        graphViewModel.edges.forEach { e ->
            e.color = if (e.origin in mst) Color.Green else Color.LightGray
        }
    }

    fun showCommunities() {
        val comm = labelPropagation(graph)
        val palette = generateSequence { Color(Random.nextInt()) }.distinct().iterator()
        comm.values.forEach { verts ->
            val c = palette.next()
            verts.forEach { v ->
                graphViewModel.vertices.first { it.origin == v }.color = c
            }
        }
    }

    fun showScc() {
        val scc = findSCC(graph)
        val palette = generateSequence { Color(Random.nextInt()) }.distinct().iterator()
        scc.forEach { comp ->
            val c = palette.next()
            comp.forEach { v ->
                graphViewModel.vertices.first { it.origin == v }.color = c
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
}