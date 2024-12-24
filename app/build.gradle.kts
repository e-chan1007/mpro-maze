import java.nio.charset.Charset

plugins {
    application
}

application {
    mainClass = "maze.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "maze.Main"
    }
}
