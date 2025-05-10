package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme.edgeDefaultColor
import viewmodel.graph.GraphViweModel

@Composable
fun GraphView(
    viewModel: GraphViweModel,
    scale: Float,
) {

    var offsetX = remember { mutableStateOf(2f) }
    var offsetY = remember { mutableStateOf(2f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(197, 197, 197))
    ) {
        Box(
            modifier = Modifier
            .fillMaxSize()
            .pointerInput(viewModel) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x
                    offsetY.value += dragAmount.y
                }
            }
            .offset(offsetX.value.dp, offsetY.value.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
        ) {
            viewModel.edges.forEach { e ->
                EdgeView(e, Modifier, viewModel.graph.isDirected)
            }
            viewModel.vertices.forEach { v ->
                VertexView(v, Modifier)
            }
        }
    }
}
