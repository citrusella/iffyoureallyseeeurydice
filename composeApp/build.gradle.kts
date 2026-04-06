import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("io.github.vinceglb:filekit-core:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs:0.12.0")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.12.0")
            implementation("io.github.vinceglb:filekit-coil:0.12.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "io.github.citrusella.iffyoureallyseeeurydice.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Rpm)
            packageName = "IFFYouReallySeeEurydice"
            packageVersion = "2.0.0"
            description = "A simple tool for converting 1.0 version Sims iff files to 2.0"
            copyright = "Created in 2026 by purplewowies, released under GNU Public License 3.0"
            licenseFile.set(project.file("LICENSE.txt"))

            windows {
                iconFile.set(File("icons/eurydice icon multi.ico"))
            }
            linux {
                iconFile.set(File("icons/eurydice icon.png"))
                modules("jdk.security.auth")
            }
            macOS {
                iconFile.set(File("icons/eurydice icon multi.icns"))
            }
        }
    }
}
