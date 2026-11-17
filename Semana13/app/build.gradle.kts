// Importamos Properties aquí: dentro de Gradle, "java" es una extensión del plugin y
// escribir java.util.Properties() falla con "Unresolved reference: util".
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// TODO Paso 1: lee local.properties (si existe) y úsalo en defaultConfig
// Idea: aquí vivirá tu MAPS_API_KEY para que NO quede escrita en el código ni en GitHub.
val localProps = Properties().apply {
    // TODO Paso 1: cargar rootProject.file("local.properties")
}

android {
    // El namespace define el paquete donde se genera la clase R. Debe coincidir con tus carpetas.
    namespace = "com.yldefonso.campusgps"
    compileSdk = 35

    defaultConfig {
        // Identificador único de la app. La clave de Maps (Paso 1) se restringe a este valor.
        applicationId = "com.yldefonso.campusgps"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        // TODO Paso 1: manifestPlaceholders["MAPS_API_KEY"] = ...
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    // Jetpack Compose: UI 100% en Kotlin, sin XML.
    buildFeatures { compose = true }
}

dependencies {
    // --- Compose + Material 3 ---
    // El BOM fija versiones compatibles entre sí para todas las librerías de Compose.
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")

    // --- Dependencias del laboratorio (sección 4 del documento) ---
    // FusedLocationProviderClient: obtiene la ubicación del dispositivo.
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // Convierte un Task de Google en corrutina con .await()
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    // GoogleMap, Marker, Circle... como Composables.
    implementation("com.google.maps.android:maps-compose:8.4.0")
    // viewModel(factory = ...) dentro de un Composable.
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
}
