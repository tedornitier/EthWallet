import org.jetbrains.compose.desktop.application.dsl.TargetFormat

group = "com.github.alessandrotedd.ethwallet"
version = "1.1.0"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("java")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.web3j:core:5.0.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.junit.jupiter:junit-jupiter:5.7.1")
                compileOnly("junit:junit:4.13")
                runtimeOnly("org.junit.vintage:junit-vintage-engine")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}