plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.hilt)
}

val withQuotes = { s: String -> String.format("\"%1\$s\"", s) }
val baseUrl = "https://jsonplaceholder.typicode.com/"
val dbName = "ScrollAppDb"
val dsName = "pref"

android {
    namespace = "com.matteomacri.scrollapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.matteomacri.scrollapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        all {
            buildConfigField("String", "BASE_URL", withQuotes(baseUrl))
            buildConfigField("String", "DB_NAME", withQuotes(dbName))
            buildConfigField("String", "DS_NAME", withQuotes(dsName))
        }
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
    }
}

dependencies {

    // Defaults
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.bundles.paging)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.database)
    implementation(libs.bundles.moshi)
    implementation(libs.bundles.retrofit)
    implementation(libs.google.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}