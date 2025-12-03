package view.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import viewmodel.graph.GraphViewModel

@Composable
fun CycleDetectionDialog(
    graphViewModel: GraphViewModel,
    onDismiss: () -> Unit,
    onVertexSelected: (Int) -> Unit
) {
    var selectedVertexId by remember { mutableStateOf<Int?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Find Cycles for Vertex",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )


                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    items(graphViewModel.vertices.toList()) { vertexViewModel ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    selectedVertexId = vertexViewModel.origin.id
                                },
                            backgroundColor = if (selectedVertexId == vertexViewModel.origin.id)
                                MaterialTheme.colors.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colors.surface,
                            elevation = 2.dp
                        ) {
                            Text(
                                text = "Vertex ${vertexViewModel.origin.id}: ${vertexViewModel.origin.label}",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            selectedVertexId?.let { vertexId ->
                                onVertexSelected(vertexId)
                            }
                            onDismiss()
                        },
                        enabled = selectedVertexId != null
                    ) {
                        Text("Find Cycles")
                    }
                }
            }
        }
    }
}