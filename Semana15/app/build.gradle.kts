plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    // namespace = paquete del código fuente (com.yldefonso), donde vive R y MainActivity
    namespace = "com.yldefonso"
    compileSdk = 35

    defaultConfig {
        // applicationId = identificador único de la app en el celular
        applicationId = "com.yldefonso.avisoscampus"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // BOM: fija versiones coherentes de todas las librerías de Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)

    // ViewModel para Compose (dependencia del proyecto base)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
}
