plugins {
    java
    jacoco
    checkstyle
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.jmp"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

jacoco {
    toolVersion = "0.8.12"
}

checkstyle {
    toolVersion = "10.21.2"
    configFile = file("checkstyle.xml")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springdoc-openapi-webmvc-ui.version")}")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mapstruct:mapstruct:${property("mapstruct.version")}")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstruct.version")}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.github.database-rider:rider-spring:${property("database-rider.version")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Checkstyle> {
    configProperties = mapOf("charset" to "UTF-8")
    maxWarnings = 5
    isShowViolations = true
}

tasks.named("checkstyleMain") {
    dependsOn("compileJava")
}

tasks.named("checkstyleTest") {
    enabled = false
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include("jacoco/*.exec")
        }
    )
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("unitTest") {
    description = "Runs unit tests"
    group = "verification"
    useJUnitPlatform {
        includeTags("unit")
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    useJUnitPlatform {
        includeTags("integration")
    }
}

tasks.named("build") {
    dependsOn("checkstyleMain")
}