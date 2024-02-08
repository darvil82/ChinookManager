plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.4.1.Final")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.formdev:flatlaf:+")
    implementation("com.formdev:flatlaf-intellij-themes:+")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}