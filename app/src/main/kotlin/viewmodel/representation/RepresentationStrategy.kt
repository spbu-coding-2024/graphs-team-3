package viewmodel.representation

import viewmodel.graph.GraphViewModel
import viewmodel.graph.VertexViewModel


interface RepresentationStrategy {
    fun place(width: Double, height: Double, graphViewModel: GraphViewModel)
}