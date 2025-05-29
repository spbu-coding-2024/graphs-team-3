package viewmodel.screens

import androidx.compose.runtime.*
import model.graph.Graph
import model.io.sqlite.SqliteRepository

class SqliteViewModel(
    private val repo: SqliteRepository,
) {
    var filter by mutableStateOf("")
        private set
    val graphs = mutableStateListOf<Pair<Int, String>>()

    init {
        reload()
    }

    fun onFilterChange(searchQuery: String) {
        filter = searchQuery
        reload()
    }

    private fun reload() {
        graphs.clear()
        graphs += repo.listGraphs(filter)
    }

    fun openGraph(id: Int): Graph = repo.read(id)

    fun deleteGraph(id: Int) {
        repo.delete(id)
        reload()
    }
}
