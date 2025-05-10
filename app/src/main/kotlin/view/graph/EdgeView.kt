package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import viewmodel.graph.EdgeViewModel
import kotlin.math.atan
import kotlin.math.atan2


@Composable
fun EdgeView (
    viewModel: EdgeViewModel,
    modifier: Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLine(
            start = Offset(
                viewModel.first.x.toPx() + viewModel.first.radius.toPx(),
                viewModel.first.y.toPx() + viewModel.first.radius.toPx(),
            ),
            end = Offset(
                viewModel.second.x.toPx() + viewModel.second.radius.toPx(),
                viewModel.second.y.toPx() + viewModel.second.radius.toPx(),
            ),
            color = viewModel.color,
        )
    }
    if (viewModel.weightVisible) {
        Text(
            modifier = Modifier
                .offset(
                    viewModel.first.x + (viewModel.second.x - viewModel.first.x) / 2,
                    viewModel.first.y + (viewModel.second.y - viewModel.first.y) / 2
                ),
            text = viewModel.weight,
        )
    }
}