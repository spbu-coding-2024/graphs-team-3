import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import view.helloScreen
import java.awt.Dimension


@Composable
fun App() {
    helloScreen()
}

fun main() = application {
    Window (
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 1200.dp, height = 900.dp),
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}