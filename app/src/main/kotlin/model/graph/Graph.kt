package model.graph

class Graph(
    internal var isDirected: Boolean = false,
) {
    internal val _vertices = hashMapOf<Int, Vertex>()
    internal val _edges = hashMapOf<Long, Edge>()
    internal var vertexIdCount: Int = 0
    internal var edgeIdCount: Long = 0
    internal var isNegativeWeight: Boolean = false

    val vertices: Collection<Vertex>
        get() = _vertices.values

    val edges: Collection<Edge>
        get() = _edges.values

    fun addVertex(label: String): Vertex {
        var newVertex = Vertex(label, vertexIdCount)
        _vertices.getOrPut(vertexIdCount) { newVertex }
        vertexIdCount++
        return newVertex
    }

    fun addVertex(id: Int, label: String): Vertex = _vertices.getOrPut(id) { Vertex(label, id) }

    fun addEdge(firstVertexId: Int, secondVertexId: Int, weight: Long = 1): Edge {
        var edgeForChange: Edge? = null
        _edges.values.forEach { edge ->
            when {
                edge.vertices.first.id == firstVertexId && edge.vertices.second.id == secondVertexId -> {
                    edgeForChange = edge
                }

                edge.vertices.first.id == secondVertexId && edge.vertices.second.id == firstVertexId -> {
                    if (isDirected) {
                        throw IllegalStateException("The application does not support graphs with multiple edges.")
                    }
                    edgeForChange = edge
                }
            }
        }

        edgeForChange?.let {
            edgeForChange?.weight = weight
            return edgeForChange ?: throw IllegalStateException()
        }
        if (weight < 0) {
            isNegativeWeight = true
        }

        val firstVertex =
            _vertices[firstVertexId] ?: throw IllegalStateException("No vertex with ID: $firstVertexId in graph")
        val secondVertex =
            _vertices[secondVertexId] ?: throw IllegalStateException("No vertex with ID: $secondVertexId in graph")
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

        isNegativeWeight = false
        _edges.values.forEach { edge ->
            if (edge.weight < 0) {
                isNegativeWeight = true
            }
        }
    }

    fun toAdjacencyList(): MutableMap<Vertex, MutableList<Vertex>> {
        var adjacencyList: MutableMap<Vertex, MutableList<Vertex>> = mutableMapOf()
        this.vertices.forEach { vertex ->
            adjacencyList[vertex] = mutableListOf()
        }
        this.edges.forEach { edge ->
            adjacencyList[edge.vertices.first]?.add(edge.vertices.second)
            if (!isDirected) {
                adjacencyList[edge.vertices.second]?.add(edge.vertices.first)
            }
        }
        return adjacencyList
    }

    fun getEdge(first: Vertex, second: Vertex): Edge? {
        var result: Edge? = null
        this.edges.forEach { edge ->
            if ((edge.vertices.first == first && edge.vertices.second == second) || (edge.vertices.first == second && edge.vertices.second == first)) {
                result = edge
            }
        }
        return result
    }

    fun getVertex(vertexId: Int): Vertex? {
        vertices.forEach { vertex ->
            if (vertex.id == vertexId) return vertex
        }
        return null
    }
}
