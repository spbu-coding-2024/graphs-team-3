package viewmodel.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.graph.Graph
import model.io.neo4j.Neo4jRepository
import view.screens.Storage

class HelloScreenViewModel {
    private val _storage = mutableStateOf<Storage?>(null)
    val storage: State<Storage?> get() = _storage

    private val _uri = mutableStateOf<String?>(null)
    val uri: State<String?> get() = _uri

    private val _username = mutableStateOf<String?>(null)
    val username: State<String?> get() = _username

    private val _password = mutableStateOf<String?>(null)
    val password: State<String?> get() = _password

    private val _graph = mutableStateOf<Graph>(Graph())
    val graph: State<Graph> get() = _graph

    private val _exceptionDialog = mutableStateOf<Boolean>(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    fun selectStorage(storage: Storage?){
        _storage.value = storage
    }

    fun setUri(uri: String?){
        _uri.value = uri
    }

    fun setUsername(username: String?){
        _username.value = username
    }

    fun setPassword(password: String?){
        _password.value = password
    }

    fun selectGraph(graph: Graph){
        _graph.value = graph
    }

    fun clearAuthData() {
        _uri.value = null
        _username.value = null
        _password.value = null
    }

    fun setExceptionDialog(exceptionDialog: Boolean){
        _exceptionDialog.value = exceptionDialog
    }

    fun setMessage(message: String){
        _message.value = message
    }

    fun onNeo4jConnect() {
        var neo4jRepo = Neo4jRepository(_uri.value ?: "", _username.value ?: "", _password.value ?: "")
        try {
            _graph.value = neo4jRepo.readFromDB()
        } catch (exception: Exception) {
            setMessage(exception.message ?: "Unknown error")
            clearAuthData()
            _exceptionDialog.value = true
        }
    }
}
