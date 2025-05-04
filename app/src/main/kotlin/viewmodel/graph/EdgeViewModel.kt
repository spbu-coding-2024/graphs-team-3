package viewmodel.graph

import androidx.compose.runtime.State
import model.graph.Edge

class EdgeViewModel (
    val first: VertexViewModel,
    val second: VertexViewModel,
    private val edge: Edge,
    private val _weightVisible: State<Boolean>,
) {
    val weight
        get() = edge.weight.toString()

    val weightVisible
        get() = _weightVisible.value
}