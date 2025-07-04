plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}

group = "com.sportsocial"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.register<org.gradle.api.tasks.Exec>("composeUpAll") {
    group = "docker"
    description = "Запустить docker compose с приложением и БД (оба в Docker, профиль docker)"
    commandLine("/usr/local/bin/docker", "compose", "up", "--build")
}

tasks.register<org.gradle.api.tasks.Exec>("composeUpAppOnly") {
    group = "docker"
    description = "Запустить только приложение в Docker, используя локальную БД (профиль local)"
    commandLine(
        "/usr/local/bin/docker", "compose", "run", "--rm", "--no-deps",
        "-e", "SPRING_PROFILES_ACTIVE=local",
        "-e", "SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/sportsocial",
        "-e", "SPRING_DATASOURCE_USERNAME=postgres",
        "-e", "SPRING_DATASOURCE_PASSWORD=postgres",
        "app"
    )
}

tasks.register<org.gradle.api.tasks.Exec>("composeDown") {
    group = "docker"
    description = "Остановить docker compose с приложением и БД"
    commandLine("/usr/local/bin/docker", "compose", "down")
}

tasks.register<org.gradle.api.tasks.Exec>("composeUpAppOnlyUp") {
    group = "docker"
    description = "Запустить только сервис app через 'docker compose up app' (использует локальную БД)"
    commandLine("/usr/local/bin/docker", "compose", "up", "app")
}