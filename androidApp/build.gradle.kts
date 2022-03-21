import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

val apiKey = gradleLocalProperties(rootDir).getProperty("local.properties")

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.azuwinrazak.flickrassignment.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY",  "\"$apiKey\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("com.github.skydoves:landscapist-fresco:1.5.0")

    val composeVersion = "1.1.0"

    // di -dagger hilt
    implementation("com.google.dagger:dagger:2.41")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0-beta01")

    //compose
    implementation("androidx.compose.animation:animation-core:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui-geometry:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.foundation:foundation-layout:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.runtime:runtime-rxjava2:$composeVersion")
    implementation("androidx.compose.ui:ui-text:$composeVersion")
    implementation("androidx.compose.ui:ui-util:$composeVersion")
    implementation("androidx.compose.ui:ui-viewbinding:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.activity:activity-compose:1.3.1")

    //Compose Constraintlayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")

    // networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // coil for image loading
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("com.github.skydoves:landscapist-glide:1.1.7")
}