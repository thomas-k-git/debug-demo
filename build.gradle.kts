val jettyVersion by extra("12.0.13")
val httpClient5Version by extra("5.3.1")
val slf4jVersion by extra("2.0.13")

subprojects {
    group = "com.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
