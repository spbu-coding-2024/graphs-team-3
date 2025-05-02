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
import view.colors.ColorTheme

enum class Storage {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun helloScreen() {
    var storage by remember { mutableStateOf<Storage?>(null) }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text (
            text = "Select the method for loading the graph",
            color = ColorTheme.TextColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 52.sp
        )
        Row (
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            OutlinedButton (
                onClick = { storage = Storage.JSON },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectReposytoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.23f)
            )  {
                Text("JSON")
            }

            OutlinedButton (
                onClick = { storage = Storage.SQLite },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectReposytoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.34f)
            )  {
                Text("SQLite")
            }

            OutlinedButton (
                onClick = { storage = Storage.Neo4j },
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.SelectReposytoryButtonColor),
                modifier = Modifier.clip(RoundedCornerShape(percent = 25)).weight(0.28f)

            )  {
                Text("Neo4j")
            }
        }
    }

    when (storage) {
        Storage.JSON -> {

        }

        Storage.SQLite -> {

        }

        Storage.Neo4j -> {

        }
        else -> {

        }
    }
}