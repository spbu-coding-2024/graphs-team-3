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
import model.graph.Vertex
import viewmodel.graph.GraphViewModel
import androidx.compose.ui.window.Dialog

@Composable
fun CycleListDialog(
    cycles: List<List<Vertex>>,
    graphViewModel: GraphViewModel,
    onDismiss: () -> Unit
) {
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
                    "Select a Cycle",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    items(cycles) { cycle ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    graphViewModel.highlightCycle(cycle)
                                    onDismiss()
                                },
                            backgroundColor = MaterialTheme.colors.surface,
                            elevation = 2.dp
                        ) {
                            Text(
                                text = cycle.joinToString(" -> ") { it.label },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}