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
import view.screens.Storage
import kotlin.random.Random

class MainScreenViewModel (
    private val graph: Graph,
    private val representationStrategy: RepresentationStrategy,
): ScreenViewModel {
    override val _storage = mutableStateOf<Storage?>(null)
    override val storage: State<Storage?> get() = _storage

    override val _uri = mutableStateOf<String?>(null)
    override val uri: State<String?> get() = _uri

    override val _username = mutableStateOf<String?>(null)
    override val username: State<String?> get() = _username

    override  val _password = mutableStateOf<String?>(null)
    override val password: State<String?> get() = _password
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

    override fun selectStorage(storage: Storage?){
        _storage.value = storage
    }

    override fun setUri(uri: String?){
        _uri.value = uri
    }

    override fun setUsername(username: String?){
        _username.value = username
    }

    override fun setPassword(password: String?){
        _password.value = password
    }

    override fun clearAuthData() {
        _uri.value = null
        _username.value = null
        _password.value = null
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
        try {
            resetColors()
            val mst = findMST(graph)
            graphViewModel.edges.forEach { e ->
                e.color = if (e.origin in mst) Color.Green else Color.Gray
            }
        } catch (e: Exception) {
            setMessage(e.message)
            clearId()
            setExceptionDialog(true)
        }
    }

    fun showCommunities() {
        try {
            resetColors()
            val comm = findCommunities(graph)
            val palette = generatePalette(comm.size)

            comm.values.forEachIndexed { idx, verts ->
                val color = palette[idx]
                verts.forEach { v ->
                    graphViewModel.vertices
                        .first { it.origin == v }
                        .color = color
                }
            }
        } catch (e: Exception) {
            setMessage(e.message)
            clearId()
            setExceptionDialog(true)
        }
    }

    fun showScc() {
        try {
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
        } catch (e: Exception) {
            setMessage(e.message)
            clearId()
            setExceptionDialog(true)
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