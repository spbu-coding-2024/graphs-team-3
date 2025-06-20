package view.io

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import viewmodel.colors.ColorTheme
import viewmodel.screens.ScreenViewModel

@Composable
fun neo4jView(
    uri: State<String?>,
    username: State<String?>,
    password: State<String?>,
    onDismiss: () -> Unit,
    onConnect: () -> Unit,
    viewModel: ScreenViewModel,
) {
    var openDialog by remember { mutableStateOf(true) }
    var passwordVisibility by remember { mutableStateOf(false) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                viewModel.clearAuthData()
                onDismiss()
            },
            title = { Text(text = "Enter your database connection details.") },
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = uri.value ?: "",
                        onValueChange = {
                            viewModel.setUri(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text(text = "Uri") },
                        modifier = Modifier.padding(bottom = 16.dp),
                        singleLine = true,
                        label = { Text(text = "Uri") },
                    )
                    OutlinedTextField(
                        value = username.value ?: "",
                        onValueChange = {
                            viewModel.setUsername(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text(text = "Username") },
                        modifier = Modifier.padding(bottom = 16.dp),
                        singleLine = true,
                        label = { Text(text = "Username") },
                    )
                    OutlinedTextField(
                        value = password.value ?: "",
                        onValueChange = {
                            viewModel.setPassword(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = ColorTheme.TextFieldColor),
                        placeholder = { Text(text = "Password") },
                        modifier = Modifier.padding(bottom = 16.dp),
                        singleLine = true,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        label = { Text(text = "Password") },
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = passwordVisibility,
                            onCheckedChange = { passwordVisibility = !passwordVisibility },
                        )
                        Text(
                            text = "Show password",
                            modifier =
                                Modifier.clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = { passwordVisibility = !passwordVisibility },
                                ),
                        )
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
                            viewModel.clearAuthData()
                            openDialog = false
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(ColorTheme.RejectColor),
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            if (uri.value == null) viewModel.setUri("")
                            if (username.value == null) viewModel.setUsername("")
                            if (password.value == null) viewModel.setPassword("")
                            openDialog = false
                            onDismiss()
                            onConnect()
                        },
                        colors = ButtonDefaults.buttonColors(ColorTheme.ConfirmColor),
                    ) {
                        Text("Ok")
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(percent = 5)),
        )
    }
}
