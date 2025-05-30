package view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import view.dialogs.FordBellmanDialog
import view.dialogs.exceptionView
import view.graph.GraphView
import view.io.neo4jView
import view.io.sqliteSaveView
import viewmodel.colors.ColorTheme
import viewmodel.screens.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val navigator = LocalNavigator.current
    val fordBellmanStart = remember { mutableStateOf(false) }
    val exceptionDialog = remember { viewModel.exceptionDialog }
    val message = remember { viewModel.message }
    var scale by remember { mutableStateOf(1f) }
    var expanded by remember { mutableStateOf(false) }
    val storage by viewModel.storage
    val uri = remember { viewModel.uri }
    val username = remember { viewModel.username }
    val password = remember { viewModel.password }

    Column {
        Box(
            modifier =
                Modifier
                    .fillMaxHeight(0.025f)
                    .fillMaxWidth()
                    .background(ColorTheme.PanelBackgroundColor),
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(onClick = { viewModel.selectStorage(Storage.Neo4j) }) {
                    Text("Save to Neo4j")
                }
                DropdownMenuItem(onClick = { viewModel.selectStorage(Storage.SQLite) }) {
                    Text("Save to SQLite")
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(0.20f)
                        .padding(start = 15.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = viewModel.showVerticesLabels,
                        onCheckedChange = { viewModel.showVerticesLabels = it },
                    )
                    Text(
                        "Show vertex label",
                        fontSize = 18.sp,
                        modifier =
                            Modifier
                                .clickable(
                                    onClick = { viewModel.showVerticesLabels = !viewModel.showVerticesLabels },
                                    interactionSource = null,
                                    indication = null,
                                ).padding(4.dp),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = viewModel.showVerticesId,
                        onCheckedChange = { viewModel.showVerticesId = it },
                    )
                    Text(
                        "Show vertex id",
                        fontSize = 18.sp,
                        modifier =
                            Modifier
                                .clickable(
                                    onClick = { viewModel.showVerticesId = !viewModel.showVerticesId },
                                    interactionSource = null,
                                    indication = null,
                                ).padding(4.dp),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = viewModel.showEdgesWeights,
                        onCheckedChange = { viewModel.showEdgesWeights = it },
                    )
                    Text(
                        "Show edges weights",
                        fontSize = 18.sp,
                        modifier =
                            Modifier
                                .clickable(
                                    onClick = { viewModel.showEdgesWeights = !viewModel.showEdgesWeights },
                                    interactionSource = null,
                                    indication = null,
                                ).padding(4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = viewModel::resetColors,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("Reset default colors")
                }
                Button(
                    onClick = viewModel::resetGraphView,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("Reset default settings")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Algorithms",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (!viewModel.graphViewModel.graph.isDirected) {
                    Button(
                        onClick = viewModel::showMst,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                    ) {
                        Text("MST")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = viewModel::showCommunities,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("Communities")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = viewModel::showScc,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("SCC")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { fordBellmanStart.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("Ford Bellman algorithm", textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (!viewModel.graphViewModel.graph.isDirected) {
                    Button(
                        onClick = {
                            viewModel.resetColors()
                            viewModel.graphViewModel.findBridges()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                    ) {
                        Text("Find bridges")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navigator?.popUntilRoot() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.ButtonColor),
                ) {
                    Text("Main menu")
                }
            }
            Surface(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .scrollable(
                            orientation = Orientation.Vertical,
                            state =
                                rememberScrollableState { delta ->
                                    scale = (scale * (1f + delta / 500)).coerceIn(0.01f, 100f)
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

    if (fordBellmanStart.value) {
        FordBellmanDialog(
            viewModel.firstId,
            viewModel.secondId,
            viewModel::onFordBellmanRun,
            { fordBellmanStart.value = false },
            viewModel,
        )
    }

    when (storage) {
        Storage.Neo4j -> {
            neo4jView(
                uri,
                username,
                password,
                onDismiss = { viewModel.selectStorage(null) },
                onConnect = viewModel::onNeo4jConnect,
                viewModel,
            )
        }
        Storage.SQLite -> {
            sqliteSaveView(
                graph = viewModel.graphViewModel.graph,
                onDismiss = { viewModel.selectStorage(null) },
            )
        }
        else -> {
        }
    }
}
