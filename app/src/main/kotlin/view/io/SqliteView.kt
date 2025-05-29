package view.io

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import model.graph.Graph
import model.io.sqlite.SqliteRepository
import view.dialogs.exceptionView
import view.dialogs.SqliteDeleteDialog
import viewmodel.colors.ColorTheme
import viewmodel.screens.SqliteViewModel

@Composable
fun sqliteView(
    repo: SqliteRepository = remember { SqliteRepository() },
    onDismiss: () -> Unit,
    onGraphChosen: (Graph, Int) -> Unit,
) {
    val vm = remember { SqliteViewModel(repo) }
    var open by remember { mutableStateOf(true) }

    val deleteDialog = remember { vm.deleteDialog }
    val toDelete = remember { vm.toDelete }
    val exceptionDialog = remember { vm.exceptionDialog }
    val message = remember { vm.message }

    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
                onDismiss()
            },
            title = { Text(text = "Choose the graph") },
            buttons = {
                Column(
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = vm.filter,
                        onValueChange = vm::onFilterChange,
                        label = { Text(text = "Search") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier =
                            Modifier
                                .heightIn(max = 300.dp)
                                .fillMaxWidth(),
                    ) {
                        items(vm.graphs) { (id, name) ->
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = name,
                                    modifier =
                                        Modifier
                                            .clickable {
                                                onGraphChosen(vm.openGraph(id), id)
                                                open = false
                                            },
                                )
                                IconButton(
                                    onClick = { toDelete = id to name },
                                    modifier = Modifier.size(20.dp),
                                ) {
                                    Icon(Icons.Default.Delete, null)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = {
                                open = false
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(ColorTheme.rejectColor),
                        ) { Text(text = "Cancel") }
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(percent = 5))
        )
    }

    if (deleteDialog.value) {
        SqliteDeleteDialog(
            graphName = toDelete.value?.second.orEmpty(),
            onConfirm = vm::confirmDelete,
            onDismiss = vm::cancelDelete
        )
    }

    if (exceptionDialog.value) {
        exceptionView(message.value) { vm.setExceptionDialog(false) }
    }
}
