package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Edge

class EdgeViewModel (
    val first: VertexViewModel,
    val second: VertexViewModel,
    color: Color,
    private val edge: Edge,
    private val _weightVisible: State<Boolean>,
    private val isDirected: Boolean
) {
    val weight
        get() = edge.weight.toString()

    val weightVisible
        get() = _weightVisible.value

    private var _color = mutableStateOf(color)
    var color: Color
        get() = _color.value
        set(value) {
            _color.value = value
        }

    val origin: Edge get() = edge

}