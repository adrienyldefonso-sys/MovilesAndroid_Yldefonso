# AvisosCampus

App Android hecha con Kotlin, Jetpack Compose y Material 3 (sin XML para las pantallas). Muestra notificaciones locales, programa un recordatorio a los 10 segundos y escucha los cambios del modo avión mediante un BroadcastReceiver. Todos los avisos quedan registrados en un historial.

- **Patrón:** MVVM + Repository (interface)
- **Tecnologías:** Kotlin, Jetpack Compose, Material 3, Corrutinas, BroadcastReceiver, NotificationCompat
- **Datos:** ficticios, sin internet ni claves

## Funciones

1. **Permiso de notificaciones:** pide `POST_NOTIFICATIONS` en Android 13 o superior y muestra su estado (Sin pedir, Permitido o Denegado).
2. **Mostrar aviso:** muestra la notificación **Tarea lista**. Si se pulsa dos veces, salen dos notificaciones distintas.
3. **Recordatorio:** cuenta regresiva de 10 segundos con opción de cancelar. Al llegar a 0, muestra **Recordatorio**.
4. **Modo avión:** con el switch activado, avisa cuando el modo avión se activa o desactiva.
5. **Historial:** lista los últimos 20 avisos, con origen, título, texto y hora.
6. **Permiso denegado:** la app sigue funcionando, guarda el aviso en el historial, muestra un mensaje y tiene un botón para abrir los ajustes de notificaciones.

## Estructura de paquetes

```
com.yldefonso/
├── AvisosApp.kt                 Application: crea el canal al iniciar
├── MainActivity.kt              Actividad principal
├── model/
│   └── Modelos.kt               Aviso, Origen, PermisoUi
├── data/
│   └── AvisosRepository.kt      Interface + AvisosRepositoryMemoria
├── notificacion/
│   └── Notificador.kt           Canal, permiso y mostrar la notificación
├── receiver/
│   └── ModoAvionReceiver.kt     BroadcastReceiver del modo avión
├── viewmodel/
│   ├── AvisosViewModel.kt       Estado y lógica (sin Context)
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt        Pantalla con las tarjetas
    ├── HistorialAvisos.kt       Tarjeta del historial
    └── theme/                   Colores y tema Material 3
```

## Arquitectura MVVM + Repository

```
View (PantallaAvisos)
  | eventos: botones, switch
  ^ collectAsState()
  |
ViewModel (AvisosViewModel) --- StateFlow (avisos, permiso, faltan...) --->  View
  | registra el aviso                 | emite evento (SharedFlow<Aviso>)
  v                                   v
AvisosRepository (interface)     View recibe el evento y llama a
  -> AvisosRepositoryMemoria     Notificador.mostrar(context, aviso)
```

- **View:** solo conoce su ViewModel y el Notificador. Tiene el `Context`.
- **ViewModel:** decide qué avisar y lo guarda en el Repository. No tiene `Context`, `NotificationManager` ni `BroadcastReceiver`.
- **Repository:** no usa la UI ni `Context`.
- **Notificador:** es el único que habla con el sistema de notificaciones.

## Capturas

1. Notificación en el panel: `docs/capturas/01-notificacion.png`
2. Recordatorio con cuenta regresiva: `docs/capturas/02-recordatorio.png`
3. Modo avión: `docs/capturas/03-modo-avion.png`
4. Historial: `docs/capturas/04-historial.png`
5. Permiso denegado: `docs/capturas/05-permiso-denegado.png`

![Notificación](docs/capturas/01-notificacion.png)
![Recordatorio](docs/capturas/02-recordatorio.png)
![Modo avión](docs/capturas/03-modo-avion.png)
![Historial](docs/capturas/04-historial.png)
![Permiso denegado](docs/capturas/05-permiso-denegado.png)

## Para pensar

**¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?**
Desde Android 8 (API 26) cada notificación pertenece a un canal, y el usuario controla desde Ajustes su importancia y sonido. Sin canal, la notificación no se muestra. Desde Android 13 (API 33) hace falta el permiso `POST_NOTIFICATIONS`; sin él, el sistema bloquea las notificaciones. En esta app, `Notificador.mostrar` devuelve `false` en ese caso, y la pantalla muestra el mensaje de error.

**¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?**
Porque mostrar una notificación necesita `Context`, y el ViewModel no debe tenerlo: así sobrevive a la rotación sin filtrar la pantalla y se puede probar sin Android. El ViewModel decide qué avisar, la pantalla lo recibe y el Notificador lo muestra.

**¿Qué pasa si usas el mismo id en dos notificaciones?**
Android reemplaza la primera notificación por la segunda, porque ambas tienen el mismo identificador. Por eso el id se toma de `aviso.id`, que cambia en cada aviso.

**¿Por qué `onReceive` debe ser corto y por qué hay que quitar el receptor al salir?**
`onReceive` corre en el hilo principal. Si tarda demasiado, Android puede mostrar un error de "aplicación no responde". El receptor se registra solo mientras el switch está activado; si no se quita en `onDispose`, sigue recibiendo anuncios y retiene la pantalla en memoria.

**¿En qué casos usarías una notificación local y en cuáles una push de Firebase?**
Una notificación local nace en el propio celular, como el recordatorio o el aviso del modo avión, y no necesita internet. Una push de Firebase (FCM) la envía un servidor cuando hay información que el celular no puede generar, por ejemplo una entrega nueva o un mensaje de otra persona, incluso con la app cerrada.