package view.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme
import viewmodel.screens.MainScreenViewModel

@Composable
fun FordBellmanDialog(
    firstId: State<String?>,
    secondId: State<String?>,
    algoRun: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: MainScreenViewModel
) {
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                openDialog = false
            },
            title = { Text("Enter vertices id's") },
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = firstId.value ?: "",
                        onValueChange = {
                            viewModel.setFirstId(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text("First vertex id") },
                        modifier = Modifier.padding(bottom = 16.dp),
                        singleLine = true,
                        label = { Text("First vertex id") },
                    )
                    OutlinedTextField(
                        value = secondId.value ?: "",
                        onValueChange = {
                            viewModel.setSecondId(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text("Second vertex id") },
                        modifier = Modifier.padding(bottom = 16.dp),
                        singleLine = true,
                        label = { Text("Second vertex id") },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = {
                                viewModel.clearId()
                                onDismiss()
                                openDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(ColorTheme.rejectColor)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = {
                                if (firstId.value == null) viewModel.setFirstId("")
                                if (secondId.value == null) viewModel.setSecondId("")
                                onDismiss()
                                openDialog = false
                                algoRun()
                            },
                            colors = ButtonDefaults.buttonColors(ColorTheme.ConfirmColor)
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        )
    }

}