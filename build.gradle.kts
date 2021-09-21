import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    id("io.kotest") version "0.3.8"
}

group = "pl.emil"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2020.0.1"

fun kotest(target: String, version: String = "4.6.3") = "io.kotest:kotest-$target:$version"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    testImplementation(kotest("assertions-core-jvm"))
    testImplementation(kotest("framework-engine-jvm"))
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
