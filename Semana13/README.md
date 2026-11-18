# CampusGPS

**Laboratorio 13 · Localización Geográfica · Programación en Móviles (Tecsup)**
**Autor:** TU NOMBRE COMPLETO · **Docente:** Juan León

## Descripción
CampusGPS es una app Android que obtiene la ubicación del celular, la muestra en un mapa de Google y
le dice al usuario qué lugar del campus tiene más cerca. Muestra cinco puntos ficticios (biblioteca,
cafetería, laboratorio, auditorio y estacionamiento) con su distancia en metros, ordenados del más
cercano al más lejano. Sigue funcionando si el usuario niega el permiso, apaga el GPS o el dispositivo
no tiene Google Play Services.

**Tecnología:** Kotlin · Jetpack Compose · Material 3 (sin XML) · Google Play Services Location ·
Maps Compose · Patrón MVVM + Repository.

> **Nota de versiones:** el laboratorio indica `maps-compose 8.4.0`, pero esa versión exige
> compileSdk 37 y Android Gradle Plugin 9.1. Se usa `maps-compose 6.2.1`, compatible con AGP 8.7.3 y
> compileSdk 35, con la misma API para este laboratorio.

## Estructura de paquetes
```
com.yldefonso.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                 Posicion, Lugar, LugarConDistancia, TipoError, UbicacionUi
├── data/
│   ├── UbicacionRepository.kt     interface
│   ├── UbicacionRepositoryGoogle.kt   implementación con FusedLocationProviderClient
│   ├── LugaresRepository.kt       interface
│   └── LugaresRepositoryLocal.kt  los 5 lugares fijos
├── util/
│   ├── Distancia.kt               distanciaMetros(...)
│   └── Formato.kt                 formatearDistancia(...)
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt
    ├── MapaUbicacion.kt
    ├── ListaLugares.kt
    ├── MensajeError.kt
    └── theme/
```

## Arquitectura MVVM + Repository
```
View (PantallaMapa)
   |  evento: pulsar el botón            ^ collectAsState()
   v                                     |
ViewModel (MapaViewModel) --- StateFlow --->
   |  llama a las interfaces
   v
UbicacionRepository ---> UbicacionRepositoryGoogle (FusedLocationProviderClient)
LugaresRepository   ---> LugaresRepositoryLocal (los 5 lugares fijos)
```
- La **View** solo conoce a su ViewModel y dibuja según el estado.
- El **ViewModel** solo conoce interfaces de Repository y no recibe ningún `Context`.
- El **Repository** es el único que habla con Google Play Services.
- `util/` contiene funciones puras (distancia y formato) y no guarda estado.

## Configuración de la clave de Maps
La clave **no está en el repositorio**. Para ejecutar el proyecto, agrega en `local.properties`
(ignorado por git) la línea `MAPS_API_KEY=tu_clave`. La clave debe tener habilitado *Maps SDK for Android*
y estar restringida al paquete `com.yldefonso.campusgps` y a tu huella SHA-1.

## Para pensar

**1. ¿Por qué FusedLocationProviderClient está dentro del Repository y no en el ViewModel?**
Porque necesita un `Context` y es una librería de Google. Si estuviera en el ViewModel, este dependería
de Android y de Google, no se podría probar sin un dispositivo y se rompería MVVM. En el Repository queda
aislado detrás de una interface: el ViewModel solo pide `obtenerPosicion()` y no sabe cómo se obtiene.
Así se podría cambiar de proveedor sin tocar el ViewModel.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo concede el usuario y autoriza a la app a leer la ubicación del dispositivo; la clave de API
la crea el desarrollador en Google Cloud e identifica a la app ante el servicio de mapas. Son servicios
distintos. Sin permiso no hay ubicación (la app muestra el mensaje de error con Abrir ajustes), pero el
mapa se ve. Sin clave válida el mapa sale gris o en blanco, pero la ubicación sí se obtiene.

**3. ¿Por qué la clave va en local.properties y no en el código? ¿Qué harías si se subiera a GitHub por error?**
Porque el código se sube al repositorio y cualquiera podría copiar la clave y gastar la cuota o la
facturación de mi proyecto. `local.properties` está en `.gitignore` y se queda solo en mi computadora; el
build la inyecta en el Manifest con `manifestPlaceholders`. Si se subiera por error, la borraría y crearía
una clave nueva en Google Cloud, porque la anterior ya no es segura (queda en el historial de git), y
revisaría el `.gitignore`.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
En el ViewModel (`actualizarDistancias`), usando la función pura `distanciaMetros` de `util/`. Un
Composable se vuelve a ejecutar muchas veces (recomposición) y repetiría el cálculo cada vez; además,
mezclar lógica con la UI rompe MVVM y dificulta las pruebas. La View solo recibe la lista ya calculada y
ordenada y le da formato.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?**
Android deja de mostrar el cuadro de permiso (queda negado de forma permanente), así que el botón ya no
puede pedirlo. La app no se cierra: el lanzador devuelve "denegado", el ViewModel pasa al estado
`Error(SIN_PERMISO)` y la pantalla muestra el mensaje con el botón **Abrir ajustes**, que lleva a la
pantalla de la app (`ACTION_APPLICATION_DETAILS_SETTINGS`) para activar el permiso a mano.