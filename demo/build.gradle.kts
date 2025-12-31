plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.javamodularity.moduleplugin") version "1.8.12"
}

application {
    mainClass.set("demos.MainDemo")
//    mainModule.set("demo.main")
    applicationDefaultJvmArgs = listOf(
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-exports", "javafx.base/com.sun.javafx.binding=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/javafx.scene=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/javafx.scene.text=ALL-UNNAMED",
        "--add-exports", "javafx.base/com.sun.javafx.event=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED",
        "--add-exports", "javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED"
    )
}

repositories {
    mavenCentral()
}

dependencies {
    // NOTE: the latest version _is_ 8.0.1!
    // 8.0.7 was published by mistake
    implementation("io.datafx:datafx:8.0.1")
    implementation("io.datafx:flow:8.0.1")
    // FontAwesome
    // Versions higher than 2.x are for Java 11
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-fontawesome5-pack:12.4.0")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation(project(":jfoenix"))
}

javafx {
    version = "21.0.10-ea+1"
    modules = listOf("javafx.controls", "javafx.fxml")
}
