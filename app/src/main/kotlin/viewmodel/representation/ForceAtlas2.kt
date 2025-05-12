package viewmodel.representation

import androidx.compose.ui.unit.dp
import org.gephi.graph.api.GraphController
import org.gephi.graph.api.GraphModel
import org.gephi.graph.api.Node
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2
import org.gephi.project.api.ProjectController
import org.openide.util.Lookup
import viewmodel.graph.GraphViewModel
import viewmodel.graph.VertexViewModel
import kotlin.math.abs
import kotlin.random.Random

class ForceAtlas2(): RepresentationStrategy {
    override fun place(width: Double, height: Double, graphViewModel: GraphViewModel) {
        val prjCtl: ProjectController = Lookup.getDefault().lookup(ProjectController::class.java)
        prjCtl.newProject()
        val workSpace = prjCtl.currentWorkspace
        val graphModel = Lookup.getDefault().lookup(GraphController::class.java).getGraphModel(workSpace)
        val graph = graphModel.getGraph()
        val vertices = graphViewModel.vertices
        val edges = graphViewModel.edges
        val mapping = mutableMapOf<String, Node>()

        for (vertex in vertices) {
            val label = vertex.id
            val node: Node = graphModel.factory().newNode(label)
            node.label = label
            node.setX(abs(Random.nextFloat() * 1000))
            node.setY(abs(Random.nextFloat() * 1000))
            node.setSize(vertex.radius.value)
            graph.addNode(node)
            mapping[vertex.id] = node
        }

        for (edge in edges) {
            graph.addEdge(graphModel.factory().newEdge(mapping[edge.first.id], mapping[edge.second.id], false))
        }

        var layout = ForceAtlas2(null)
        layout.setGraphModel(graphModel)
        layout.initAlgo()
        layout.resetPropertiesValues()
        layout.scalingRatio = 15.0
        layout.isLinLogMode = true
        layout.gravity = 1.5

        var i = 0
        while (i < 100) {
            if (layout.canAlgo()) {
                layout.goAlgo()
            } else {
                break
            }
            for (vertex in vertices) {
                val m = mapping[vertex.id]
                vertex.x = (m?.x()?.dp ?: vertex.x) + (width / 2).dp
                vertex.y = (m?.y()?.dp ?: vertex.y) + (height / 2).dp
            }
            i++
        }
        layout.endAlgo()
    }
}