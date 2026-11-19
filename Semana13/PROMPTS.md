# PROMPTS.md · Fase 2 con IA · CampusGPS

| # | Mejora | Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|---|---|
| #1 | Radio de cercanía | Estoy en la Fase 2 de un laboratorio de Android. El proyecto es CampusGPS (Kotlin, Jetpack Compose, Material 3, paquete com.yldefonso.campusgps, patrón MVVM + Repository). Quiero agregar la mejora "Radio de cercanía" SIN romper la arquitectura... [selector 100m/200m/500m, círculo en mapa, marca en lista, texto de conteo, ViewModel con combine y StateFlow, sin cambiar comportamiento anterior ni añadir dependencias]. | Gemini creó el composable `ui/SelectorRadio.kt` con un selector compacto (`SingleChoiceSegmentedButtonRow`), actualizó `MapaViewModel.kt` añadiendo `radioMetros` y `idsDentroDelRadio` (derivado con `combine`), dibujó el `Circle` en `MapaUbicacion.kt`, marcó los lugares dentro del radio en `ListaLugares.kt`, e integró todo en `PantallaMapa.kt`. | (completar por el estudiante tras revisar el código) | (completar por el estudiante tras revisar el código) |

## Detalle de la mejora #1

### Qué se pidió
Agregar la funcionalidad de "Radio de cercanía" respetando la arquitectura MVVM y las reglas establecidas:
1. Un selector con 3 opciones: 100 m, 200 m y 500 m (por defecto 200 m).
2. Cuando el estado sea `Lista`, dibujar en el mapa un `Circle` centrado en la posición del usuario con el radio elegido (relleno semitransparente y borde del color primario del tema).
3. En la lista, marcar de forma clara los lugares que están dentro del radio (`metros != null && metros <= radio`).
4. Debajo del selector, un texto corto: "X lugares dentro de N m".
5. Lógica en el ViewModel (`radioMetros`, `cambiarRadio`, y `idsDentroDelRadio` derivado con `combine`/`map`/`stateIn`). Los Composables solo dibujan y avisan con lambdas.

### Archivos modificados y creados
- **Creado:**
  - `ui/SelectorRadio.kt`: Composable para el selector compacto y conteo de lugares.
- **Modificados:**
  - `viewmodel/MapaViewModel.kt`: Añadido `_radioMetros`, `radioMetros`, `idsDentroDelRadio` (StateFlow derivado) y `cambiarRadio(Int)`.
  - `ui/PantallaMapa.kt`: Integración del `SelectorRadio`, recolección de estados de radio e ids dentro del radio, y paso a componentes.
  - `ui/MapaUbicacion.kt`: Inclusión del `Circle` de Google Maps Compose centrado en la posición del usuario.
  - `ui/ListaLugares.kt`: Indicador visual "Dentro del radio" para los lugares que cumplen la condición.

### Cómo funciona el flujo
1. **Selector:** El usuario selecciona un radio (100m, 200m o 500m) en `SelectorRadio`.
2. **ViewModel:** `SelectorRadio` invoca `vm.cambiarRadio(metros)`, actualizando `_radioMetros`.
3. **Derivación:** El `combine` entre `_lugares` y `_radioMetros` recalcula automáticamente `idsDentroDelRadio: Set<Int>`.
4. **UI (Mapa y Lista):** `PantallaMapa` observa los cambios y los transmite a `MapaUbicacion` (que redibuja el `Circle` con el nuevo radio) y a `ListaLugares` (que actualiza las etiquetas de los lugares dentro del radio), además de actualizar el texto de conteo.

### Cómo probarlo
1. Ejecutar la aplicación en el emulador o dispositivo.
2. Pulsar el botón "Obtener mi ubicación" para activar la geolocalización.
3. Verificar que aparezca el círculo semitransparente en el mapa alrededor de la posición del usuario y las etiquetas "Dentro del radio" en la lista de lugares correspondientes.
4. Cambiar entre las opciones de 100 m, 200 m y 500 m en el selector superior y comprobar que el mapa, la lista y el contador se actualizan instantáneamente.
