// Archivo raíz: solo declara las versiones de los plugins; "apply false" = se aplican en :app.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    // Desde Kotlin 2.0 el compilador de Compose es un plugin de Kotlin (no hace falta composeOptions).
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
