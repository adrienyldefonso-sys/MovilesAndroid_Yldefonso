# PROMPTS.md

Registro del uso de IA en la Fase 2 (rama mejora-ia-lab15).
Asistente usado en las mejoras: Claude. El lab pide Gemini: si usaste Gemini en algún paso, registra ese paso aparte con su respuesta real.

## Mejora 1 · Texto largo con BigTextStyle (commit 1)

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Elige 3 mejoras y empecemos con la mejora 1: texto largo con BigTextStyle, en Notificador.kt» | Propuso tres mejoras y entregó el Notificador.kt completo con `setStyle(NotificationCompat.BigTextStyle().bigText(...))`. | Para probarla alargué temporalmente el texto de "Tarea lista" en AvisosViewModel. Lo revertí con `git checkout` antes del commit, y el commit solo incluye Notificador.kt. | Cambia solo Notificador, así que no altera la arquitectura MVVM. La prueba se confirmó con la notificación expandida mostrando el texto completo. |

## Mejora 2 · Dos canales: Avisos y Urgentes (commit 2)

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Mejora 2: canal Urgentes con IMPORTANCE_HIGH para el recordatorio; el resto sigue en Avisos.» | Entregó Notificador.kt con `createNotificationChannels`, `Aviso` con el campo `urgente` por defecto `false`, y el ViewModel emitiendo el recordatorio con `copy(urgente = true)`. | [PENDIENTE: qué ajustaste después de probar] | El ViewModel decide qué es urgente y la View solo muestra. El campo tiene valor por defecto para no cambiar el Repository. |
| [PENDIENTE: prueba del canal urgente] | [PENDIENTE: qué pasó en el emulador. Por ejemplo, si el recordatorio no salió como emergente y qué revisaste en Ajustes o con `dumpsys notification`] | [PENDIENTE: cambio que hiciste para corregirlo, si lo hubo] | [PENDIENTE: causa que encontraste] |

## Mejora 3 · Posponer 10 s con BroadcastReceiver (commit 3)

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Mejora 3: botón Posponer 10 s en la notificación con un BroadcastReceiver propio.» | Entregó Notificador.kt con `addAction` y un PendingIntent de broadcast por aviso, PosponerReceiver.kt con `Handler` de 10 s y la entrada en el Manifest con `exported="false"`. | [PENDIENTE: ajustes que hiciste] | Usar `applicationContext` en el Handler evita retener la Activity. Se anota la limitación: si Android cierra el proceso antes de los 10 s, el aviso no reaparece. |
| [PENDIENTE: verificación del BroadcastReceiver] | [PENDIENTE: resultado de tocar Posponer en el emulador] | [PENDIENTE: correcciones] | [PENDIENTE: criterio técnico] |

## Rediseño visual (commit 4)

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Mejora todo el diseño: colores, formas y letras, algo innovador y no simple.» | Entregó tema oscuro, componentes reutilizables (GlassCard, GradientButton, StatusPill) y una línea de tiempo para el historial. | [PENDIENTE: lo que cambiaste al verlo en el emulador] | La lógica no cambia. Se mantiene el título AvisosCampus y las tarjetas que pide el lab. |