package model.io.json

import kotlinx.serialization.Serializable


@Serializable
data class GraphJson(
    val isDirected:Boolean,
    val vertices :List<VertexData>,
    val  edges: List<EdgeData>,
)
@Serializable
data class VertexData(
    val id: Int,
    val label: String,
)
@Serializable
data class EdgeData(
    val id: Long,
    val from: Int,
    val to: Int,
    val weight: Long,
)


