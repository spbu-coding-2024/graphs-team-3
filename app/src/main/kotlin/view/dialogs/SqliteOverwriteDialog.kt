package view.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme

@Composable
fun OverwriteDialog(
    graphName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    var open by remember { mutableStateOf(true) }
    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
                onDismiss()
            },
            title = {
                Row {
                    Icon(rememberVectorPainter(image = Icons.Outlined.Warning), null)
                    Text("Graph with this name exists")
                }
            },
            text = {
                Text("Graph \"$graphName\" will be overwritten. Continue?")
            },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            open = false
                            onDismiss()
                        },
                    ) { Text("Cancel", color = ColorTheme.TextColor) }
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            onConfirm()
                            open = false
                        },
                        colors = ButtonDefaults.buttonColors(ColorTheme.RejectColor),
                    ) { Text("Continue") }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(percent = 5)),
        )
    }
}
