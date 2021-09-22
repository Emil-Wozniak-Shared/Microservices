buildscript {
    repositories { maven { url = uri("https://plugins.gradle.org/m2/") } }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.5.0-M1")
        classpath(embeddedKotlin("gradle-plugin"))
    }
}

plugins {
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
}

group = "pl.emil"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

fun DependencyHandlerScope.kotest(target: String, module: String = "", version: String = "4.6.3") =
    this.testImplementation("io.kotest${if (module.isBlank()) "" else ".$module"}:kotest-$target:$version")

fun DependencyHandlerScope.jackson(target: String, module: String = "core", version: String = "2.12.2") =
    this.implementation("com.fasterxml.jackson.${module}:jackson-$target:${version}")

fun DependencyHandlerScope.jsonwebtoken(target: String, version: String = "0.11.1") =
    this.runtimeOnly("io.jsonwebtoken:jjwt-${target}:$version")

fun DependencyHandlerScope.javax(module: String, target: String, version: String = "4.0.1") =
    this.implementation("javax.$module:$target:$version")

fun DependencyHandlerScope.spring(module: String, target: String) =
    this.implementation("org.springframework.$module:spring-$module-$target")

fun DependencyHandlerScope.jetbrains(module: String, target: String) =
    this.implementation("org.jetbrains.${module}:${module}-${target}")

fun DependencyHandlerScope.r2dbc(target: String, version: String) =
    this.implementation("io.r2dbc:r2dbc-${target}:${version}")

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
