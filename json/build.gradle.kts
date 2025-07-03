import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

dependencies {
    api(projects.api)
    implementation(libs.json)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        fun reloc(originPkg: String, targetPkg: String) = relocate(originPkg, "${project.group}.crate.shaded.${targetPkg}")
        reloc("org.json", "json")

        minimize()
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.milkdrinkers",
        artifactId = "crate-json",
        version = version.toString().let { originalVersion ->
            if (!originalVersion.contains("-SNAPSHOT"))
                originalVersion
            else
                originalVersion.substringBeforeLast("-SNAPSHOT") + "-SNAPSHOT" // Force append just -SNAPSHOT if snapshot version
        }
    )

    pom {
        name.set(rootProject.name + "-JSON")
        description.set(rootProject.description.orEmpty())
        url.set("https://github.com/milkdrinkers/Crate")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("GNU General Public License Version 3")
                url.set("https://www.gnu.org/licenses/gpl-3.0.en.html#license-text")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.en.html#license-text")
            }
        }

        developers {
            developer {
                id.set("darksaid98")
                name.set("darksaid98")
                url.set("https://github.com/darksaid98")
                organization.set("Milkdrinkers")
            }
        }

        scm {
            url.set("https://github.com/milkdrinkers/Crate")
            connection.set("scm:git:git://github.com/milkdrinkers/Crate.git")
            developerConnection.set("scm:git:ssh://github.com:milkdrinkers/Crate.git")
        }
    }

    configure(JavaLibrary(
        javadocJar = JavadocJar.None(), // We want to use our own javadoc jar
    ))

    // Publish to Maven Central
    publishToMavenCentral(automaticRelease = true)

    // Sign all publications
    signAllPublications()

    // Skip signing for local tasks
    tasks.withType<Sign>().configureEach { onlyIf { !gradle.taskGraph.allTasks.any { it is PublishToMavenLocal } } }
}
