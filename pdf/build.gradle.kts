import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    groovy
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
}

group = "pl.emil"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    mavenLocal()
}

fun DependencyHandler.kotest(target: String = "", module: String = "", version: String = "4.6.3") {
    this.testImplementation("io.kotest${if (module.isBlank()) "" else ".$module"}:kotest-$target:$version")
}

dependencies {
    implementation ("pl.emil:contract:1.0.1")
    implementation("org.codehaus.groovy:groovy-all:3.0.8")
    implementation("net.sourceforge.htmlcleaner:htmlcleaner:2.25")
    implementation("org.apache.xmlgraphics:fop:2.6")
    implementation("org.apache.pdfbox:pdfbox:2.0.24")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.xhtmlrenderer:flying-saucer-core:9.0.7")
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.0.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.codeborne:pdf-test:1.6.1")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    kotest("assertions-core")
    kotest("property")
    kotest("runner-junit5")
    kotest("assertions-core-jvm")
    kotest("framework-engine-jvm")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.0.1")
    testImplementation ("org.assertj:assertj-core:3.19.0")
    testImplementation("org.apache.pdfbox:pdfbox:2.0.24")
}

tasks.named<AbstractCompile>("compileGroovy") {
    // Groovy only needs the declared dependencies
    // (and not longer the output of compileJava)
    classpath = sourceSets.main.get().compileClasspath
}
tasks.named<AbstractCompile>("compileKotlin") {
    // Java also depends on the result of Groovy compilation
    // (which automatically makes it depend of compileGroovy)
    classpath += files(sourceSets.main.get().withConvention(GroovySourceSet::class) { groovy }.classesDirectory)
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

sourceSets {
    main {
        groovy {
            setSrcDirs(listOf("src/main/groovy"))
        }
    }
    test {
        groovy {
            setSrcDirs(listOf("src/test/groovy"))
        }
    }
}
