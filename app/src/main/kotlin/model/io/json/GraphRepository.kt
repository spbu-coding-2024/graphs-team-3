package model.io.json

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.graph.Graph
import java.io.File

class JsonRepository {
    private val json = Json { prettyPrint = true }

    fun writeToDisk(graph: Graph, filePath: String) {

        val graphJson = GraphJson(
            isDirected = graph.isDirected,
            vertices = graph.vertices.map { vertex ->
                VertexData(id = vertex.id, label = vertex.label)
            },
            edges = graph.edges.map { edge ->
                EdgeData(
                    id = edge.id,
                    from = edge.vertices.first.id,
                    to = edge.vertices.second.id,
                    weight = edge.weight
                )
            }
        )


        val jsonString = json.encodeToString(graphJson)
        File(filePath).writeText(jsonString)
    }

    fun readFromDisk(filePath: String): Graph {

        val jsonString = File(filePath).readText()
        val graphJson = json.decodeFromString<GraphJson>(jsonString)


        val graph = Graph(isDirected = graphJson.isDirected)


        graphJson.vertices.forEach { vertexData ->
            graph.addVertex(vertexData.id, vertexData.label)
        }


        graphJson.edges.forEach { edgeData ->
            graph.addEdge(edgeData.from, edgeData.to, edgeData.weight)
        }

        return graph
    }
}