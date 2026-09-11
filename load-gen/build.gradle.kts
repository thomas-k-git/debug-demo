plugins {
    application
}

val httpClient5Version: String by rootProject.extra
val slf4jVersion: String by rootProject.extra

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpClient5Version")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}

application {
    mainClass.set("com.example.loadgen.LoadGenApp")
}
