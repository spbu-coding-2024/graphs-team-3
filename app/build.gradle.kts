plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
    id("org.jetbrains.compose") version "1.7.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation("org.neo4j.driver", "neo4j-java-driver", "5.28.4")
    implementation("org.gephi","gephi-toolkit" ,"0.10.1" , classifier = "all")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.1")
    implementation("cafe.adriel.voyager:voyager-screenmodel:1.0.1")
    implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    implementation("com.github.JetBrains-Research:louvain:main-SNAPSHOT")}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }

}