plugins {
    application
}

val jettyVersion: String by rootProject.extra
val slf4jVersion: String by rootProject.extra

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jettyVersion")
    implementation("com.github.luben:zstd-jni:1.5.6-3")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}

application {
    mainClass.set("com.example.backendGcIssues.BackendApp")
}
