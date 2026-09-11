plugins {
    application
}

val jettyVersion: String by rootProject.extra
val httpClient5Version: String by rootProject.extra
val slf4jVersion: String by rootProject.extra

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jettyVersion")
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpClient5Version")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}

application {
    mainClass.set("com.example.proxy.ProxyApp")
}
