package view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

        Column(modifier = Modifier.fillMaxWidth(0.20f)) {
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
                    checked = viewModel.showVerticesId, onCheckedChange = { viewModel.showVerticesId = it })
                Text(
                    "Show vertex id", fontSize = 28.sp,
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
                Switch(checked = viewModel.showEdgesWeights, onCheckedChange = {
                    viewModel.showEdgesWeights = it
                })
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reset default settings",
                )
            }
            Divider(Modifier.padding(vertical = 6.dp))
            Text(
                text = "Algorithms",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())

            Button(onClick = viewModel::showBridges, modifier = Modifier.fillMaxWidth()) {
                Text("Bridges")
            }
            Button(
                onClick = viewModel::showMst,
                enabled = !viewModel.graphViewModel.edges.none { it.origin.weight >= 0 },
                modifier = Modifier.fillMaxWidth()
            ) { Text("MST") }
            Button(onClick = viewModel::showCommunities, modifier = Modifier.fillMaxWidth()) {
                Text("Communities")
            }
            Button(
                onClick = viewModel::showScc,
                enabled = viewModel.graphViewModel.edges.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("SCC") }

            Spacer(Modifier.height(12.dp))
            Divider()
            Button(onClick = { viewModel.saveToDb() }, modifier = Modifier.fillMaxWidth()) {
                Text("Save")
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
