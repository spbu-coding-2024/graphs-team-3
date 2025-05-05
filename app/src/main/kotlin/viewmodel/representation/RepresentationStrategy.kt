package viewmodel.representation

import viewmodel.graph.GraphViweModel
import viewmodel.graph.VertexViewModel


interface RepresentationStrategy {
    fun place(width: Double, height: Double, graphViewModel: GraphViweModel)
}