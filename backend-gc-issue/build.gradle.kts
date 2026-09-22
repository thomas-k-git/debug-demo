plugins {
    application
}

val jettyVersion: String by rootProject.extra
val slf4jVersion: String by rootProject.extra

java {
    toolchain {
        // JVM 21 still has region pinning issues. 25 has them fixed (pinned regions don't block G1 GC)
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
    mainClass.set("com.dynatrace.debugdemo.backendGcIssues.BackendApp")
}

tasks.named<JavaExec>("run") {
    doFirst { mkdir("logs") }
    jvmArgs(
        "-Xlog:gc*,gc+humongous=debug,gc+jni=debug:file=logs/gc-backend.log:time,uptime,level,tags:filecount=5,filesize=10m",
        "-Xmx6700M",
        "-XX:G1HeapRegionSize=210M",
        "-XX:G1ReservePercent=15"
    )
}
