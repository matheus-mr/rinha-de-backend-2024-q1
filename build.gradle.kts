plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.graalvm.buildtools.native") version "0.10.1"
}

group = "com.matheus-mr"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// r2dbc
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("io.r2dbc:r2dbc-pool")
	implementation("org.postgresql:r2dbc-postgresql")

	// jackson
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

	// tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
