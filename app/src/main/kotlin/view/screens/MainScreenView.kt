package view.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.graph.GraphView
import viewmodel.screens.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        var scale by remember { mutableStateOf(1f) }
        val scrollState = rememberScrollState()

        Column(modifier = Modifier.width(370.dp)) {
            Row {
                Switch(
                    checked = viewModel.showVerticesLabels,
                    onCheckedChange = {
                        viewModel.showVerticesLabels = it
                    },
                )
                Text("Show vertices labels", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            Row {
                Switch(checked = viewModel.showEdgesWeights, onCheckedChange = {
                    viewModel.showEdgesWeights = it
                })
                Text("Show edges weights", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            Row {
                Switch(
                    checked = viewModel.showVerticesId, onCheckedChange = { viewModel.showVerticesId = it })
                Text("Show vertex id", fontSize = 28.sp, modifier = Modifier.padding(4.dp))
            }
            Button(
                onClick = viewModel::resetGraphView,
                enabled = true,
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
                            scale = scale.coerceIn(0.000001f, 100f)
                            delta
                        },
                ),
        ) {
            GraphView(viewModel.graphViewModel, scale)
        }
    }
}
