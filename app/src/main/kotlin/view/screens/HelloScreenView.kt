package view.screens

import MainScreenNav
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
import view.exceptionDialog.exceptionView
import viewmodel.colors.ColorTheme
import view.io.neo4jView
import viewmodel.representation.ForceAtlas2
import viewmodel.screens.HelloScreenViewModel

enum class Storage {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun helloScreen(
    viewModel: HelloScreenViewModel = remember { HelloScreenViewModel() },
) {
    val storage by viewModel.storage
    val navigator = LocalNavigator.current
    val uri = remember { viewModel.uri }
    val username  = remember { viewModel.username}
    val password = remember { viewModel.password }
    val graph = remember { viewModel.graph }
    val exceptionDialog = remember { viewModel.exceptionDialog }
    val message = remember { viewModel.message }
    val isMainScreen = remember { viewModel.isMainScreen }

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
                onClick = { viewModel.selectStorage(Storage.JSON) },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.translucentButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.23f),
                enabled = false,
            ) {
                Text("JSON\nWIP")
            }

            OutlinedButton(
                onClick = { viewModel.selectStorage(Storage.SQLite) },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.translucentButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.34f),
                enabled = false,
            ) {
                Text(
                    "SQLite\nWIP"
                )
            }

            OutlinedButton(
                onClick = { viewModel.selectStorage(Storage.Neo4j) },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.translucentButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.28f)
            ) {
                Text("Neo4j")
            }
        }
    }

    when (storage) {
        Storage.JSON -> {
            viewModel.selectStorage(null)
        }

        Storage.SQLite -> {
            viewModel.selectStorage(null)
        }

        Storage.Neo4j -> {
            neo4jView(
                uri,
                username,
                password,
                onDismiss = { viewModel.selectStorage(null) },
                onConnect = viewModel::onNeo4jConnect,
                viewModel
            )
        }

        else -> {

        }
    }

    if (exceptionDialog.value) {
        exceptionView(message.value) { viewModel.setExceptionDialog(false) }
    }

    if (isMainScreen.value) {
        navigator?.push(MainScreenNav(graph.value, ForceAtlas2()))
    }
}
