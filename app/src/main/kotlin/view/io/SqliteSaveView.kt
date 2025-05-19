package view.io

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import model.graph.Graph
import model.io.sqlite.SqliteRepository
import view.dialogs.exceptionView
import viewmodel.colors.ColorTheme
import viewmodel.screens.SqliteSaveViewModel

@Composable
fun sqliteSaveView(
    graph: Graph,
    repo: SqliteRepository = remember { SqliteRepository() },
    onDismiss: () -> Unit,
) {
    val vm = remember { SqliteSaveViewModel(repo, graph) }
    var open by remember { mutableStateOf(true) }

    val exceptionDialog = remember { vm.exceptionDialog }
    val message = remember { vm.message }
    val overwriteDialog = remember { vm.overwriteDialog }
    val nameState = remember { vm.nameState }

    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
                onDismiss()
            },
            title = { Text(text = "Create name for the graph") },
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = nameState.value,
                        onValueChange = vm::onNameChange,
                        label = { Text(text = "Name") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .fillMaxWidth()
                    ) {
                        items(vm.graphs) { (_, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                                    .clickable { vm.onGraphClicked(name) },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = name)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { vm.chooseFile() },
                            colors = ButtonDefaults.buttonColors(ColorTheme.ButtonColor)
                        ) { Text(text = "Choose file") }
                        Row {
                            Button(
                                onClick = { open = false; onDismiss() },
                                colors = ButtonDefaults.buttonColors(ColorTheme.rejectColor)
                            ) { Text(text = "Cancel") }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (vm.onSaveClick()) {
                                        open = false
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(ColorTheme.ConfirmColor)
                            ) { Text(text = "Save") }
                        }
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(percent = 5))
        )
    }

    if (overwriteDialog.value != null) {
        AlertDialog(
            onDismissRequest = { vm.cancelOverwrite() },
            title = { Text(text = "Graph with this name exists") },
            text = { Text("The existing graph will be overwritten. Continue?") },
            confirmButton = {
                Button(onClick = {
                    vm.confirmOverwrite()
                    overwriteDialog.value = null
                    open = false
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(ColorTheme.rejectColor)
                ) { Text("Continue") }
            },
            dismissButton = {
                TextButton(onClick = {
                    vm.cancelOverwrite()
                }
                ) { Text("Cancel",  color = ColorTheme.TextColor) }
            }
        )
    }

    if (exceptionDialog.value) {
        exceptionView(message.value) { vm.setExceptionDialog(false) }
    }
}