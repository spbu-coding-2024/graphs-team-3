package view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.exceptionDialog.exceptionView
import view.graph.GraphView
import viewmodel.colors.ColorTheme
import viewmodel.screens.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    val exceptionDialog = remember { viewModel.exceptionDialog }
    val message = remember { viewModel.message }
    Column {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.025f)
                .fillMaxWidth()
                .background(ColorTheme.ButtonColor)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            var scale by remember { mutableStateOf(1f) }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.20f)
                    .padding(start = 15.dp)
            ) {
                Row {
                    Switch(
                        checked = viewModel.showVerticesLabels,
                        onCheckedChange = {
                            viewModel.showVerticesLabels = it
                        },
                    )
                    Text(
                        "Show vertex label", fontSize = 28.sp,
                        modifier = Modifier
                            .clickable(
                                onClick = { viewModel.showVerticesLabels = !viewModel.showVerticesLabels },
                                interactionSource = null,
                                indication = null,
                            )
                            .padding(4.dp)
                    )
                }
                Row {
                    Switch(
                        checked = viewModel.showVerticesId,
                        onCheckedChange = { viewModel.showVerticesId = it },
                    )
                    Text(
                        "Show vertex id",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .clickable(
                                onClick = { viewModel.showVerticesId = !viewModel.showVerticesId },
                                interactionSource = null,
                                indication = null,
                            )
                            .padding(4.dp)
                    )
                }
                Row {
                    Switch(
                        checked = viewModel.showEdgesWeights,
                        onCheckedChange = {
                            viewModel.showEdgesWeights = it
                        },
                    )
                    Text(
                        "Show edges weights", fontSize = 28.sp,
                        modifier = Modifier
                            .clickable(
                                onClick = { viewModel.showEdgesWeights = !viewModel.showEdgesWeights },
                                interactionSource = null,
                                indication = null,
                            )
                            .padding(4.dp)
                    )
                }
                Button(
                    onClick = viewModel::resetGraphView,
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(ColorTheme.ButtonColor)
                ) {
                    Text(
                        text = "Reset default settings",
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state =
                            rememberScrollableState { delta ->
                                scale *= 1f + delta / 500
                                scale = scale.coerceIn(0.01f, 100f)
                                delta
                            },
                    ),
            ) {
                GraphView(viewModel.graphViewModel, scale)
            }
        }
    }

    if (exceptionDialog.value) {
        exceptionView(message.value) { viewModel.setExceptionDialog(false) }
    }
}
