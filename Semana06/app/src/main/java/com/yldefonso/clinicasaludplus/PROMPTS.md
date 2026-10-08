# Registro de Prompts e Iteraciones - Fase 2 (Mejora con IA)

## Hito 1: Integración de Avatares Médicos con Coil

### 1. Lo que se solicitó a la IA
* **Objetivo:** Actualizar el modelo `Medico` y la pantalla `MedicosScreen` para mostrar avatares circulares cargados desde la red utilizando la librería Coil (`AsyncImage`), manteniendo el estilo visual y el botón de retroceso (`IconButton` con `ArrowBack`).
* **Prompt enviado:**
  > "Actúa como desarrollador senior de Android con Jetpack Compose. Integra Coil (AsyncImage) en MedicosScreen.kt, añade el campo fotoUrl en Medico.kt y asigna URLs de avatares médicos en Repositorio.kt."

### 2. Resultado Esperado
* Mostrar cada tarjeta de médico con su fotografía circular a la izquierda, junto a su nombre, CMP, especialidad y disponibilidad.
* Mantener la navegación fluida y sin interrupciones visuales al listar los médicos por especialidad.

### 3. Correcciones y Ajustes Realizados (Iteración Humana)
Durante las pruebas en el emulador se identificaron y resolvieron dos inconvenientes clave:

1. **Cierre Inesperado de la Aplicación (`Crash: keeps stopping`):**
    * **Causa:** Coil intentaba realizar peticiones HTTP/HTTPS sin contar con el permiso explícito en el sistema Android.
    * **Solución:** Se añadió manualmente `<uses-permission android:name="android.permission.INTERNET" />` en el archivo `AndroidManifest.xml`.

2. **Imágenes Bloqueadas / Iconos Desalineados del Rubro:**
    * **Causa:** Varios servicios de imágenes (como Flaticon o Freepik) bloqueaban las peticiones directas desde el emulador (*hotlinking*), mostrando imágenes de relleno (vegetales/íconos abstractos) o fallando la carga.
    * **Solución:** Se sustituyeron las URLs iniciales en `Repositorio.kt` por enlaces directos, públicos y estables de Unsplash, asegurando fotografías de profesionales de la salud con bata médica y estetoscopio alineadas con la rúbrica del proyecto.