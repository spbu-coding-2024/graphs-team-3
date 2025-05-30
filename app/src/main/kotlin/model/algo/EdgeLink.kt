package model.algo

import org.jetbrains.research.ictl.louvain.Link

class EdgeLink(
    private val from: Int,
    private val to: Int,
    private val weight: Double,
) : Link {
    override fun source() = from

    override fun target() = to

    override fun weight() = weight
}
