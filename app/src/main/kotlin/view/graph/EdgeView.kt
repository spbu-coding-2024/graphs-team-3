package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import viewmodel.graph.EdgeViewModel
import kotlin.math.*

@Composable
fun EdgeView(
    viewModel: EdgeViewModel,
    modifier: Modifier,
    isDirected: Boolean,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLine(
            start =
                Offset(
                    viewModel.first.x.toPx() + viewModel.first.radius.toPx(),
                    viewModel.first.y.toPx() + viewModel.first.radius.toPx(),
                ),
            end =
                Offset(
                    viewModel.second.x.toPx() + viewModel.second.radius.toPx(),
                    viewModel.second.y.toPx() + viewModel.second.radius.toPx(),
                ),
            color = viewModel.color,
        )
    }

    if (isDirected) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val radius = viewModel.first.radius.toPx()
            val arrowBase =
                Offset(
                    viewModel.first.x.toPx() + radius + (viewModel.second.x.toPx() - viewModel.first.x.toPx()) / 2,
                    viewModel.first.y.toPx() + radius + (viewModel.second.y.toPx() - viewModel.first.y.toPx()) / 2,
                )

            val angle =
                atan2(
                    viewModel.second.y.toPx() - viewModel.first.y.toPx(),
                    viewModel.second.x.toPx() - viewModel.first.x.toPx(),
                )
            val arrowLen = 25.dp.toPx()
            val arrowAngle = Math.toRadians(25.0).toFloat()

            val arrowFirstPoint =
                Offset(
                    arrowBase.x - arrowLen * cos(angle - arrowAngle),
                    arrowBase.y - arrowLen * sin(angle - arrowAngle),
                )
            val arrowSecondPoint =
                Offset(
                    arrowBase.x - arrowLen * cos(angle + arrowAngle),
                    arrowBase.y - arrowLen * sin(angle + arrowAngle),
                )

            drawLine(color = viewModel.color, start = arrowBase, end = arrowFirstPoint)
            drawLine(color = viewModel.color, start = arrowBase, end = arrowSecondPoint)
        }
    }
    if (viewModel.weightVisible) {
        Text(
            modifier =
                Modifier
                    .offset(
                        viewModel.first.x + (viewModel.second.x - viewModel.first.x) / 2,
                        viewModel.first.y + (viewModel.second.y - viewModel.first.y) / 2,
                    ),
            text = viewModel.weight,
        )
    }
}
