package viewmodel.screens

import androidx.compose.runtime.*
import model.graph.Graph
import model.io.sqlite.SqliteRepository
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import kotlin.io.path.Path
import kotlin.io.path.exists

class SqliteViewModel(private var repo: SqliteRepository) {

    companion object {
        private val DB_EXTENSIONS = listOf(".db", ".sqlite", ".sqlite3", ".db3", ".s3db")
    }

    var filter by mutableStateOf("")
        private set

    val graphs = mutableStateListOf<Pair<Int, String>>()

    private val _exceptionDialog = mutableStateOf(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    private val _toDelete = mutableStateOf<Pair<Int, String>?>(null)
    val toDelete: State<Pair<Int, String>?> get() = _toDelete

    private val _deleteDialog = mutableStateOf(false)
    val deleteDialog: State<Boolean> get() = _deleteDialog

    init { reload() }

    fun onFilterChange(searchQuery: String) {
        filter = searchQuery
        reload()
    }

    private fun reload() {
        graphs.clear()
        graphs += repo.listGraphs(filter)
    }

    fun openGraph(id: Int): Graph = repo.read(id)

    fun setExceptionDialog(exceptionDialog: Boolean) {
        _exceptionDialog.value = exceptionDialog
    }

    fun setMessage(message: String) {
        _message.value = message
    }

    fun startDelete(id: Int, name: String) {
        _toDelete.value = id to name
        _deleteDialog.value = true
    }

    fun confirmDelete() {
        _toDelete.value?.let { (id, _) -> repo.delete(id) }
        reload(); cancelDelete()
    }

    fun cancelDelete() {
        _deleteDialog.value = false
        _toDelete.value = null
    }

    fun chooseFile() {
        val oldRepo = repo
        try {
            val dialog = FileDialog(null as Frame?, "Choose database file", FileDialog.LOAD)
            dialog.filenameFilter = FilenameFilter { _, name ->
                DB_EXTENSIONS.any { ext -> name.endsWith(ext) }
            }
            dialog.isVisible = true

            dialog.file?.let { fileName ->
                val fullPath = File(dialog.directory, fileName).absolutePath
                if (Path(fullPath).exists()) {
                    val newRepo = SqliteRepository(fullPath)
                    repo = newRepo
                    reload()
                }
            }
        } catch (e: Exception) {
            repo = oldRepo
            reload()
            setMessage(e.message ?: "Unknown error")
            setExceptionDialog(true)
        }
    }
}