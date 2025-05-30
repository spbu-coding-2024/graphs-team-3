package io

import model.graph.Graph
import model.io.sqlite.SqliteRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class SqliteTest {
    private lateinit var repo: SqliteRepository

    @BeforeEach
    fun setup(@TempDir tempDir: Path) {
        val dbFile = tempDir.resolve("test.db").toFile().absolutePath
        repo = SqliteRepository(dbFile)
    }

    /**
     * Simple test for saving empty graph
     */
    @Test
    fun `save empty graph`() {
        val id = repo.save(Graph(), "empty")
        val restored = repo.read(id)
        assertTrue(restored.vertices.isEmpty())
    }

    /**
     * Round-trip with undirected graph
     */
    @Test
    fun `save and read undirected graph`() {
        val graph = Graph()
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge(0, 1)

        val id = repo.save(graph, "52")
        val restored = repo.read(id)

        assertFalse(restored.isDirected)
        assertEquals(2, restored.vertices.size)
        assertEquals(1, restored.edges.size)
    }

    /**
     * Round-trip with directed graph
     */
    @Test
    fun `save and read directed graph`() {
        val graph = Graph(true)
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge(0, 1)

        val id = repo.save(graph, "42")
        val restored = repo.read(id)

        assertTrue(restored.isDirected)
        assertEquals(2, restored.vertices.size)
        assertEquals(1, restored.edges.size)
    }

    /**
     * Test checks if update function delete previous graph
     */
    @Test
    fun `update overrides graph and name`() {
        val original = Graph()
        original.addVertex("a")
        original.addVertex("b")
        original.addEdge(0, 1, 4)

        val id = repo.save(original, "original")

        val new = Graph(true)
        new.addVertex("c")
        new.addVertex("d")
        new.addEdge(0, 1, -1)

        repo.update(id, new, "new")

        val restored = repo.read(id)
        assertTrue(restored.isDirected)
        assertEquals(-1, restored.edges.first().weight)
        val actualName = repo.listGraphs().single { it.first == id }.second
        assertEquals("new", actualName)
    }

    /**
     * Deletion of whole graph. Cannot read it after delete
     */
    @Test
    fun `delete removes graph entirely`() {
        val graph = Graph()
        graph.addVertex("a")
        val id = repo.save(graph, "to delete")
        repo.delete(id)

        assertTrue(repo.listGraphs().none { it.first == id })
        assertThrows<NoSuchElementException> { repo.read(id) }
    }

    /**
     * Listing function filter properly and list in alphabetical order
     */
    @Test
    fun `listGraphs ordered alphabetically and filterable`() {
        repo.save(Graph(), "b")
        repo.save(Graph(), "a")
        repo.save(Graph(), "abc")

        val allNames = repo.listGraphs().map { it.second }
        assertEquals(listOf("a", "abc", "b"), allNames)

        val filtered = repo.listGraphs("a").map { it.second }
        assertEquals(listOf("a", "abc"), filtered)
    }
}