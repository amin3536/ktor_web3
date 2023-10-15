import org.jetbrains.kotlin.cli.jvm.main

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_ktor: String by project
val ksp_version: String by project
val koin_ksp_version: String by project


val exposed_version: String by project
val h2_version: String by project
plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id ("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.infrastructure.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}
sourceSets{
    main{
        java.srcDirs("build/generated/ksp/main/kotlin")
        kotlin.srcDirs("build/generated/ksp/main/kotlin")
        resources.srcDirs("build/resources")
        resources.srcDirs("resources")
    }
    test{
        kotlin.srcDirs("test")
        java.srcDirs("test")
        resources.srcDirs("test/resources")

    }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    //DI
    implementation("io.insert-koin:koin-ktor:$koin_ktor")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    //validator
    implementation("io.konform:konform-jvm:0.4.0")
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.18")

    //bcrypt
    implementation("com.ToxicBakery.library.bcrypt:bcrypt:+")



    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation ("io.kotest:kotest-runner-junit5:5.7")
    testImplementation ("io.kotest.extensions:kotest-assertions-ktor:2.0.0")
    testImplementation ("io.kotest.extensions:kotest-extensions-koin:1.3.0")


    implementation(kotlin("stdlib"))

    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

}
ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
