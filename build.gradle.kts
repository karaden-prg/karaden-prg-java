plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

val VERSION: String by project
val GROUP: String by project
val VENDOR: String by project
val ARTIFACT: String by project

version = VERSION
group = GROUP

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to archiveVersion,
            "Implementation-Vendor" to VENDOR,
            "Bundle-SymbolicName" to project.name,
            "Export-Package" to GROUP + ".*"
        )
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

publishing {
    repositories {
        /*
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/karaden-prg/karaden-prg-java")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        */
        maven {
            name = "OSSRH-STAGING-API"
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = ARTIFACT
            from(components["java"])

            pom {
                name.set("karaden-prg/karaden-prg-java")
                description.set("Java library for the Karaden API.")
                url.set("https://github.com/karaden-prg/karaden-prg-java")
                licenses {
                    license {
                        name.set("MIT LICENSE")
                        url.set("https://raw.githubusercontent.com/karaden-prg/karaden-prg-java/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("cpaas-dev-admin-om")
                        name.set("cpaas-dev-admin-om")
                        email.set("cpaas-dev-admin-om@nttcoms.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/karaden-prg/karaden-prg-java.git")
                    developerConnection.set("scm:git:https://github.com/karaden-prg/karaden-prg-java.git")
                    url.set("https://github.com/karaden-prg/karaden-prg-java")
                }
            }
        }
    }
}

signing {
    val gpgPrivateKey = providers.gradleProperty("gpgPrivateKey")
    val gpgPassphrase = providers.gradleProperty("gpgPassphrase")
    if (gpgPrivateKey.isPresent() && gpgPassphrase.isPresent()) {
        useInMemoryPgpKeys(gpgPrivateKey.get(), gpgPassphrase.get())
    }
    sign(publishing.publications["maven"])
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("org.json:json:20231013")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
