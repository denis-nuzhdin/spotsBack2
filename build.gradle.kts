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

sourceSets {
    val integrationTest by creating {
        kotlin.srcDir("src/test/kotlin")
        resources.srcDir("src/test/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
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

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
    useJUnitPlatform()
}

tasks.register<Exec>("dockerIntegrationTest") {
    group = "verification"
    description = "Запускает интеграционные тесты в Docker через docker-compose-test.yml"
    
    // Основная команда (пустая, так как логика в doFirst/doLast)
    commandLine("echo", "Starting Docker integration tests...")
    
    doFirst {
        // Остановить контейнеры если они запущены
        exec {
            commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "down")
        }
        
        // Start containers in background
        exec {
            commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "up", "--build", "-d")
        }
        
        // Wait for the application to start
        println("Waiting for application to start...")
        Thread.sleep(30000) // Wait 30 seconds for app to start
    }
    
    doLast {
        // Run integration tests from host against the running container
        exec {
            environment("SPRING_PROFILES_ACTIVE", "test")
            environment("SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost:5433/sportsocial_test")
            environment("SPRING_DATASOURCE_USERNAME", "test")
            environment("SPRING_DATASOURCE_PASSWORD", "test")
            environment("SPRING_JPA_HIBERNATE_DDL_AUTO", "none")
            commandLine("./gradlew", "integrationTest")
        }
        
        // Stop containers
        exec {
            commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "down")
        }
    }
}

tasks.register<Exec>("dockerTestDb") {
    group = "docker"
    description = "Запускает только тестовую базу данных в Docker"
    
    doFirst {
        exec {
            commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "down")
        }
    }
    
    commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "up", "db", "-d")
    
    doLast {
        println("Тестовая база данных запущена на порту 5433")
        println("Для остановки выполните: ./gradlew dockerTestDbDown")
    }
}

tasks.register<Exec>("dockerTestDbDown") {
    group = "docker"
    description = "Останавливает тестовую базу данных"
    commandLine("/usr/local/bin/docker", "compose", "-f", "docker-compose-test.yml", "down")
}