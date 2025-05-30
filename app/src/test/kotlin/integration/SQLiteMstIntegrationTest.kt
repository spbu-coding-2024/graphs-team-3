package integration

import androidx.compose.ui.graphics.Color
import model.graph.Edge
import model.graph.Graph
import model.io.sqlite.SqliteRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Path
import viewmodel.representation.RepresentationStrategy
import viewmodel.screens.MainScreenViewModel
import viewmodel.representation.ForceAtlas2

class SQLiteMstIntegrationTest {
    private lateinit var repo: SqliteRepository
    private lateinit var representationStrategy: RepresentationStrategy
    private lateinit var graph: Graph

    @BeforeEach
    fun setup(@TempDir tempDir: Path) {
        val dbFile = tempDir.resolve("test.db").toFile().absolutePath
        repo = SqliteRepository(dbFile)
        graph = Graph()
        representationStrategy = ForceAtlas2()
    }

    /**
     * Test checks coloring of edges by basic scenario:
     * user creates graph -> save it to sqlite -> load it form sqlite -> use find mst algorithm
     */
    @Test
    fun `create graph, load to sqlite, read from there and find mst`() {
        val expected = mutableSetOf<Edge>()
        graph.addVertex("0")
        graph.addVertex("1")
        graph.addVertex("2")
        expected.add(graph.addEdge(0, 1, -1))
        expected.add(graph.addEdge(1, 2, 1))
        graph.addEdge(2, 0, 10)

        val stored = repo.save(graph, "kirilenko_top")
        val loaded = repo.read(stored)
        assertEquals(3, loaded.vertices.size)

        val vm = MainScreenViewModel(loaded, representationStrategy)
        vm.showMst()

        val expectedWeight = expected.sumOf { it.weight }
        val greenEdges = vm.graphViewModel.edges.filter { it.color == Color.Green }
        val grayEdges = vm.graphViewModel.edges.filter { it.color == Color.Gray }
        val greenEdgesWeight = greenEdges.sumOf { it.origin.weight }

        assertEquals(expectedWeight, greenEdgesWeight)
        assertEquals(loaded.edges.size - greenEdges.size, grayEdges.size)
    }
}
