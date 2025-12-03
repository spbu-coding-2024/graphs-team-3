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
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import model.graph.Graph
import model.io.json.JsonRepository

@Composable
fun jsonView(
    onDismiss: () -> Unit,
    onGraphChosen: (Graph) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                    text = "Load Graph from JSON",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Text("Loading JSON file...")
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Text(
                        text = "Select JSON file to load graph",
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


                            try {
                                val file = selectJsonFile()
                                if (file != null) {
                                    val repository = JsonRepository()
                                    val graph = repository.readFromDisk(file.absolutePath)
                                    onGraphChosen(graph)
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error loading JSON: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        },
                        enabled = !isLoading,
                    ) {
                        Text("Load JSON File")
                    }
                }
            }
        }
    }
}

fun selectJsonFile(): File? {
    val fileChooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter("JSON files", "json")
        dialogTitle = "Select JSON Graph File"
    }

    return when (fileChooser.showOpenDialog(null)) {
        JFileChooser.APPROVE_OPTION -> fileChooser.selectedFile
        else -> null
    }
}