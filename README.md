[![CodeFactor](https://www.codefactor.io/repository/github/spbu-coding-2024/graphs-team-3/badge)](https://www.codefactor.io/repository/github/spbu-coding-2024/graphs-team-3)

![example workflow](https://github.com/spbu-coding-2024/graphs-team-3/actions/workflows/build.yml/badge.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-blue.svg)
![Gradle](https://img.shields.io/badge/Gradle-8.13-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-21-brightgreen.svg)
![License](https://img.shields.io/badge/License-GPLv3-red.svg)
[![Neo4j](https://img.shields.io/badge/Neo4j-008CC1?style=flat&logo=neo4j&logoColor=white)](https://neo4j.com/)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=flat)](https://www.sqlite.org/)
# Graph Visualizer Application

![Hello Screen](/resources/mainScreen.png)

## Technologies

- Kotlin 2.1.10
- Java 21
- SQLite
- Neo4j
- JUnit 5
- Jetpack Compose 1.7.1
- Gradle 8.13

## Architecture
We've implemented the **Model-View-ViewModel (MVVM)** architectural pattern

Blocks:
- Model (graph model, input/output, utilities)
- ViewModel (states, coloring)
- View (GUI elements)

## Start guide

- Download:
```bash
git clone git@github.com:spbu-coding-2024/graphs-team-3.git
cd graphs-team-3
```
- Build:
```bash
./gradlew build
```
- Run:
```bash
./gradlew run
```

# Algorithms

## Communities
#### Use Louvain algorithm
![](resources/Louvane.gif)

## Find Bridges
![](resources/FindBridges.gif)

## Minimum Spanning Tree
#### Use Kruskal algorithm
![](resources/MST.gif)

## Path and negative loops find
#### Use Ford-Bellman algorithm
![](resources/FordBellman.gif)

## Strongly Connected Components
![](resources/SCC.gif)

# Load graph

![](resources/helloScreen.png)
You can:
- Load graph from SQLite database
- Load graph from Neo4j graph database
- Create random graph with settings

# Contributors

- [Gorlov Stepan](https://github.com/Stepiiiiiiik)
- [Yakovlev Nickolai](https://github.com/Nickovlev)

# License
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.txt)
