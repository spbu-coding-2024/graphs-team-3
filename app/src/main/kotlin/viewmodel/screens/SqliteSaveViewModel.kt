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

class SqliteSaveViewModel(
    private var repo: SqliteRepository,
    private val graph: Graph,
) {
    companion object {
        private val DB_EXTENSIONS = listOf(".db", ".sqlite", ".sqlite3", ".db3", ".s3db")
    }

    val graphs = mutableStateListOf<Pair<Int, String>>()
    val nameState = mutableStateOf("")

    val overwriteDialog: MutableState<Int?> = mutableStateOf(null)

    var filter by mutableStateOf("")
        private set

    private val _exceptionDialog = mutableStateOf(false)
    val exceptionDialog: State<Boolean> get() = _exceptionDialog

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> get() = _message

    init {
        reload()
    }

    private fun reload() {
        graphs.clear()
        graphs += repo.listGraphs(filter)
    }

    fun onNameChange(newName: String) {
        nameState.value = newName
        filter = newName
        reload()
    }

    fun onGraphClicked(name: String) {
        nameState.value = name
    }

    fun onSaveClick(): Boolean {
        val name = nameState.value.trim()
        if (name.isBlank()) {
            setMessage("Name must not be empty")
            setExceptionDialog(true)
            return false
        }
        val duplicate = graphs.firstOrNull { it.second == name }
        if (duplicate != null) {
            overwriteDialog.value = duplicate.first
            return false
        }
        saveNew(name)
        return true
    }

    fun confirmOverwrite() {
        overwriteDialog.value?.let { gid ->
            try {
                repo.update(gid, graph, nameState.value.trim())
                overwriteDialog.value = null
            } catch (e: Exception) {
                setMessage(e.message ?: "Unknown error")
                setExceptionDialog(true)
            }
        }
    }

    fun cancelOverwrite() {
        overwriteDialog.value = null
    }

    private fun saveNew(name: String) {
        try {
            repo.save(graph, name)
        } catch (e: Exception) {
            setMessage(e.message ?: "Unknown error")
            setExceptionDialog(true)
        }
    }

    fun chooseFile() {
        val oldRepo = repo
        try {
            val dialog = FileDialog(null as Frame?, "Choose database file", FileDialog.LOAD)
            dialog.filenameFilter =
                FilenameFilter { _, fname ->
                    DB_EXTENSIONS.any { ext -> fname.endsWith(ext) }
                }
            dialog.isVisible = true
            dialog.file?.let { fileName ->
                val fullPath = File(dialog.directory, fileName).absolutePath
                if (Path(fullPath).exists()) {
                    repo = SqliteRepository(fullPath)
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

    fun setExceptionDialog(exceptionDialog: Boolean) {
        _exceptionDialog.value = exceptionDialog
    }

    fun setMessage(message: String) {
        _message.value = message
    }
}
