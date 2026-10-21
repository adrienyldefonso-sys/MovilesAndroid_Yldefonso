# Prompts utilizados

## Prompt 1
**Texto enviado a Gemini:**
Eres un desarrollador Android senior. Trabajas en LavaCampus, una app de lavandería universitaria hecha con Kotlin, Jetpack Compose, Material 3 (sin XML), MVVM + Repository y Room. Debes agregar una mejora SIN romper la arquitectura. Antes de escribir código, lee los archivos existentes y sigue su estilo.

CONTEXTO Y REGLAS:
- Capas: View (ui/) -> ViewModel (viewmodel/) -> LavaRepository (interface en data/repository/) -> LavaRepositoryRoom -> DAO (data/local/dao/) -> Room.
- Room 3: imports de androidx.room3.*
- Flow para lecturas reactivas.

TAREA: "PEDIDOS ATRASADOS"
Un pedido está ATRASADO si su fechaEntrega es menor que la fecha de hoy Y su estado es distinto de ENTREGADO y de CANCELADO.

Cambios realizados por capa:
1. DAO (PedidoDao.kt): consultas observarIdsAtrasados(hoy: String) y observarCantidadAtrasados(hoy: String) con filtro fechaEntrega < :hoy AND estado NOT IN ('ENTREGADO', 'CANCELADO').
2. Repository (LavaRepository.kt y LavaRepositoryRoom.kt): declaración de ambas funciones en la interface e implementación delegando al DAO.
3. ViewModel de Bandeja (BandejaViewModel.kt): expone atrasados: StateFlow<Set<Int>> calculando hoy con LocalDate.now().toString() y usando stateIn con SharingStarted.WhileSubscribed(5000).
4. ViewModel del Panel (PanelViewModel.kt): expone atrasados: StateFlow<Int> con la cantidad usando el mismo patrón stateIn.
5. View de Bandeja (BandejaScreen.kt): observa vm.atrasados con collectAsState(), pasa atrasado: Boolean a PedidoBandejaItem y muestra borde rojo (MaterialTheme.colorScheme.error), chip "Atrasado" y fecha de entrega vencida en formato dd/MM/aaaa.
6. View del Panel (PanelScreen.kt): observa vm.atrasados con collectAsState() y muestra tarjeta roja suave con "N pedidos atrasados" (singular si es 1) cuando atrasados > 0.

**Que tuve que corregir yo:** Revisé el código generado archivo por archivo antes de aceptarlo. No hubo ningun error,unicamente verificaciones de imports.
- [Si hubo error] Gemini importó androidx.room.* en el DAO; lo cambié a androidx.room3.* porque el proyecto usa Room 3.
- [Si hubo error] La fecha de hoy se calculaba en la View; la moví a BandejaViewModel con LocalDate.now() para que la View solo muestre.
- [Si hubo ajuste visual] Ajusté el borde rojo y la etiqueta "Atrasado" para que usen MaterialTheme.colorScheme.error y no un color fijo, y para que la tarjeta se vea igual que el resto cuando no está atrasada.
- [Si no hubo errores] No encontré errores de arquitectura; solo verifiqué que la consulta usara "<" (el pedido que vence hoy no es atrasado) y que excluyera ENTREGADO y CANCELADO.

**Respeta MVVM?:** Si. La View (BandejaScreen y PanelScreen) solo observa los StateFlow con collectAsState() y no usa el DAO ni el Repository. Las funciones nuevas se declararon primero en la interface LavaRepository y LavaRepositoryRoom solo delega al DAO. La fecha de hoy y los StateFlow están en los ViewModels, y no se modificó ninguna entidad ni la versión de la base.

## Reflexion (5 lineas)
La IA mejoró la rapidez al generar en pocos minutos las consultas, las funciones del Repository y los cambios visuales, aunque no reconoció las convenciones del proyecto como Room 3 y requirió la revisión de cada archivo. Sin embargo, no cometió ningún error y la tarea se realizó correctamente. Aprendí que cualquier mejora atraviesa las capas de MVVM respetando su función única (el DAO consulta, el Repository delega, el ViewModel decide y la View muestra), además de entender cómo Room usa Flow para actualizar la pantalla y la necesidad de centralizar reglas de negocio probando casos límite.