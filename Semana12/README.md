# Laboratorio 11-12 · Compose + MVVM + Retrofit + Room + Firebase

App Android en Kotlin con login de dos roles, catálogo de smartphones desde internet, favoritos guardados en el celular y una notificación local.

- **Autor:** yldefonso (trabajo individual)
- **Grupo:** G01
- **Tema:** Productos (DummyJSON) · **Variante:** categoría `smartphones`
- **Paquete / applicationId:** `com.tecsup.catalogo`
- **Endpoints:**
    - Lista: `GET https://dummyjson.com/products/category/smartphones` (responde `{ "products": [...] }`)
    - Detalle: `GET https://dummyjson.com/products/{id}`

## Tecnologías

Jetpack Compose + Material 3 · MVVM · Retrofit + kotlinx-serialization · Room · Firebase Authentication + Firestore · Coil · Notificaciones locales (NotificationCompat).

## Cómo compilar y ejecutar

1. Abrir en Android Studio la carpeta `Semana12` (la que contiene `settings.gradle.kts`) y esperar el Gradle Sync.
2. Crear el proyecto de Firebase (siguiente sección) y copiar su `google-services.json` dentro de `Semana12/app/`. **Ese archivo no está en el repositorio** porque contiene la clave de API.
3. Ejecutar en un emulador con Google Play (Android 13 o superior para ver el permiso de notificaciones).

## Configurar Firebase y crear los usuarios de prueba

1. En https://console.firebase.google.com/ crear el proyecto `lab-integrador-g01-yldefonso` (sin Analytics).
2. Registrar la app Android con el paquete exacto `com.tecsup.catalogo` y descargar `google-services.json` a `app/`.
3. Authentication → Método de acceso → activar **Correo electrónico/contraseña**.
4. Authentication → Usuarios → crear `admin@catalogo.com` y `usuario@catalogo.com` (las contraseñas se entregan al docente por el aula virtual).
5. Firestore Database → crear la base y la colección `usuarios`. Un documento por usuario, con **ID = UID del usuario** y estos campos de tipo string:

| Documento (ID) | `rol` | `nombre` |
|---|---|---|
| UID de admin@catalogo.com | `admin` | `Administrador` |
| UID de usuario@catalogo.com | `usuario` | `Usuario de prueba` |

6. Firestore → Reglas (publicar):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Capturas

| Login | Lista | Detalle |
|---|---|---|
![img.png](img.png)

| Favoritos | Panel Admin | Notificación |
|----------|---|---|
| ![img_1.png](img_1.png)![img_2.png](img_2.png)![img_3.png](img_3.png)         |

## Arquitectura

MVVM con tres modelos distintos, convertidos por mappers: `ItemDto` (API) → `ItemEntity` (Room) → `Item` (UI). Room es la fuente única de verdad: la lista que ve la persona sale siempre de Room y Retrofit solo la actualiza. Los ViewModel dependen de interfaces (`ItemRepository`, `AuthRepository`, `Notificador`) y se crean con un `ViewModelProvider.Factory` que recibe el `AppContainer`.

```
app/src/main/java/com/tecsup/catalogo/
├── MainActivity.kt
├── AppContainer.kt
├── data/
│   ├── remote/ (ApiService, RetrofitClient, dto/ItemDto)
│   ├── local/ (ItemEntity, ItemDao, AppDatabase)
│   ├── auth/ (AuthRepository, FirebaseAuthRepository)
│   ├── repository/ (ItemRepository, ItemRepositoryImpl)
│   └── mapper/Mappers.kt
├── domain/model/ (Item, Sesion)
├── notification/ (Notificador, NotificadorAndroid)
└── ui/
    ├── navigation/AppNav.kt
    ├── UiState.kt
    ├── login/ lista/ detalle/ favoritos/ admin/
    ├── components/ (Cargando, ErrorView)
    └── theme/
```

## Requisitos implementados

| Cód. | Requisito | Dónde está |
|---|---|---|
| R01 | Login con validación y error de credenciales | `LoginScreen`, `LoginViewModel` |
| R02 | Dos roles guardados en Firestore | `FirebaseAuthRepository`, `AppNav` |
| R03 | Lista con Cargando, Error (Reintentar) y Vacío | `ListaViewModel`, `ListaScreen` |
| R04 | Detalle con el segundo endpoint | `DetalleViewModel`, `DetalleScreen` |
| R05 | MVVM con UiState, ViewModel y Repository como interface | todo el proyecto |
| R06 | Room: Entity, Dao y Database | `data/local/` |
| R07 | Favoritos que sobreviven al cerrar la app y al sincronizar | `ItemRepositoryImpl.refrescar`, `FavoritosScreen` |
| R08 | Modo sin conexión con aviso | `ListaViewModel`, `ListaScreen` |
| R09 | Notificación local con canal y permiso | `NotificadorAndroid`, `MainActivity` |
| R10 | Panel Admin: totales, sincronizar y limpiar caché | `AdminViewModel`, `AdminScreen` |
| R11 | Sesión persistente y cierre de sesión | `LoginViewModel`, `AppNav` |
| R12 | Pruebas unitarias del ViewModel de la lista | Fase 2 (pendiente) |
| R13 | Mejora con IA en la rama `mejora-ia` | Fase 2 (pendiente) |

## Adaptaciones al laboratorio

| Cambio | Por qué |
|---|---|
| Paquete `com.tecsup.catalogo` | Es el del árbol de carpetas del laboratorio. |
| `Item` tiene `precio`, `descripcion`, `stock` y `rating` (los tres últimos son `null` en la lista) | `obtenerDetalle` devuelve `Item` y el detalle debe mostrar datos que solo trae el segundo endpoint. |
| `ItemEntity` incluye `precio` | La lista guardada en Room muestra el precio. |
| El envoltorio `{ products }` (`ListaResponseDto`) vive dentro de `ItemDto.kt` | DummyJSON envuelve la lista y así no se agrega un archivo fuera de la estructura. |
| `VacioView` es privada dentro de `ListaScreen.kt` | El estado Vacío no tiene archivo propio en la estructura. |
| `ItemCard` es `internal` | Se reutiliza en la lista y en Favoritos. |
| El `ViewModelFactory` vive en `AppContainer.kt` | El laboratorio exige el Factory pero no le asigna archivo. |
| `LoginViewModel` también cierra la sesión | La estructura no incluye un ViewModel de sesión aparte. |
| `AdminResumen` vive dentro de `AdminViewModel.kt` | Para no agregar un archivo fuera de la estructura. |
| `AppNav` guarda el rol que llega con la `Sesion` | El rol lo decide Firestore. `AppNav` solo lo usa para elegir qué pantallas mostrar. |
| El permiso de notificaciones se pide en `MainActivity` | La estructura no incluye otro archivo para eso. |
| Se agregó Coil (`coil-compose`) | `imagenUrl` necesita una librería para mostrar imágenes desde una URL. |
| Room 2.6.1 en lugar de 2.8.5 | Combinación conocida con Kotlin 2.0.21 y KSP 2.0.21-1.0.28. |
| `minSdk = 26` | Los canales de notificación existen siempre y es compatible con los emuladores actuales. |
| `ItemRepository` entra en el commit H3 | Depende de `Item`, que se crea en H3. |

## Seguridad

Validar el rol en la app solo cambia lo que se muestra. Lo que protege los datos de verdad son las reglas de Firestore: solo se puede leer el documento propio y nadie puede escribir. `google-services.json` no se sube porque contiene la clave de API del proyecto.

## Plan de commits (rama `main`, Fase 1 sin IA)

| Hito | Commit |
|---|---|
| H1 | chore: crear proyecto base con Compose |
| H2 | chore: crear estructura MVVM y contratos |
| H3 | feat: agregar modelo, UiState y componentes |
| H4 | feat: lista con datos de prueba |
| H5 | feat: agregar ApiService y RetrofitClient para consumir productos de smartphones |
| H6 | feat: conectar la lista con la API mediante repositorio y ViewModel |
| H7 | feat: mostrar el detalle del smartphone con el segundo endpoint de la API |
| H8 | feat: crear la base de datos Room para guardar los smartphones en el celular |
| H9 | refactor: guardar los smartphones en Room y mostrar la lista desde la base de datos |
| H10 | feat: agregar la pantalla de favoritos y el acceso desde la lista |
| H11 | feat: mostrar los datos guardados con un aviso cuando no hay conexión |
| H12 | feat: configurar Firebase Authentication y Firestore para el ingreso por roles |
| H13 | feat: agregar el login con Firebase Authentication, la sesión persistente y el cierre de sesión |
| H14 | feat: mostrar el panel de administrador solo a las personas con rol admin |
| H15 | feat: notificar al terminar de sincronizar y abrir la app al tocar el aviso |
| H16 | docs: README |

## Fase 2 (rama `mejora-ia`)

Pendiente: pruebas unitarias del `ListaViewModel` con un repositorio falso (mínimo 3), mejoras de interfaz y errores, `PROMPTS.md`, Pull Request hacia `main` y versión `v1.0` con el APK de depuración.

## Reparto de tareas

Trabajo individual: todos los hitos los realiza yldefonso.