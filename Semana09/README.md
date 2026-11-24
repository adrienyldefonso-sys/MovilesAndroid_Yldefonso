# MuroCampus: Servicio Web REST (Semana 09)

App Android en Kotlin con Jetpack Compose que consume el servicio público JSONPlaceholder mediante Retrofit. Usa arquitectura MVVM con Repository, y maneja los estados Cargando, Éxito y Error en cada pantalla.

## Funcionalidades

- Lista de publicaciones (GET posts).
- Detalle de una publicación con sus comentarios (GET posts/{id} y GET posts/{id}/comments).
- Filtro por usuario (GET posts?userId=N).
- Crear una publicación (POST posts con cuerpo JSON).
- Manejo de errores de red y de servidor, con botón Reintentar.

## Estructura de paquetes

    com.yldefonso.murocampus/
    ├── MainActivity.kt
    ├── model/
    │   └── Modelos.kt
    ├── data/
    │   ├── remote/
    │   │   ├── PostApi.kt
    │   │   ├── RetrofitClient.kt
    │   │   └── UsuarioApi.kt
    │   ├── PostRepository.kt
    │   └── UsuarioRepository.kt
    ├── viewmodel/
    │   ├── PostsViewModel.kt
    │   ├── PostsViewModelFactory.kt
    │   └── UiState.kt
    └── ui/
        ├── Componentes.kt
        ├── PantallaLista.kt
        ├── PantallaDetalle.kt
        ├── PantallaCrear.kt
        └── theme/
            ├── Color.kt
            ├── Theme.kt
            └── Type.kt

## Arquitectura MVVM + Repository

    View (PantallaLista, PantallaDetalle, PantallaCrear)
       |  eventos: cargar, filtrar, publicar        ^  collectAsStateWithLifecycle()
       v                                            |
    ViewModel (PostsViewModel) --- StateFlow<UiState> (Cargando / Exito / Error)
       |
       v
    PostRepository (interface)  <- devuelve Result<...>
       -> PostRepositoryRemote (captura IOException, HttpException...)
       |
       v
    PostApi (Retrofit) --- OkHttp ---> https://jsonplaceholder.typicode.com/

La View solo conoce al ViewModel, el ViewModel solo conoce la interface del Repository, y el Repository es el único que habla con Retrofit y convierte las excepciones en `Result.failure`.

## Capturas

### Lista
![Lista](capturas/lista.png)

### Detalle
![Detalle](capturas/detalle.png)

### Filtro por usuario
![Filtro](capturas/filtro.png)

### Crear publicación
![Crear](capturas/crear.png)

### Error de conexión
![Error](capturas/error.png)

## Para pensar

**1. ¿Qué diferencia hay entre GET y POST? ¿Por qué crear lleva @Body y porUsuario lleva @Query?**
GET solo lee datos y no cambia nada en el servidor; POST envía datos para crear un recurso. Crear lleva `@Body` porque la publicación viaja como JSON dentro del cuerpo de la petición. `porUsuario` lleva `@Query` porque el filtro es un parámetro opcional de lectura que va en la URL (`?userId=2`).

**2. ¿Por qué la ruta es "posts" y no "/posts"?**
Retrofit une la `baseUrl` con la ruta. Si la ruta empieza con "/", reemplaza el camino de la URL base en lugar de agregarse a él. Por eso las rutas van sin barra inicial.

**3. ¿Por qué el Repository devuelve Result en vez de dejar que la excepción llegue al ViewModel?**
Así el error es un valor que el ViewModel recibe y traduce a un mensaje. Si la excepción llegara sin capturar, el ViewModel tendría que conocer tipos de red (IOException, HttpException) y la app podría cerrarse si nadie la captura.

**4. ¿Qué pasa si olvidas el permiso INTERNET? ¿Y si olvidas ignoreUnknownKeys y el servidor agrega un campo nuevo?**
Sin el permiso, la app compila pero cada llamada falla con un error de red aunque tengas internet. Sin `ignoreUnknownKeys = true`, kotlinx.serialization lanza una SerializationException al encontrar un campo que no está en la data class, y la pantalla muestra "Respuesta inesperada".

**5. ¿Por qué la pantalla de lista usa un when sobre UiState y no un if con tres variables?**
Con un `sealed interface`, cada estado es excluyente y el compilador obliga a cubrir los tres casos. Con tres variables booleanas podrían existir combinaciones inválidas, como estar cargando y mostrar un error a la vez.

**6. JSONPlaceholder no guarda los POST. ¿Cómo cambiaría tu app si usaras un servicio real?**
El servidor asignaría el id y guardaría la publicación, así que la app tendría que usar la respuesta del POST para mostrarla. Además, convendría guardar las publicaciones en una base local (Room) para verlas sin conexión, y recargar la lista después de publicar para que aparezca la nueva.
