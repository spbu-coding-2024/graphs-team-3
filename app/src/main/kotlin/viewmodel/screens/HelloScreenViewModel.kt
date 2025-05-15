package viewmodel.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.LocalNavigator
import model.graph.Graph
import model.io.neo4j.Neo4jRepository
import view.screens.Storage


class HelloScreenViewModel: ScreenViewModel {
    override val _storage = mutableStateOf<Storage?>(null)
    override val storage: State<Storage?> get() = _storage

    override val _uri = mutableStateOf<String?>(null)
    override val uri: State<String?> get() = _uri

    override val _username = mutableStateOf<String?>(null)
    override val username: State<String?> get() = _username

    override  val _password = mutableStateOf<String?>(null)
    override val password: State<String?> get() = _password

    private val _graph = mutableStateOf<Graph>(Graph())
    val graph: State<Graph> get() = _graph

    private val _exceptionDialog = mutableStateOf<Boolean>(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    private val _isMainScreen = mutableStateOf(false)
    val isMainScreen: State<Boolean> get() = _isMainScreen

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

    fun selectGraph(graph: Graph){
        _graph.value = graph
    }


    fun setExceptionDialog(exceptionDialog: Boolean){
        _exceptionDialog.value = exceptionDialog
    }

    fun setMessage(message: String){
        _message.value = message
    }

    fun setMainScreen(isMainScreen: Boolean){
        _isMainScreen.value = isMainScreen
    }

    fun onNeo4jConnect() {
        var neo4jRepo = Neo4jRepository(_uri.value ?: "", _username.value ?: "", _password.value ?: "")
        try {
            _graph.value = neo4jRepo.readFromDB()
            setMainScreen(true)
        } catch (exception: Exception) {
            setMessage(exception.message ?: "Unknown error")
            clearAuthData()
            _exceptionDialog.value = true
        }
    }
}
