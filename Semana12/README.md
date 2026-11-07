# Laboratorio 11-12 · Compose + MVVM + Retrofit + Room + Firebase

- **Autor:** yldefonso (trabajo individual)
- **Grupo:** G01 · **Tema:** Productos (DummyJSON) · **Variante:** categoría `smartphones`
- **Paquete / applicationId:** `com.tecsup.catalogo`
- **Endpoints:**
  - Lista: `GET https://dummyjson.com/products/category/smartphones` → `{ "products": [...] }`
  - Detalle: `GET https://dummyjson.com/products/{id}`

## Cómo compilar
1. Abrir la carpeta del proyecto en Android Studio y esperar el Gradle Sync.
2. Para Firebase (H12) seguir la sección de abajo. Antes de eso la app compila sin Firebase.
3. Ejecutar en un emulador con Google Play.

## Configurar Firebase (H12)
1. Crear el proyecto en https://console.firebase.google.com/ (Analytics opcional).
2. Registrar la app Android con el paquete exacto `com.tecsup.catalogo`.
3. Descargar `google-services.json` y colocarlo en `app/` (**no se sube a Git**).
4. Authentication → Método de acceso → activar Correo electrónico/contraseña.
5. Authentication → Usuarios → crear `admin@catalogo.com` y `usuario@catalogo.com`.
6. Firestore Database → colección `usuarios`, un documento por usuario con **ID = UID** y campos
   `rol` (`admin` o `usuario`) y `nombre`.
7. Pegar las reglas de Firestore del laboratorio y publicar.
8. En el código: descomentar las 3 marcas `FIREBASE (H12)` (build raíz, build de `app/`) y quitar el
   comentario de bloque en `FirebaseAuthRepository.kt`.

## Usuarios de prueba
| Rol | Correo | Clave |
|---|---|---|
| Admin | admin@catalogo.com | (la que se creó en la consola) |
| Usuario | usuario@catalogo.com | (la que se creó en la consola) |

## Adaptaciones al laboratorio (para la sustentación)
| Cambio | Por qué |
|---|---|
| Paquete `com.tecsup.catalogo` | Es el del árbol de carpetas del laboratorio; el repositorio lleva el nombre `lab-integrador-g01-yldefonso`. |
| `Item` tiene `precio`, `descripcion`, `stock`, `rating` | `obtenerDetalle` devuelve `Item` y el detalle (R04) debe mostrar datos que la lista no trae. Los tres últimos son `null` en la lista. |
| `ItemEntity` llevará `precio` (H8) | La lista de Room muestra el precio. |
| El envoltorio `{ products }` se define dentro de `ItemDto.kt` | DummyJSON envuelve la lista; así no se crea un archivo fuera de la estructura. |
| `VacioView` es privada dentro de `ListaScreen.kt` | El estado Vacío (R03) no tiene archivo propio en la estructura. |
| El `ViewModelProvider.Factory` va dentro de `AppContainer.kt` | El laboratorio exige Factory que reciba el `AppContainer` pero no le asigna archivo. |
| Se agregó Coil (`coil-compose`) | `imagenUrl` necesita una librería para mostrar imágenes desde una URL. |
| Room 2.6.1 en lugar de 2.8.5 | Combinación conocida con Kotlin 2.0.21 + KSP 2.0.21-1.0.28 (el laboratorio permite subir de versión). |
| `minSdk = 26` | Incluye los emuladores actuales con Google Play y simplifica canales de notificación. |
| `ItemRepository` entra en el commit H3 | Depende de `Item`, que se crea en H3. |
| Dependencias de Firebase comentadas hasta H12 | Dependen del proyecto que se cree en la nube. |

## Estado por hito
- [x] H1–H4 (proyecto base, estructura y contratos, modelo/UiState/componentes, lista con datos de prueba)
- [ ] H5–H16

## Reparto de tareas
Trabajo individual: todos los hitos los realiza yldefonso.
