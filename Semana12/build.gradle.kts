// Build raíz: solo declara los plugins con versión y "apply false".
// En el módulo :app los plugins van SOLO con el id (sin versión).
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    // misma versión que Kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
    // KSP: formato <kotlin>-<ksp>, debe coincidir con la versión de Kotlin
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    // Firebase: procesa google-services.json
    id("com.google.gms.google-services") version "4.4.2" apply false
}