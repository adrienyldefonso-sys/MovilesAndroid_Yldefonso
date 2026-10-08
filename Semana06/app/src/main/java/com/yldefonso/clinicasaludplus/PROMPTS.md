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

## Hito 2: Calendario Dinámico con java.time.LocalDate y Reorganización de Horarios

### 1. Lo que se solicitó a la IA
* **Objetivo:** Implementar la lógica del calendario dinámico de 5 días hábiles con `java.time.LocalDate`, habilitar navegación entre semanas con `<` y `>`, ampliar el catálogo de turnos médicos en `Repositorio.kt` y reorganizar los botones de hora en una cuadrícula centrada de 3 columnas en `FechaHoraScreen.kt`.
* **Prompt enviado:**
  > "Implementa la lógica de LocalDate para días hábiles en FechaHoraScreen.kt. Amplía la disponibilidad a 8 turnos en Repositorio.kt y distribuye los botones en una LazyVerticalGrid de 3 columnas centradas."

### 2. Resultado Esperado
* Calendario interactivo con el mes/año dinámico ("Octubre 2026").
* Tarjetas de días (Lunes a Viernes) con nombre y número perfectamente centrados.
* Múltiples turnos disponibles en mañana y tarde, organizados de forma uniforme en 3 columnas.

### 3. Correcciones y Ajustes Realizados (Iteración Humana)
* Se amplió la lista de disponibilidades iniciales en `Repositorio.kt` para simular una agenda médica realista de consulta externa (mañana y tarde).
* Se sustituyó el contenedor horizontal estirado por una `LazyVerticalGrid` de 3 columnas con bordes redondeados y alineación centrada para dar un acabado profesional alineado a la maqueta de la rúbrica.

## Hito 3: Resumen Visual, Motivo Opcional y Persistencia de Reserva

### 1. Lo que se solicitó a la IA
* **Objetivo:** Refactorizar `ConfirmarCitaScreen.kt` para alinearlo visualmente con la Pantalla 7 de la maqueta de referencia, incluyendo fecha larga en español con año ("Jueves, 8 de octubre 2026"), resumen estilizado con íconos M3, campo de texto libre para el motivo de la consulta y persistencia en `Repositorio.kt`.
* **Prompt enviado:**
  > "Diseña ConfirmarCitaScreen.kt con una tarjeta de datos del médico, un bloque de resumen con íconos M3 en contenedores circulares azul celeste pastel, un campo de texto vacio 'Motivo de consulta (Opcional)' y un botón 'Agendar cita' que registre la reserva en Repositorio.citasReservadas y navegue a CitaExitosa."

### 2. Resultado Esperado
* Tarjeta del médico con avatar cargado vía Coil (`AsyncImage`), nombre, especialidad y CMP.
* Bloque de resumen con íconos circulares en tono celeste pastel (`0xFFC8D7FF`) con íconos azul marino (`0xFF2A4292`).
* Campo de entrada editable inicializado en blanco para que el usuario escriba su motivo libremente.
* Registro reactivo de la cita en `citasReservadas` dentro de `Repositorio.kt` para bloquear la disponibilidad en consultas posteriores.

### 3. Correcciones y Ajustes Realizados (Iteración Humana)
* Se eliminó el texto predeterminado del motivo de consulta para dejarlo en estado libre (`""`), permitiendo la entrada de texto por parte del usuario mediante un `OutlinedTextField` estilizado.
* Se ajustaron los colores exactos de los contenedores de íconos y tarjetas (`Color(0xFFF8F9FE)` y `Color(0xFFC8D7FF)`) para asegurar paridad gráfica con el prototipo de la rúbrica.
* Se garantizó que el campo de fecha mantenga el año ("2026") de forma explícita en la cadena formateada.