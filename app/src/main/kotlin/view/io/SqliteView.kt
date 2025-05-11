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

    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
                onDismiss()
            },
            title = { Text(text = "Choose the graph") },
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = vm.filter,
                        onValueChange = vm::onFilterChange,
                        label = { Text(text = "Search") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .fillMaxWidth()
                    ) {
                        items(vm.graphs) { (id, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onGraphChosen(vm.openGraph(id), id)
                                        open = false
                                    }
                                    .padding(6.dp)
                            ) {
                                Text(text = "$id | $name")
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { open = false; onDismiss() },
                            colors = ButtonDefaults.buttonColors(ColorTheme.rejectColor)
                        ) { Text(text = "Cancel") }
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(percent = 5))
        )
    }
}
