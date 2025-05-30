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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme
import viewmodel.graph.GraphViewModel

@Composable
fun GraphView(
    viewModel: GraphViewModel,
    scale: Float,
) {
    var offsetX = remember { mutableStateOf(2f) }
    var offsetY = remember { mutableStateOf(2f) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(ColorTheme.BackgroundColor),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pointerInput(scale) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX.value += dragAmount.x / scale
                            offsetY.value += dragAmount.y / scale
                        }
                    }.graphicsLayer(scaleX = scale, scaleY = scale)
                    .offset(offsetX.value.dp, offsetY.value.dp),
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
