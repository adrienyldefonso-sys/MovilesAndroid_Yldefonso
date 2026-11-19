# PROMPTS.md · Fase 2 con IA · CampusGPS

| # | Mejora | Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|---|---|
| #1 | Radio de cercanía | Estoy en la Fase 2 de un laboratorio de Android. El proyecto es CampusGPS (Kotlin, Jetpack Compose, Material 3, paquete com.yldefonso.campusgps, patrón MVVM + Repository). Quiero agregar la mejora "Radio de cercanía" SIN romper la arquitectura... [selector 100m/200m/500m, círculo en mapa, marca en lista, texto de conteo, ViewModel con combine y StateFlow, sin cambiar comportamiento anterior ni añadir dependencias]. | Gemini creó el composable `ui/SelectorRadio.kt` con un selector compacto (`SingleChoiceSegmentedButtonRow`), actualizó `MapaViewModel.kt` añadiendo `radioMetros` y `idsDentroDelRadio` (derivado con `combine`), dibujó el `Circle` en `MapaUbicacion.kt`, marcó los lugares dentro del radio en `ListaLugares.kt`, e integró todo en `PantallaMapa.kt`. | (completar por el estudiante tras revisar el código) | (completar por el estudiante tras revisar el código) |
| #2 | Filtrar por categoría | Seguimos en la Fase 2 de CampusGPS. Ya existe la mejora 1 (Radio de cercanía). Ahora quiero la mejora 2: "Filtrar por categoría", SIN romper la arquitectura ni la mejora 1... [fila de chips FilterChip con scroll horizontal LazyRow, selección múltiple, obtención de categorías desde los datos, cálculo de lugar más cercano y contador dentro del radio sobre lugares visibles, limpieza de selección si el lugar oculto es seleccionado, StateFlow derivado lugaresVisibles con combine, sin nuevas dependencias]. | Gemini creó el composable `ui/SelectorFiltro.kt` / `ui/FiltroCategorias.kt` con `LazyRow` y `FilterChip`, actualizó `MapaViewModel.kt` con `categoriasActivas`, `lugaresDisponibles`, y el StateFlow derivado `lugaresVisibles` (con `combine`), y ajustó `PantallaMapa.kt` pasando `lugaresVisibles` a `MapaUbicacion` y `ListaLugares`. | (completar por el estudiante tras revisar el código) | (completar por el estudiante tras revisar el código) |
| #3 | Punto azul nativo | Seguimos en la Fase 2 de CampusGPS. Ya existen la mejora 1 (Radio de cercanía) y la mejora 2 (Filtrar por categoría). Ahora quiero la mejora 3: "Punto azul nativo", SIN romper las anteriores ni la arquitectura... [activar isMyLocationEnabled y myLocationButtonEnabled solo si hay permiso de ubicación FINE o COARSE, evitar SecurityException si no hay permiso, actualizar dinámicamente al conceder permiso desde el lanzador o al reanudar la app con LifecycleEventObserver en ON_RESUME, mantener elementos existentes sin modificar ViewModel ni repositorios, usar MapProperties y MapUiSettings con remember]. | Gemini actualizó `PantallaMapa.kt` añadiendo verificación de permisos con `ContextCompat.checkSelfPermission` y observación del ciclo de vida con `DisposableEffect` (ON_RESUME), y actualizó `MapaUbicacion.kt` aceptando `permisoUbicacion: Boolean` y configurando `MapProperties(isMyLocationEnabled = ...)` y `MapUiSettings(myLocationButtonEnabled = ...)`. | (completar por el estudiante tras revisar el código) | (completar por el estudiante tras revisar el código) |
| #4 | Cómo llegar a pie (extra, fuera de la guía) | Seguimos con CampusGPS. Ya existen las mejoras 1, 2 y 3. Quiero una mejora EXTRA, fuera de la guía, para que la app sea más profesional: "Cómo llegar a pie"... [tiempo estimado a pie en cada tarjeta con metros != null a 80m/min redondeando hacia arriba mín 1 min, botón "Cómo llegar" en la tarjeta seleccionada cuando hay metros != null que abre Google Maps con Intent ACTION_VIEW y URI de direcciones a pie, manejo de ActivityNotFoundException con Toast, función pura formatearTiempoAPie en util/Formato.kt, función abrirRutaAPie en ui/AbrirRuta.kt con Context, callback onComoLlegar en ListaLugares, sin modificar ViewModel ni añadir dependencias]. | Gemini añadió la función pura `formatearTiempoAPie` en `util/Formato.kt`, creó `ui/AbrirRuta.kt` con `abrirRutaAPie` (manejando Intent y Toast), actualizó `ListaLugares.kt` mostrando el tiempo estimado a pie en las tarjetas y el botón "Cómo llegar" en la tarjeta seleccionada mediante `onComoLlegar`, e integró el llamado en `PantallaMapa.kt`. | (completar por el estudiante tras revisar el código) | (completar por el estudiante tras revisar el código) |

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

## Detalle de la mejora #2

### Qué se pidió
Agregar la funcionalidad de "Filtrar por categoría" con:
1. Una fila de chips (`FilterChip`) con scroll horizontal (`LazyRow`) para seleccionar categorías (Estudio, Comida, Eventos, Servicios) de forma múltiple (si ninguna está activa, se muestran todos).
2. Filtrar tanto los marcadores del mapa como los elementos de la lista para mostrar únicamente los lugares de las categorías activas.
3. Recalcular el lugar más cercano (`masCercano`) y el contador/marcas de radio sobre los lugares visibles/filtrados.
4. Limpiar la selección actual (`seleccionar(null)`) si el lugar seleccionado queda oculto por el filtro activo.
5. Arquitectura MVVM estricta: `lugaresVisibles` derivado en el ViewModel con `combine` (`lugares`, `categoriasActivas`) y `stateIn`, obteniendo las categorías disponibles desde el repositorio.

### Archivos modificados y creados
- **Creado:**
  - `ui/FiltroCategorias.kt`: Composable que renderiza la fila horizontal de chips con scroll (`LazyRow`).
- **Modificados:**
  - `viewmodel/MapaViewModel.kt`: Añadidos `categoriasDisponibles`, `_categoriasActivas`, `categoriasActivas`, `lugaresVisibles` (derivado con `combine`), `alternarCategoria(String)` y lógica en `init` para limpiar selección si el lugar oculto estaba seleccionado. Además, se adaptaron `masCercano` e `idsDentroDelRadio` para depender de `lugaresVisibles`.
  - `ui/PantallaMapa.kt`: Integración de `FiltroCategorias` y suministro de `lugaresVisibles` a `MapaUbicacion` y `ListaLugares`.

### Cómo funciona el flujo
1. **Chips:** El usuario toca uno o más `FilterChip` en `FiltroCategorias`.
2. **ViewModel:** Se invoca `vm.alternarCategoria(categoria)`, actualizando `_categoriasActivas`.
3. **Derivación:** El `combine` entre `_lugares` y `_categoriasActivas` genera la nueva lista filtrada `lugaresVisibles`. A su vez, `masCercano` e `idsDentroDelRadio` se recalculan automáticamente sobre dicha lista.
4. **UI (Mapa y Lista):** `PantallaMapa` alimenta a `MapaUbicacion` y `ListaLugares` con `lugaresVisibles`, mostrando solo los marcadores y elementos correspondientes, actualizando el lugar más cercano y limpiando la selección si el lugar seleccionado deja de estar visible.

### Cómo probarlo
1. Ejecutar la app y pulsar "Obtener mi ubicación".
2. Tocar chips de categoría (ej. "Comida" o "Estudio") para activar filtros individuales o múltiples.
3. Comprobar que en el mapa solo aparecen los marcadores de las categorías seleccionadas y en la lista solo dichos lugares.
4. Verificar que el lugar más cercano y el contador de radio ("X lugares dentro de N m") se adaptan dinámicamente a los lugares visibles filtrados.
5. Seleccionar un lugar y luego filtrar una categoría que lo oculte: comprobar que la selección se limpia automáticamente (`seleccionar(null)`).

## Detalle de la mejora #3

### Qué se pidió
Activar el punto azul nativo de Google Maps y el botón de "mi ubicación" respetando la arquitectura:
1. Habilitar `isMyLocationEnabled` y `myLocationButtonEnabled` en el mapa solo si el permiso de ubicación (FINE o COARSE) fue concedido.
2. Prevenir `SecurityException` asegurando que nunca se habiliten sin permiso previo.
3. Actualizar dinámicamente el estado del mapa cuando el usuario conceda el permiso desde el cuadro de diálogo de Android o al regresar de los Ajustes de la aplicación (`LifecycleEventObserver` en `ON_RESUME`).
4. Mantener intactos los demás elementos (marcador rojo "Estás aquí", círculo de radio, marcadores de lugares y verde del más cercano) y la arquitectura (ViewModel intacto, permisos gestionados en la View).

### Archivos modificados
- **Modificados:**
  - `ui/PantallaMapa.kt`: Gestión del estado de permisos con `remember` y `mutableStateOf`, verificación mediante `ContextCompat.checkSelfPermission`, observación del ciclo de vida (`DisposableEffect` con `ON_RESUME`), actualización en el callback del lanzador de permisos, y paso de `permisoUbicacion` a `MapaUbicacion`.
  - `ui/MapaUbicacion.kt`: Recepción del parámetro `permisoUbicacion: Boolean` y configuración de `MapProperties(isMyLocationEnabled = permisoUbicacion)` y `MapUiSettings(myLocationButtonEnabled = permisoUbicacion)` con `remember(permisoUbicacion)`.

### Cómo funciona el flujo
1. **Verificación de permisos:** `PantallaMapa` comprueba el estado del permiso mediante `ContextCompat.checkSelfPermission` al iniciar y cada vez que la app vuelve a primer plano (`ON_RESUME`).
2. **Lanzador:** Si el usuario pulsa "Obtener mi ubicación" sin permiso, se solicita mediante el lanzador de permisos. Al concederlo, se actualiza el estado `permisoUbicacion.value`.
3. **Propiedades del mapa:** `MapaUbicacion` recibe `permisoUbicacion` y configura de forma reactiva `MapProperties` y `MapUiSettings`. Google Maps habilita automáticamente el punto azul nativo y el botón de ubicación en tiempo real sin reiniciar la app.

### Cómo probarlo
1. Ejecutar la aplicación. En el primer inicio sin permisos, verificar que no hay punto azul ni botón de ubicación (y que no hay cierres inesperados por `SecurityException`).
2. Pulsar "Obtener mi ubicación" y aceptar los permisos en el diálogo del sistema.
3. Comprobar que el punto azul nativo de Google Maps y el botón de ubicación aparecen inmediatamente en el mapa, conviviendo con el marcador rojo "Estás aquí", el círculo de radio y los marcadores de lugares.
4. Ir a los Ajustes del dispositivo, revocar el permiso de ubicación y volver a la app: verificar que el punto azul se desactiva de forma dinámica gracias al observador de ciclo de vida (`ON_RESUME`).

## Detalle de la mejora #4

### Qué se pidió
Agregar la funcionalidad extra "Cómo llegar a pie":
1. Mostrar el tiempo estimado a pie en cada tarjeta de la lista junto a la distancia (ej. `"~2 min a pie"`), calculado a 80 metros por minuto (redondeando hacia arriba, mínimo 1 minuto). Si `metros` es null, no se muestra.
2. Añadir un botón de texto `"Cómo llegar"` en la tarjeta SELECCIONADA cuando ya hay distancia calculada (`metros != null`), que abre la ruta a pie hacia ese lugar en Google Maps o navegador mediante un `Intent ACTION_VIEW` con URI de Google Maps directions (`travelmode=walking`).
3. Envolver `startActivity` en `try/catch (ActivityNotFoundException)` para mostrar un Toast corto `"No se pudo abrir la ruta"` si falla, sin requerir `<queries>` en el Manifest.
4. Respetar la arquitectura estricta: ViewModel intacto, función pura `formatearTiempoAPie` en `util/Formato.kt`, función `abrirRutaAPie` con `Context` en `ui/AbrirRuta.kt`, y lambda `onComoLlegar` en `ListaLugares`.

### Archivos modificados y creados
- **Creado:**
  - `ui/AbrirRuta.kt`: Función utilitaria para construir el Intent de Google Maps Directions y abrirlo de manera segura con manejo de excepciones y Toast.
- **Modificados:**
  - `util/Formato.kt`: Adición de la función pura `formatearTiempoAPie(Float): String`.
  - `ui/ListaLugares.kt`: Inclusión del tiempo estimado a pie junto a la distancia en cada tarjeta y del botón `"Cómo llegar"` en la tarjeta seleccionada mediante el callback `onComoLlegar`.
  - `ui/PantallaMapa.kt`: Conexión de `ListaLugares` con `abrirRutaAPie(contexto, lugar)`.

### Cómo funciona el flujo
1. **Cálculo:** Una vez obtenida la ubicación, cada lugar calcula su distancia en metros. La función pura `formatearTiempoAPie` traduce dichos metros a tiempo estimado a pie (`~N min a pie`).
2. **Visualización:** `ListaLugares` muestra la distancia y el tiempo a pie en cada tarjeta. Si el usuario selecciona un lugar y ya hay distancia, aparece el botón `"Cómo llegar"`.
3. **Acción:** Al pulsar `"Cómo llegar"`, la tarjeta avisa a `PantallaMapa` mediante la lambda `onComoLlegar`. `PantallaMapa` ejecuta `abrirRutaAPie`, la cual genera el Intent con la URI de Google Maps (`travelmode=walking`) y lanza la actividad para mostrar la ruta a pie.

### Cómo probarlo
1. Ejecutar la app y pulsar "Obtener mi ubicación".
2. Observar que en cada tarjeta de la lista, junto a la distancia en metros o kilómetros, aparece el tiempo estimado a pie (ej. `~2 min a pie`).
3. Tocar cualquier lugar para seleccionarlo: verificar que aparece el botón de texto `"Cómo llegar"` en la parte inferior de su tarjeta.
4. Pulsar `"Cómo llegar"` y comprobar que se abre Google Maps (o el navegador) trazando la ruta a pie hacia el destino seleccionado.
