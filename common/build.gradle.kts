import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.graalvm.buildtools.native") version "0.9.20"
}

java.sourceCompatibility = JavaVersion.VERSION_17

apply {
    plugin("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // querydsl
    // https://mvnrepository.com/artifact/com.querydsl/querydsl-jpa
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:31.1-jre")

}

// querydsl 설정
// entity는 common 모듈에만 있으므로, 기타 모듈(api, batch에는 따로 설정하지 않음)
var queryDslSrcDir = "src/main/generated"

java.sourceSets["main"].java {
    srcDir(queryDslSrcDir)
}

tasks.compileJava {
    options.generatedSourceOutputDirectory.set(file(queryDslSrcDir))
    tasks.clean
}

tasks.clean {
    file(queryDslSrcDir).deleteRecursively()
}
// querydsl 설정 끝.

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

tasks.bootRun {
    enabled = false
}