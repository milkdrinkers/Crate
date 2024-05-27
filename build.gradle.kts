import java.time.Instant

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1" // Shades and relocates dependencies, See https://imperceptiblethoughts.com/shadow/introduction/
    id("io.freefair.lombok") version "8.6"

    eclipse
    idea
}

group = "com.github.milkdrinkers"
version = "1.2.0"
description = ""

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8)) // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.jetbrains:annotations:24.1.0")

    implementation("com.esotericsoftware.yamlbeans:yamlbeans:1.15")
    implementation("org.json:json:20240303")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0-M2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0-M2")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        options.compilerArgs.addAll(arrayListOf("-Xlint:all", "-Xlint:-processing", "-Xdiags:verbose"))
    }

    javadoc {
        isFailOnError = false
        val options = options as StandardJavadocDocletOptions
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.overview = "src/main/javadoc/overview.html"
        options.isDocFilesSubDirs = true
        options.tags("apiNote:a:API Note:", "implNote:a:Implementation Note:", "implSpec:a:Implementation Requirements:")
        options.use()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        fun reloc(originPkg: String, targetPkg: String) = relocate(originPkg, "${project.group}.Crate.shaded.${targetPkg}")
        reloc("com.esotericsoftware", "esotericsoftware")
        reloc("org.json", "json")
    }

    test {
        useJUnitPlatform()
    }
}

// Apply custom version arg
val versionArg = if (hasProperty("customVersion"))
    (properties["customVersion"] as String).uppercase() // Uppercase version string
else
    "${project.version}-SNAPSHOT-${Instant.now().epochSecond}" // Append snapshot to version

// Strip prefixed "v" from version tag
project.version = if (versionArg.first().equals('v', true))
    versionArg.substring(1)
else
    versionArg.uppercase()

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = "crate"
            version = "${project.version}"

            pom {
                name.set("Crate")
                description.set(rootProject.description.orEmpty())
                url.set("https://github.com/milkdrinkers/Crate")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/")
                    }
                }
                developers {
                    developer {
                        id.set("darksaid98")
                        name.set("darksaid98")
                        organization.set("Milkdrinkers")
                        organizationUrl.set("https://github.com/milkdrinkers")
                    }
                    developer {
                        id.set("KotlinFactory")
                        name.set("Leonhard Solbach")
                        organization.set("Simplix Softworks")
                        organizationUrl.set("https://simplixsoft.com/")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/milkdrinkers/Crate.git")
                    developerConnection.set("scm:git:ssh://github.com:milkdrinkers/Crate.git")
                    url.set("https://github.com/milkdrinkers/Crate")
                }
            }

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "milkdrinkers"
            url = uri("https://maven.athyrium.eu/releases")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}