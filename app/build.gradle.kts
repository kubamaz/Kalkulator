import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.provider.ListProperty
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}


interface GitParameters : ValueSourceParameters {
    val arguments: ListProperty<String>
}

abstract class GitValueSource : ValueSource<String, GitParameters> {
    override fun obtain(): String? {
        return try {
            val args = parameters.arguments.get()
            // ProcessBuilder jest bezpieczny i stabilny
            val process = ProcessBuilder(args).start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            if (process.exitValue() == 0 && output.isNotEmpty()) output else null
        } catch (e: Exception) {
            null
        }
    }
}

// 3. Funkcje wywołujące (pamiętaj o przekazaniu project)
fun getGitVersionName(project: Project): String {
    return project.providers.of(GitValueSource::class.java) {
        parameters.arguments.set(listOf("git", "describe", "--tags", "--abbrev=0"))
    }.getOrNull()?.removePrefix("v") ?: "1.0-dev"
}

fun getGitVersionCode(project: Project): Int {
    val count = project.providers.of(GitValueSource::class.java) {
        parameters.arguments.set(listOf("git", "rev-list", "--count", "HEAD"))
    }.getOrNull()
    return count?.toInt() ?: 1
}
android {
    namespace = "com.example.myapplication"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 36
        versionCode = getGitVersionCode(project)
        versionName = getGitVersionName(project)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.evalex)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}