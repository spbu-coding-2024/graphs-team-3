package model.io.neo4j

import model.graph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.ClientException
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
        } catch (e: ServerException) {
            println("WARNING: cannot connect to database")
        } catch (e: Exception) {
            println("WARNING: other error")
        }
    }
}