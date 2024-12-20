plugins {
    application
}

application {
    mainClass = "maze.Main"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "maze.Main"
    }
}
