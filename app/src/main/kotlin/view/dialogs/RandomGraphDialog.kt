package view.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme
import viewmodel.screens.HelloScreenViewModel

@Composable
fun RandomGraphDialog(
    isDirected: State<Boolean>,
    isWeighted: State<Boolean>,
    vertexCount: State<String?>,
    edgeMaxCount: State<String?>,
    maxWeight: State<String?> = mutableStateOf("1"),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: HelloScreenViewModel,
) {
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                openDialog = false
            },
            title = { Text(text = "Choose graph properties") },
            buttons = {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Switch(
                            checked = isDirected.value,
                            onCheckedChange = { viewModel.setDirected(!isDirected.value) },
                        )
                        Text(
                            text = "Directed",
                            modifier =
                                Modifier.clickable(
                                    onClick = { viewModel.setDirected(!isDirected.value) },
                                ),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Switch(
                            checked = isWeighted.value,
                            onCheckedChange = {
                                viewModel.setWeighted(!isWeighted.value)
                                viewModel.setMaxWeight(null)
                            },
                        )
                        Text(
                            text = "Weighted",
                            modifier =
                                Modifier.clickable(
                                    onClick = { viewModel.setWeighted(!isWeighted.value) },
                                ),
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                ) {
                    OutlinedTextField(
                        value = vertexCount.value ?: "",
                        onValueChange = {
                            viewModel.setVertexCount(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text(text = "Vertex count") },
                        modifier = Modifier.padding(bottom = 8.dp),
                        singleLine = true,
                        label = { Text(text = "Vertex count") },
                    )
                    Row {
                        OutlinedTextField(
                            value = edgeMaxCount.value ?: "",
                            onValueChange = {
                                viewModel.setEdgeMaxCount(it)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                            placeholder = { Text(text = "Edge max count") },
                            modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                            singleLine = true,
                            label = { Text(text = "Edge max count") },
                        )
                        if (isWeighted.value) {
                            OutlinedTextField(
                                value = maxWeight.value ?: "",
                                onValueChange = {
                                    viewModel.setMaxWeight(it)
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                                placeholder = { Text(text = "Max weight") },
                                modifier = Modifier.padding(bottom = 8.dp),
                                singleLine = true,
                                label = { Text(text = "Max weight") },
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            viewModel.clearProperties()
                            onDismiss()
                            openDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(ColorTheme.RejectColor),
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            if (vertexCount.value == null) viewModel.setVertexCount("")
                            if (edgeMaxCount.value == null) viewModel.setEdgeMaxCount("")
                            if (maxWeight.value == null) viewModel.setMaxWeight("1")
                            openDialog = false
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(ColorTheme.ConfirmColor),
                    ) {
                        Text("Ok")
                    }
                }
            },
        )
    }
}
