# PROMPTS.md — Mejora con IA: Calificar atención médica

## Prompt 1 — Modelo de datos + diálogo de calificación
**Qué le pedí:** agregar el campo `calificacion` a `Cita.kt` y crear un
`RatingDialog.kt` con selector de 1 a 5 estrellas usando AlertDialog.
**Qué tuve que corregir:** [ej. la IA usó `Icons.Default.Star` sin
import correcto / el botón "Calificar" no quedaba deshabilitado con 0
estrellas, tuve que agregar `enabled = estrellasSeleccionadas > 0`].

## Prompt 2 — Integración en Mis Citas
**Qué le pedí:** conectar el diálogo a MisCitasScreen, mostrando el botón
solo en citas Completadas sin calificar.
**Qué tuve que corregir:** [ej. la IA olvidó pasar `citas` como lista
mutable desde AppNavigation, tuve que ajustar el tipo del parámetro /
la recomposición no se disparaba al usar `List` en vez de
`SnapshotStateList`].

## Prompt 3 — Pulido visual
**Qué le pedí:** alinear los colores del diálogo y del texto "Calificado"
con la paleta morada del proyecto (PurpleMid).
**Qué tuve que corregir:** [ej. ajustar manualmente el spacing entre el
pill de estado y el botón / la IA usó un color morado distinto al de
Colors.kt].

## Prompt 4 — Pantalla de Login
**Qué le pedí:** crear LoginScreen.kt como nuevo punto de entrada, con
correo/contraseña y botón habilitado solo con ambos campos llenos.
**Qué tuve que corregir:** [ej. la IA olvidó actualizar startDestination
en AppNavigation / el botón quedaba habilitado sin validar campos vacíos].

## Prompt 5 — Estadísticas en Perfil + Cerrar sesión
**Qué le pedí:** agregar un resumen de citas (totales/confirmadas/
completadas) y un botón para cerrar sesión y volver al Login.
**Qué tuve que corregir:** [ej. tuve que pasar 'citas' manualmente desde
AppNavigation porque la IA no actualizó esa línea / ajustar el color del
botón para que coincida con el resto de la app].