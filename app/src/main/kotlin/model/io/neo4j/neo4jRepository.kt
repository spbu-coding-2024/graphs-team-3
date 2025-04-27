package model.io.neo4j

import model.graph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Value
import org.neo4j.driver.exceptions.ClientException
import java.io.IOException
import java.rmi.ServerException

class Neo4jRepository(private val uri: String, private val user: String, private val password: String) {
    fun writeToDB(graph: Graph) {
        try {
            val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
            val session = driver.session()

            session.executeWrite { transaction ->
                transaction.run("MATCH (n) DETACH DELETE n").consume()
            }

            for (vertex in graph.vertices) {
                session.executeWrite { transaction ->
                    transaction.run(
                        "CREATE (v:Vertex{label: \$label, id: \$id, isDirected: \$isDirected})",
                        mapOf(
                            "label" to vertex.label,
                            "id" to vertex.id,
                            "isDirected" to if (graph.isDirected) 1 else 0
                        )
                    ).consume()
                }
            }

            for (edge in graph.edges) {
                session.executeWrite { transaction ->
                    transaction.run(
                        "MATCH (v1: Vertex{id: \$idFirst}), (v2: Vertex{id: \$idSecond}) CREATE (v1)-[:CONNECT{id: \$edgeId, weight:\$weight}]->(v2)",
                        mapOf(
                            "edgeId" to edge.id,
                            "idFirst" to edge.vertices.first.id,
                            "idSecond" to edge.vertices.second.id,
                            "weight" to edge.weight
                        )
                    ).consume()
                }
            }
            session.close()
            driver.close()
        } catch (e: ClientException) {
            println("WARNING: wrong uri/username/password")
            throw e
        } catch (e: ServerException) {
            println("WARNING: cannot connect to database")
            throw e
        } catch (e: Exception) {
            println("WARNING: other error occurred")
            throw e
        }
    }

    fun readFromDB(): Graph? {
        var graph: Graph = Graph()
        try {
            val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
            val session = driver.session()
            var maxId = 0
            var changeCount = 0
            session.executeWrite { transaction ->
                maxId = transaction.run(
                    "MATCH (v: Vertex) RETURN max(ID(v)) as maxId",
                ).single().get("maxId").asInt()
                graph.vertexIdCount = maxId + 1

                val vertices = transaction.run(
                    "MATCH (v:Vertex) RETURN ID(v) as id, v.label as label, v.isDirected as isDirected",
                ).list()

                vertices.forEach { vertex ->
                    graph.addVertex(vertex.get("id").asInt(), vertex.get("label").asString())
                    graph.isDirected = vertex.get("isDirected").asInt() == 1
                }

                val edges = transaction.run(
                    "MATCH (f:Vertex)-[e]->(s:Vertex) RETURN ID(f) as firstId, ID(s) as secondId, e.weight as weight",
                ).list()

                edges.forEach { edge ->
                    graph.addEdge(
                        edge.get("firstId").asInt(),
                        edge.get("secondId").asInt(),
                        edge.get("weight").asLong()
                    )
                }
            }
            session.close()
            driver.close()
        } catch (e: ClientException) {
            println("WARNING: wrong uri/username/password")
            throw e
        } catch (e: ServerException) {
            println("ERROR: cannot connect to database")
            throw e
        } catch (e: IllegalStateException) {
            println("ERROR: incorrect graph model in database")
            throw e
        } catch (e: Exception) {
            println("ERROR: other error occurred")
            throw e
        }

        return graph
    }
}