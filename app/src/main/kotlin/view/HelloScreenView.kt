package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import model.graph.Graph
import model.io.neo4j.Neo4jRepository
import view.colors.ColorTheme
import view.io.neo4jView

enum class Storage {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun helloScreen() {
    var storage by remember { mutableStateOf<Storage?>(null) }
    val navigator = LocalNavigator.current
    val uri = remember { mutableStateOf<String?>(null) }
    val username = remember { mutableStateOf<String?>(null) }
    val password = remember { mutableStateOf<String?>(null) }
    val graph = remember { mutableStateOf<Graph>(Graph()) }
    val exceptionDialog = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Select the method for loading the graph",
            color = ColorTheme.TextColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 52.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { storage = Storage.JSON },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectRepositoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.23f),
                enabled = false,
            ) {
                Text("JSON\nWIP")
            }

            OutlinedButton(
                onClick = { storage = Storage.SQLite },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectRepositoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.34f),
                enabled = false,
            ) {
                Text(
                    "SQLite\nWIP"
                )
            }

            OutlinedButton(
                onClick = { storage = Storage.Neo4j },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectRepositoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.28f)

            ) {
                Text("Neo4j")
            }
        }
    }

    when (storage) {
        Storage.JSON -> {
            storage = null
        }

        Storage.SQLite -> {
            storage = null
        }

        Storage.Neo4j -> {
            neo4jView(
                uri,
                username,
                password,
                onDismiss = { storage = null },
                onConnect = {
                    val neo4jRepo = Neo4jRepository(uri.value ?: "", username.value ?: "", password.value ?: "")
                    try {
                        graph.value = neo4jRepo.readFromDB()
                    } catch (e: Exception) {
                        exceptionDialog.value = true
                        uri.value = null
                        username.value = null
                        password.value = null
                        message.value = e.message ?: "Unknown error"
                    }
                }
            )
        }

        else -> {

        }
    }

    if (exceptionDialog.value) {
        exceptionView(message.value) { exceptionDialog.value = false }
    }
}
