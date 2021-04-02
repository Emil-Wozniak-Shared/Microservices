@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories { maven { url = uri("https://plugins.gradle.org/m2/") } }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.5.0-M1")
        classpath(embeddedKotlin("gradle-plugin"))
    }
}

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
    kotlin("plugin.jpa") version "1.4.31"
}

apply(plugin = "org.jetbrains.kotlin.plugin.spring")

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "pl.emil"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["LOGBACK_VERSION"] = "6.4"
extra["JACKSON_VERSION"] = "2.12.2"
extra["springCloudVersion"] = "2020.0.1"

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("net.logstash.logback:logstash-logback-encoder:${properties["LOGBACK_VERSION"]}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.fasterxml.jackson.core:jackson-core:${properties["JACKSON_VERSION"]}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${properties["JACKSON_VERSION"]}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${properties["JACKSON_VERSION"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${properties["JACKSON_VERSION"]}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${properties["JACKSON_VERSION"]}")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.persistence:javax.persistence-api:2.2")

    runtimeOnly("org.postgresql:postgresql")
    implementation("com.vladmihalcea:hibernate-types-52:2.1.1")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("io.projectreactor:reactor-test")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("io.projectreactor:reactor-test")
    implementation("io.r2dbc:r2dbc-spi:0.9.0.M1")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.6.RELEASE")

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
