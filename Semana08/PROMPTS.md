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

**Que genero la IA:**
Modificó los 7 archivos por capa:
- PedidoDao.kt: Agregó dos consultas SQL en Room 3 para obtener IDs (Flow<List<Int>>) y cantidad (Flow<Int>) de pedidos atrasados.
- LavaRepository.kt y LavaRepositoryRoom.kt: Declaró e implementó las funciones observarIdsAtrasados y observarCantidadAtrasados delegando al DAO.
- BandejaViewModel.kt y PanelViewModel.kt: Expuso el StateFlow de IDs atrasados (Set<Int>) y de cantidad (Int) reactivamente con SharingStarted.WhileSubscribed(5000).
- BandejaScreen.kt y PanelScreen.kt: Renderizó borde rojo, chip "Atrasado" y fecha vencida en la bandeja, y tarjeta descriptiva en el panel cuando hay pedidos atrasados.

**Que tuve que corregir yo:** PENDIENTE: lo completa el estudiante después de revisar el código
**Respeta MVVM?:** PENDIENTE: lo completa el estudiante (Si / No y por qué)

## Reflexion (5 lineas)
- Que mejoro la IA y que no?: PENDIENTE
- Que error cometio?: PENDIENTE
- Que aprendi sobre MVVM, Room y reglas de negocio?: PENDIENTE
