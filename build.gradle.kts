plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup
minecraft {}
repositories {
    maven(url = "https://maven.terraformersmc.com/releases")
    maven(url = "https://maven.shedaniel.me/")
}
dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    val modMenuVersion: String by project
    modImplementation("com.terraformersmc:modmenu:$modMenuVersion")
    val clothApiVersion: String by project
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothApiVersion") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}
tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        withSourcesJar()
    }
}
