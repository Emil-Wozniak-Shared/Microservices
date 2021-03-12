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

extra["LOGBACK_VERSION"] = "6.4"
extra["GROOVY_VERSION"] = "3.0.7"
extra["OBJENESIS_VERSION"] = "org.objenesis:objenesis:3.1"
extra["CLIB_VERSION"] = "cglib:cglib-nodep:3.3.0"
extra["GROOVY"] = "org.codehaus.groovy:groovy:${properties["GROOVY_VERSION"]}"
extra["GROOVY_VERSION_ALL"] = "org.codehaus.groovy:groovy-all:${properties["GROOVY_VERSION"]}"
extra["SPOCK_VERSION"] = "2.0-M4-groovy-3.0"
extra["SPOCK_CORE_VERSION"] = "org.spockframework:spock-core:${properties["SPOCK_VERSION"]}"
extra["SPOCK_SPRING_VERSION"] = "org.spockframework:spock-spring:${properties["SPOCK_VERSION"]}"
extra["SPOCK_BOM"] = "org.spockframework:spock-bom:${properties["SPOCK_VERSION"]}"
extra["BYTEBUDDY_VERSION"] = "net.bytebuddy:byte-buddy:1.10.14"
extra["HAMCREST_VERSION"] = "org.hamcrest:hamcrest-core:2.2"
extra["JACKSON_VERSION"] = "2.12.2"
extra["springCloudVersion"] = "2020.0.1"

dependencies {
    implementation("net.logstash.logback:logstash-logback-encoder:${properties["LOGBACK_VERSION"]}")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

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

    implementation(  "${properties["GROOVY_VERSION_ALL"]}")
    implementation("${properties["GROOVY"]}")
    testImplementation("${properties["SPOCK_CORE_VERSION"]}")
    testImplementation("${properties["SPOCK_SPRING_VERSION"]}")
    testImplementation(platform("${properties["SPOCK_BOM"]}"))
    implementation("${properties["GROOVY_VERSION_ALL"]}")
    testImplementation("${properties["CLIB_VERSION"]}")
    testImplementation("${properties["HAMCREST_VERSION"]}")
    testImplementation("${properties["BYTEBUDDY_VERSION"]}")
    testImplementation("${properties["OBJENESIS_VERSION"]}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
