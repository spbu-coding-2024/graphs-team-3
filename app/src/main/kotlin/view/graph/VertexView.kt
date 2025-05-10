package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodel.colors.ColorTheme
import viewmodel.graph.VertexViewModel

@Composable
fun VertexView(
    viewModel: VertexViewModel,
    modifier: Modifier,
) {

    var isDrugging = remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isHover by interactionSource.collectIsHoveredAsState()

    if (isHover) {
        Box(
            modifier = modifier
                .size(viewModel.radius * 2 + 2.dp, viewModel.radius * 2 + 2.dp)
                .offset(viewModel.x - 1.dp, viewModel.y - 1.dp)
                .background(
                    color = ColorTheme.vertexBackColor,
                    shape = CircleShape
                )
                .pointerInput(viewModel) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.onDrag(dragAmount)
                    }
                }
        ) {

        }
    }

    Box(
        modifier = modifier
            .size(viewModel.radius * 2, viewModel.radius * 2)
            .offset(viewModel.x, viewModel.y)
            .background(
                color = viewModel.color,
                shape = CircleShape
            )
            .pointerInput(viewModel) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.onDrag(dragAmount)
                }
            }
            .hoverable(interactionSource = interactionSource)
    ) {

    }
    if (viewModel.labelVisible) {
        Text(
            modifier = Modifier
                .offset(
                    x = viewModel.x,
                    y = viewModel.y - viewModel.radius - 14.dp
                )
                .widthIn(max = 200.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            text = viewModel.label,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 14.sp
        )
    }

    if (viewModel.idVisible) {
        Text(
            modifier = Modifier
                .offset(
                    x = viewModel.x,
                    y = viewModel.y + viewModel.radius * 2 + 14.dp
                )
                .widthIn(max = 200.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            text = viewModel.id,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}