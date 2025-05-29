package io

import model.io.neo4j.Neo4jRepository
import model.utils.randomGraph
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders

const val vertexCount = 100
const val edgesCount = 5

class Neo4jTest {

    companion object {
        private lateinit var neo4j: Neo4j
        private lateinit var driver: Driver

        @BeforeAll
        @JvmStatic
        fun setUp() {
            neo4j = Neo4jBuilders.newInProcessBuilder().withDisabledServer().build()

            driver = GraphDatabase.driver(
                neo4j.boltURI(),
                AuthTokens.none()
            )
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            driver.close()
            neo4j.close()
        }

        fun getVertexCount(): Int {
            var result = 0
            val session = driver.session()
            session.executeRead { transaction ->
                val vCount = transaction.run(
                    "MATCH (n) RETURN count(n)"
                ).single()

                result = when {
                    vCount["count(n)"].isNull -> 0
                    else -> vCount["count(n)"].asInt()
                }
            }
            return result
        }

        fun getEdgesCount(): Int {
            var result = 0
            val session = driver.session()
            session.executeRead { transaction ->
                val eCount = transaction.run(
                    "MATCH (v1)-[e]->(v2) RETURN count(e)"
                ).single()

                result = when {
                    eCount["count(e)"].isNull -> 0
                    else -> eCount["count(e)"].asInt()
                }
            }
            return result
        }
    }

    @Test
    @Order(1)
    fun `test neo4j load`() {
        val graph =
            randomGraph(isDirected = false, isWeighted = false, vertexCount = vertexCount, edgeMaxCount = edgesCount)
        val neo4jRepo = Neo4jRepository(neo4j.boltURI().toString(), "neo4j", "password")
        neo4jRepo.writeToDB(graph)
        assertEquals(graph.vertices.size, getVertexCount())
        assertEquals(graph.edges.size, getEdgesCount())
    }

    @Test
    @Order(2)
    fun `test neo4j store`() {
        val neo4jRepo = Neo4jRepository(neo4j.boltURI().toString(), "neo4j", "password")
        val graph2 = neo4jRepo.readFromDB()
        assertEquals(graph2.vertices.size, vertexCount)
        assertEquals(graph2.edges.size, getEdgesCount())
    }
}