import org.gradle.api.tasks.testing.logging.TestLogEvent.*
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
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    id("io.kotest") version "0.3.8"
}

apply(plugin = "org.jetbrains.kotlin.plugin.spring")

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("org.springframework.data.relational.core.mapping.Table")
    annotation("javax.xml.bind.annotation.XmlRootElement")
}

group = "pl.emil"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2020.0.1"

fun DependencyHandlerScope.kotest(
    target: String,
    module: String = "",
    version: String = "4.6.3",
    extension: Boolean = false
): Dependency? =
    this.testImplementation(
        "io.kotest${
            when {
                extension -> ".extensions"
                module.isBlank() -> ""
                else -> ".$module"
            }
        }:kotest-${if (extension) "extensions-$target" else target}:$version"
    )

fun DependencyHandlerScope.jackson(target: String, module: String = "core", version: String = "2.12.2") =
    this.implementation("com.fasterxml.jackson.${module}:jackson-$target:${version}")

fun DependencyHandlerScope.jsonwebtoken(target: String, version: String = "0.11.1", runtime: Boolean = true): Dependency? =
    if(runtime) this.runtimeOnly("io.jsonwebtoken:jjwt-${target}:$version")
    else this.implementation("io.jsonwebtoken:jjwt-${target}:$version")

fun DependencyHandlerScope.javax(module: String, target: String, version: String = "4.0.1") =
    this.implementation("javax.$module:$target:$version")

fun DependencyHandlerScope.spring(module: String, target: String) =
    this.implementation("org.springframework.$module:spring-$module-$target")

fun DependencyHandlerScope.jetbrains(module: String, target: String) =
    this.implementation("org.jetbrains.${module}:${module}-${target}")

fun DependencyHandlerScope.r2dbc(target: String, version: String, runtime: Boolean = false) =
   if (runtime) this.runtimeOnly("io.r2dbc:r2dbc-${target}:${version}")
   else this.implementation("io.r2dbc:r2dbc-${target}:${version}")

dependencies {
    spring("data", "commons")
    spring("boot", "devtools")
    spring("boot", "starter-actuator")
    spring("boot", "starter-webflux")
    spring("boot", "starter-validation")
    spring("boot", "starter-security")
    spring("boot", "starter-data-r2dbc")
    spring("cloud", "starter-config")
    spring("cloud", "starter-netflix-eureka-client")

    jetbrains("kotlin", "reflect")
    jetbrains("kotlin", "stdlib-jdk8")
    jetbrains("kotlinx", "coroutines-reactor")

    javax("validation", "validation-api", "2.0.1.Final")
    javax("servlet", "javax.servlet-api", "4.0.1")
    javax("annotation", "javax.annotation-api", "1.3.2")
    javax("persistence", "javax.persistence-api", "2.2")
    javax("xml.bind", "jaxb-api", "2.3.1")

    implementation("net.logstash.logback:logstash-logback-encoder:6.4")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")

    jackson("core")
    jackson("databind")
    jackson("annotations")
    jackson("dataformat-xml", "dataformat")
    jackson("module-kotlin", "module")
    jackson("datatype-jsr310", "datatype", "2.11.3")

    implementation("com.vladmihalcea:hibernate-types-52:2.1.1")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    runtimeOnly("org.postgresql:postgresql")
    r2dbc("postgresql", "0.8.8.RELEASE", runtime = true)
    r2dbc("spi", "0.9.0.M1")
    r2dbc("postgresql", "0.8.6.RELEASE")

    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("com.github.jmnarloch:modelmapper-spring-boot-starter:1.1.0")
    implementation("org.modelmapper:modelmapper:2.3.9")

    jsonwebtoken("api", runtime = false)
    jsonwebtoken("impl")
    jsonwebtoken("jackson")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime")

    testImplementation("com.winterbe:expekt:0.5.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    kotest("assertions-core-jvm")
    kotest("assertions-core")
    kotest("property")
    kotest("framework-engine-jvm")
    kotest("runner-junit5")
    kotest("runner-junit5")
    kotest("testcontainers", version = "1.0.1", extension = true)
    kotest("spring", version = "1.0.1", extension = true)

    testImplementation("io.mockk:mockk:1.12.0")
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
    testLogging {
        events = setOf(STARTED, PASSED, FAILED)
        showStandardStreams = true
    }
}
