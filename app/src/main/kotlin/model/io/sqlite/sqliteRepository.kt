package model.io.sqlite

import model.graph.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.io.File

class SqliteRepository(dbPath: String = "graphs.db") {

    object Graphs : IntIdTable("graphs") {
        val name = varchar("name", 52)
        val isDirected = bool("isDirected")
    }

    object Vertices : IntIdTable("vertices") {
        val graph = reference("graph_id", Graphs, onDelete = ReferenceOption.CASCADE)
        val label = varchar("label", 255)
        val origId = integer("orig_id")
    }

    object Edges : IntIdTable("edges") {
        val graph = reference("graph_id", Graphs, onDelete = ReferenceOption.CASCADE)
        val origId = long("orig_id")
        val fromId = integer("from_id")
        val toId = integer("to_id")
        val weight = long("weight")
    }

    init {
        File(dbPath).parentFile?.mkdirs()
        Database.connect("jdbc:sqlite:$dbPath?foreign_keys=ON", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(Graphs, Vertices, Edges)
        }
    }

    fun save(g: Graph, name: String? = null): Int = transaction {
        val gId = Graphs.insertAndGetId {
            it[this.name] = name ?: "Graph_${System.currentTimeMillis()}"
            it[this.isDirected] = g.isDirected
        }.value

        Vertices.batchInsert(g.vertices) { vertex ->
            this[Vertices.graph] = gId
            this[Vertices.label] = vertex.label
            this[Vertices.origId] = vertex.id
        }

        Edges.batchInsert(g.edges) { edge ->
            this[Edges.graph] = gId
            this[Edges.fromId] = edge.vertices.first.id
            this[Edges.toId] = edge.vertices.second.id
            this[Edges.weight] = edge.weight
            this[Edges.origId] = edge.id
        }

        gId
    }

    fun delete(gId: Int) = transaction {
        Graphs.deleteWhere { id eq gId }
    }

    fun read(gId: Int): Graph = transaction {
        val directed = Graphs
            .selectAll()
            .where { Graphs.id eq gId }
            .single()
            .let { it[Graphs.isDirected] }

        val g = Graph(isDirected = directed)

        Vertices
            .selectAll()
            .where { Vertices.graph eq gId }
            .forEach { row ->
                g.addVertex(row[Vertices.origId], row[Vertices.label])
            }

        Edges
            .selectAll()
            .where { Edges.graph eq gId }
            .forEach { row ->
                g.addEdge(row[Edges.fromId], row[Edges.toId], row[Edges.weight])
            }

        g
    }

    fun update(gId: Int, g: Graph, newName: String? = null) = transaction {
        Graphs.update({ Graphs.id eq gId }) {
            it[isDirected] = g.isDirected
            if (newName != null) it[name] = newName
        }

        Vertices.deleteWhere { graph eq gId }
        Edges.deleteWhere { graph eq gId }

        Vertices.batchInsert(g.vertices) { vertex ->
            this[Vertices.graph] = gId
            this[Vertices.label] = vertex.label
            this[Vertices.origId] = vertex.id
        }

        Edges.batchInsert(g.edges) { edge ->
            this[Edges.graph] = gId
            this[Edges.fromId] = edge.vertices.first.id
            this[Edges.toId] = edge.vertices.second.id
            this[Edges.weight] = edge.weight
            this[Edges.origId] = edge.id
        }
    }

    fun listGraphs(filter: String = ""): List<Pair<Int, String>> = transaction {
        val q = if (filter.isBlank())
            Graphs.selectAll()
        else
            Graphs.selectAll()
                .where { Graphs.name like "%$filter%" }

        q.orderBy(Graphs.name to SortOrder.ASC)
            .map { it[Graphs.id].value to it[Graphs.name] }
    }
}