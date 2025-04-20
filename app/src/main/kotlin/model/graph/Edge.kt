package model.graph

class Edge(
    var id: Long,
    var vertices: Pair<Vertex, Vertex>,
    var weight: Float
)