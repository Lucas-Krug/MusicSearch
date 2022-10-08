plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version Versions.kotlin
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AppConfig.targetSdk

    defaultConfig {
        applicationId = "de.lucas.musicsearch"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

    implementation("androidx.core:core-ktx:${Versions.core}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.google.android.material:material:${Versions.material}")

    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.navigation:navigation-compose:${Versions.composeNav}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")

    implementation("com.jakewharton.timber:timber:${Versions.timber}")

    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}")

    implementation("androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}")
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    implementation("androidx.room:room-ktx:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")

    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serializationConverter}")

    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")

    implementation("io.coil-kt:coil-compose:${Versions.coil}")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")

    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.mockServer}")
    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.junitExt}")
}