import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.compose") version "2.2.20"
    id("org.jetbrains.compose") version "1.8.2"
    id("fabric-loom") version "1.11-SNAPSHOT"
}

version = "1.0.0"
group = "com.martmists"

repositories {
    google()
    maven("https://maven.nucleoid.xyz")
}

val testmodImplementation by configurations.creating

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings("net.fabricmc:yarn:1.21.8+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.17.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.6+kotlin.2.2.20")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.133.4+1.21.8")

    modApi(include("eu.pb4:polymer-virtual-entity:0.13.8+1.21.8")!!)
    api(include("androidx.collection:collection:1.5.0")!!)
    api(include(compose.runtime)!!)

//    testmodImplementation(sourceSets.main.get().output)
}

sourceSets {
    val main by getting

    val testmod by registering {
        runtimeClasspath += main.runtimeClasspath
        compileClasspath += main.compileClasspath
        compileClasspath += main.output
    }
}

loom {
    runs {
        val testmodServer by registering {
            server()
            ideConfigGenerated(project.rootProject == project)
            name = "Test Mod Server"
            source(sourceSets.getByName("testmod"))
        }
    }
}

tasks {
    withType(ProcessResources::class).configureEach {
        inputs.property("version", project.version)
        inputs.property("minecraft_version", "1.21.8")
        inputs.property("loader_version", "0.17.2")

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_version" to "1.21.8",
                "loader_version" to "0.17.2",
                "kotlin_loader_version" to "1.13.6+kotlin.2.2.20"
            )
        }
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.set(listOf("-Xcontext-parameters"))
        }
    }
}
