package model.algo

import model.graph.Graph
import model.graph.Vertex
import org.jetbrains.research.ictl.louvain.getPartition

fun findCommunities(graph: Graph, depth: Int = 2): Map<Int, List<Vertex>> {
    val links = graph.edges.map {
        EdgeLink(
            from = it.vertices.first.id,
            to = it.vertices.second.id,
            weight = it.weight.toDouble()
        )
    }

    val partition = getPartition(links, depth).toMutableMap()

    val idToVertex = graph.vertices.associateBy { it.id }

    var nextCommunityId = (partition.values.maxOrNull() ?: -1) + 1

    for (vertexId in idToVertex.keys) {
        if (!partition.containsKey(vertexId)) {
            partition[vertexId] = nextCommunityId++
        }
    }

    return partition.entries
        .groupBy(
            keySelector = { it.value },
            valueTransform= { idToVertex.getValue(it.key) }
        )
}
