import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.core.screen.Screen
import model.graph.Graph
import view.screens.MainScreen
import view.screens.helloScreen
import viewmodel.representation.RepresentationStrategy
import viewmodel.screens.MainScreenViewModel
import java.awt.Dimension


object HelloScreen:  Screen {
    @Composable
    override fun Content() = helloScreen()
}

class MainScreen(val graph: Graph, val representationStrategy: RepresentationStrategy): Screen {
    @Composable
    override fun Content() = MainScreen(MainScreenViewModel(graph, representationStrategy))
}

@Composable
fun App() {
    Navigator(HelloScreen)
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