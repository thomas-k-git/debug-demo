plugins {
    application
}

val httpClient5Version: String by rootProject.extra
val slf4jVersion: String by rootProject.extra
val logbackVersion: String by rootProject.extra

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpClient5Version")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
}

application {
    mainClass.set("com.dynatrace.debugdemo.loadgen.LoadGenApp")
}
