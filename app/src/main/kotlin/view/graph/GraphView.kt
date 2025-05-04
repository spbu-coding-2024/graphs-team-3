package view.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewmodel.graph.GraphViweModel

@Composable
fun GraphView(
    viewModel: GraphViweModel,
) {
    Box(modifier = Modifier
        .fillMaxSize()

    ) {
        viewModel.edges.forEach { e ->
            EdgeView(e, Modifier)
        }
        viewModel.vertices.forEach { v ->
            VertexView(v, Modifier)
        }
    }
}