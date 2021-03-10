@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
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
    groovy
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

val LOGBACK_VERSION = "6.4"
val GROOVY_VERSION = "3.0.7"
val OBJENESIS_VERSION = "org.objenesis:objenesis:3.1"
val CLIB_VERSION = "cglib:cglib-nodep:3.3.0"
val GROOVY = "org.codehaus.groovy:groovy:$GROOVY_VERSION"
val GROOVY_VERSION_ALL = "org.codehaus.groovy:groovy-all:$GROOVY_VERSION"
val SPOCK_VERSION = "2.0-M4-groovy-3.0"
val SPOCK_CORE_VERSION = "org.spockframework:spock-core:$SPOCK_VERSION"
val SPOCK_SPRING_VERSION = "org.spockframework:spock-spring:$SPOCK_VERSION"
val BYTEBUDDY_VERSION = "net.bytebuddy:byte-buddy:1.10.14"
val HAMCREST_VERSION = "org.hamcrest:hamcrest-core:2.2"

configurations {
    all {
        resolutionStrategy {
            eachDependency {
                if (requested.group == "org.codehaus.groovy") {
                    useVersion("3.0.7")
                }
            }
        }
    }
}

dependencies {
    implementation("net.logstash.logback:logstash-logback-encoder:${LOGBACK_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.fasterxml.jackson.core:jackson-core:2.12.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.2")
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

    implementation(GROOVY_VERSION_ALL)
    implementation(GROOVY)
    testImplementation(SPOCK_CORE_VERSION)
    testImplementation(SPOCK_SPRING_VERSION)
    testImplementation(platform("org.spockframework:spock-bom:${SPOCK_VERSION}"))
    implementation(GROOVY_VERSION_ALL)
    testImplementation(CLIB_VERSION)
    testImplementation(HAMCREST_VERSION) // only necessary if Hamcrest matchers are used
    testImplementation(BYTEBUDDY_VERSION)
    testImplementation(OBJENESIS_VERSION)

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
