package model.graph

class Edge(
    var id: Long,
    var vertices: Pair<Vertex, Vertex>,
    var weight: Long = 1
) {
    @Override
    override fun toString(): String {
        return "from vertex ${vertices.first.label} to vertex ${vertices.second.label}"
    }
}
