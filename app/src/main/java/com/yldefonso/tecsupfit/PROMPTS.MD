# PROMPTS.md — Mejora con IA: Cancelar reserva (rama mejora-ia)

Proyecto: TECSUP Fit
Herramienta usada: Gemini (Android Studio)
Archivos trabajados: CancelDialog.kt, ReservasScreen.kt, AppNavigation.kt

---

## Prompt 1 — Diálogo de confirmación (CancelDialog.kt)

**Qué le pedí:**
Crear un componente nuevo `CancelDialog.kt` que muestre un `AlertDialog`
con título "Cancelar reserva", un mensaje de confirmación dinámico usando
el nombre de la clase, un botón "Sí, cancelar" en color de error, y un
botón "No, mantener" para descartar el diálogo sin hacer nada.

**Qué tuve que corregir:**
- La IA generó el import del package con `com.example.tecsupfit` en vez
  de `com.yldefonso.tecsupfit` (mi package real), lo que rompía la
  compilación en cascada apenas se agregaba el archivo al proyecto.
- Tuve que ajustar manualmente el color del botón "Sí, cancelar" porque
  la IA lo dejó con el color primario (verde) en vez de
  `MaterialTheme.colorScheme.error`, lo cual no comunicaba visualmente
  que era una acción destructiva.

---

## Prompt 2 — Integración en la pantalla de reservas (ReservasScreen.kt)

**Qué le pedí:**
Conectar `CancelDialog` dentro de `ReservasScreen.kt`, mostrando un botón
"Cancelar reserva" únicamente en las tarjetas con estado "Confirmada", y
que al confirmar la cancelación, la reserva se elimine de la lista
visible sin afectar las demás tarjetas.

**Qué tuve que corregir:**
- La IA dejó el parámetro `reservas` como `List<Reserva>` (de solo
  lectura), por lo que `reservas.removeAll { ... }` ni siquiera
  compilaba. Tuve que cambiarlo a `SnapshotStateList<Reserva>` para que
  la eliminación disparara la recomposición correctamente.
- El botón "Cancelar reserva" quedó pegado directamente debajo del pill
  de estado sin espacio; agregué un `Spacer` adicional para que no se
  viera apretado.
- Tuve que revisar que el import de `TealPrimary`/colores del proyecto
  apuntara al package correcto (`com.yldefonso.tecsupfit.ui.theme`), ya
  que la IA volvió a mezclar `com.example.tecsupfit` en un import suelto.

---

## Prompt 3 — Datos de prueba para sustentación (AppNavigation.kt)

**Qué le pedí:**
Precargar la lista global de reservas con al menos una reserva
"Confirmada" y una "Completada" al iniciar la app, para poder demostrar
la función de cancelar sin tener que reservar una clase manualmente
antes de cada prueba.

**Qué tuve que corregir:**
- El cálculo del `id` de una reserva nueva (`reservas.size + 1`) generaba
  IDs duplicados una vez que la lista ya no empezaba vacía. Lo cambié a
  `(reservas.maxOfOrNull { it.id } ?: 0) + 1` para evitar colisiones.
- Tuve que revisar que `ClassDetailScreen.kt` usara ese mismo cálculo de
  id corregido, porque la IA solo lo aplicó en un archivo y no propagó
  el cambio al otro punto donde también se crean reservas.

---

## Resumen general de correcciones recurrentes durante la Fase 2

- **Mezcla de packages**: en más de un archivo, la IA generó imports con
  `com.example.tecsupfit` en vez de `com.yldefonso.tecsupfit`, causando
  errores de compilación en cascada. Tuve que revisar cada import
  manualmente y corregirlo.
- **Listas inmutables vs mutables**: la IA tiende a declarar parámetros
  como `List<T>` por defecto; para cualquier pantalla que necesite
  modificar el estado global (agregar, calificar o cancelar), tuve que
  cambiarlo a `SnapshotStateList<T>`.
- **Detalles visuales**: colores de botones e íconos no siempre
  coincidían con la paleta del proyecto (`TealPrimary`, `TealLight`) a la
  primera, y tuve que ajustarlos manualmente comparando contra el
  mockup del profesor.