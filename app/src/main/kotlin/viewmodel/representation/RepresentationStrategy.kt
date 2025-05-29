package viewmodel.representation

import viewmodel.graph.GraphViewModel

interface RepresentationStrategy {
    fun place(
        width: Double,
        height: Double,
        graphViewModel: GraphViewModel,
    )
}
