import org.gradle.api.internal.artifacts.mvnsettings.LocalMavenRepositoryLocator

// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.9.0"
}
fun properties(key: String) = project.findProperty(key).toString()

group = "org.intellij.sdk"
version = "2.0.0"

repositories {
    maven {
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/google/")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/spring/")
    }
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("commons-dbutils:commons-dbutils:1.7")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")
    implementation("org.mapdb:mapdb:3.0.8") {
        exclude(group = "com.google.guava")
    }

}


// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    //2021.1.1  2020.3
    version.set("IU-2022.2")
    //localPath.set("D:\\Program Files\\Jebrains\\ideaIU-2020.3\\")
    plugins.set(listOf("com.intellij.java", "DatabaseTools"))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set("${project.version}")
        sinceBuild.set("203")
        untilBuild.set("222.*")
    }
}
