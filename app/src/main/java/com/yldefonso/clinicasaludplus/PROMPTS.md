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