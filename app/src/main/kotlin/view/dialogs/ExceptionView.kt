package view.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme

@Composable
fun exceptionView(
    message: String?,
    onDismiss: () -> Unit,
) {
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                openDialog = false
            },
            title = {
                Row {
                    Icon(
                        rememberVectorPainter(image = Icons.Outlined.Warning),
                        contentDescription = null,
                    )
                    Text(text = "Error")
                }
            },
            text = {
                Text(text = "Exception: $message")
            },
            buttons = {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        openDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorTheme.RejectColor),
                    modifier = Modifier.clip(RoundedCornerShape(percent = 25)).padding(15.dp),
                ) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(percent = 5)),
        )
    }
}
