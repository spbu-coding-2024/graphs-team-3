package view.io

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import model.graph.Graph
import model.io.json.JsonRepository
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun jsonSaveView(
    graph: Graph,
    onDismiss: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Save Graph to JSON",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Text("Saving JSON file...")
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else if (successMessage != null) {
                    Text(
                        text = successMessage!!,
                        color = Color.Green,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Text(
                        text = "Select location to save JSON file",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            errorMessage = null
                            successMessage = null

                            try {
                                val file = selectSaveJsonFile()
                                if (file != null) {
                                    val repository = JsonRepository()
                                    repository.writeToDisk(graph, file.absolutePath)
                                    isLoading = false
                                    successMessage = "Graph saved successfully to: ${file.name}"
                                } else {
                                    isLoading = false
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = "Error saving JSON: ${e.message}"
                            }
                        },
                        enabled = !isLoading,
                    ) {
                        Text("Save JSON File")
                    }
                }
            }
        }
    }
}

fun selectSaveJsonFile(): File? {
    val fileChooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter("JSON files", "json")
        dialogTitle = "Save Graph as JSON"
        selectedFile = File("graph.json")
    }

    return when (fileChooser.showSaveDialog(null)) {
        JFileChooser.APPROVE_OPTION -> fileChooser.selectedFile
        else -> null
    }
}
