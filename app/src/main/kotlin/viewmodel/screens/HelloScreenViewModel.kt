package viewmodel.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.LocalNavigator
import model.graph.Graph
import model.io.neo4j.Neo4jRepository
import model.utils.randomGraph
import view.screens.Storage
import kotlin.math.max


class HelloScreenViewModel : ScreenViewModel {
    override val _storage = mutableStateOf<Storage?>(null)
    override val storage: State<Storage?> get() = _storage

    override val _uri = mutableStateOf<String?>(null)
    override val uri: State<String?> get() = _uri

    override val _username = mutableStateOf<String?>(null)
    override val username: State<String?> get() = _username

    override val _password = mutableStateOf<String?>(null)
    override val password: State<String?> get() = _password

    private val _graph = mutableStateOf<Graph>(Graph())
    val graph: State<Graph> get() = _graph

    private val _exceptionDialog = mutableStateOf<Boolean>(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    private val _isMainScreen = mutableStateOf(false)
    val isMainScreen: State<Boolean> get() = _isMainScreen

    private val _randomDirected = mutableStateOf(false)
    val randomDirected: State<Boolean> get() = _randomDirected

    private val _randomWeighted = mutableStateOf(false)
    val randomWeighted: State<Boolean> get() = _randomWeighted

    private val _vertexCount = mutableStateOf<String?>(null)
    val vertexCount: State<String?> get() = _vertexCount

    private val _edgeMaxCount = mutableStateOf<String?>(null)
    val edgeMaxCount: State<String?> get() = _edgeMaxCount

    private val _maxWeight = mutableStateOf<String?>(null)
    val maxWeight: State<String?> get() = _maxWeight

    private val _isRandom = mutableStateOf(false)
    val isRandom: State<Boolean> get() = _isRandom

    override fun selectStorage(storage: Storage?) {
        _storage.value = storage
    }

    override fun setUri(uri: String?) {
        _uri.value = uri
    }

    override fun setUsername(username: String?) {
        _username.value = username
    }

    override fun setPassword(password: String?) {
        _password.value = password
    }

    override fun clearAuthData() {
        _uri.value = null
        _username.value = null
        _password.value = null
    }

    fun selectGraph(graph: Graph) {
        _graph.value = graph
    }


    fun setExceptionDialog(exceptionDialog: Boolean) {
        _exceptionDialog.value = exceptionDialog
    }

    fun setMessage(message: String) {
        _message.value = message
    }

    fun setMainScreen(isMainScreen: Boolean) {
        _isMainScreen.value = isMainScreen
    }

    fun setDirected(isDirected: Boolean) {
        _randomDirected.value = isDirected
    }

    fun setWeighted(isWeighted: Boolean) {
        _randomWeighted.value = isWeighted
    }

    fun setVertexCount(vertexCount: String?) {
        _vertexCount.value = vertexCount
    }

    fun setEdgeMaxCount(edgeMaxCount: String?) {
        _edgeMaxCount.value = edgeMaxCount
    }

    fun setMaxWeight(isMaxWeight: String?) {
        _maxWeight.value = isMaxWeight
    }

    fun setIsRandom(isRandom: Boolean) {
        _isRandom.value = isRandom
    }

    fun clearProperties() {
        _vertexCount.value = null
        _edgeMaxCount.value = null
        _randomDirected.value = false
        _randomWeighted.value = false
        _maxWeight.value = null
    }

    fun onNeo4jConnect() {
        var neo4jRepo = Neo4jRepository(_uri.value ?: "", _username.value ?: "", _password.value ?: "")
        try {
            _graph.value = neo4jRepo.readFromDB()
            setMainScreen(true)
        } catch (exception: Exception) {
            setMessage(exception.message ?: "Unknown error")
            clearAuthData()
            setExceptionDialog(true)
        }
    }

    fun onRandom() {
        try {
            val vertexCountCast = vertexCount.value?.toInt() ?: 0
            val edgeMaxCountCast = edgeMaxCount.value?.toInt() ?: 0
            val maxWeightCast = maxWeight.value?.toLong() ?: 1

            if (edgeMaxCountCast > vertexCountCast) {
                throw IllegalStateException("Max edges count can`t be greater than vertex count")
            }
            _graph.value = randomGraph(
                randomDirected.value,
                randomWeighted.value,
                vertexCountCast,
                edgeMaxCountCast,
                maxWeightCast
            )
            setMainScreen(true)
        } catch (e: Exception) {
            setMessage(e.message ?: "Unknown error")
            clearProperties()
            setIsRandom(false)
            setExceptionDialog(true)
        }
    }
}
