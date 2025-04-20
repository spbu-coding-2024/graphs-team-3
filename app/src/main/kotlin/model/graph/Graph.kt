package model.graph

class Graph(
    var isDirected: Boolean = false,
    var isWeighted: Boolean = false
) {
    private val _vertices = hashMapOf<Int, Vertex>()
    private val _edges = hashMapOf<Long, Edge>()
    private var vertexIdCount: Int = 0
    private var edgeIdCount: Long = 0

    val vertices: Collection<Vertex>
        get() = _vertices.values

    val edges: Collection<Edge>
        get() = _edges.values

    fun addVertex(id: Int, label: String): Vertex = _vertices.getOrPut(id) { Vertex(label, id) }

    fun addEdge(firstVertexId: Int, secondVertexId: Int, weight: Float = 1f): Edge {
        val firstVertex =
            _vertices[firstVertexId] ?: throw IllegalStateException("No vertex with ID: $firstVertexId in graph")
        val secondVertex =
            _vertices[secondVertexId] ?: throw IllegalStateException("No vertex with ID: $secondVertexId in graph")

        if (!isDirected) {
            _edges.getOrPut(edgeIdCount + 1) { Edge(edgeIdCount + 1, secondVertex to firstVertex, weight) }
            edgeIdCount += 2
            return _edges.getOrPut(edgeIdCount - 2) { Edge(edgeIdCount - 2, firstVertex to secondVertex, weight) }
        }
        edgeIdCount++
        return _edges.getOrPut(edgeIdCount - 1) { Edge(edgeIdCount - 1, firstVertex to secondVertex, weight) }
    }

    fun deleteVertex(vertexID: Int) {
        val vertexRemove = _vertices[vertexID] ?: throw IllegalStateException("No vertex with ID: $vertexID in graph")
        var edgesForDelete: MutableList<Long> = mutableListOf()
        _edges.keys.forEach { edgeID ->
            if (vertexRemove == _edges[edgeID]?.vertices?.first || vertexRemove == _edges[edgeID]?.vertices?.second) {
                edgesForDelete.add(edgeID)
            }
        }
        edgesForDelete.forEach { edgeID ->
            _edges.remove(edgeID)
        }
        _vertices.remove(vertexID)
    }

//    fun deleteEdge(edgeId: Long) {
//        _edges.remove(edgeId)
//    }
}