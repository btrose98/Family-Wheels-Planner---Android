import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
//    id("jacoco")
}

android {
    namespace = "com.example.familywheelsplanner_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.familywheelsplanner_android"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val secretsFile = project.rootProject.file("secrets.properties")
        val properties = Properties()
        properties.load(secretsFile.inputStream())

        //return empty key in case something goes wrong
        val baseUrl = properties.getProperty("BASE_URL") ?: ""
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = baseUrl
            )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
}

//jacoco {
//    toolVersion = "0.8.9"
//    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
//}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("com.jakewharton.timber:timber:5.0.1")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // For Gson serialization

    //Mockk
    testImplementation ("io.mockk:mockk:1.13.9")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation ("io.mockk:mockk-android:1.13.9")

    //Calendar
    implementation("com.kizitonwose.calendar:compose:2.5.0")
}