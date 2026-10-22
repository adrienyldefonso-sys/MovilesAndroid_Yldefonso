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

## Prompt 2
**Texto enviado a Gemini:**
Mejora "DESCUENTO POR VOLUMEN". Aplicar 10 % de descuento por cada línea de pedido cuyo servicio se cobra por KG y cuya cantidad es mayor a 5.0 kg.
Cambios por capa:
1. Entidad (Pedido.kt): agregar parámetro val descuento: Double = 0.0 al final.
2. Base de datos (LavaDatabase.kt): subir versión de 1 a 2.
3. Repositorio (LavaRepository.kt y LavaRepositoryRoom.kt): actualizar firma de crearPedido para incluir descuento: Double. Calcular total = (sumaSubtotales - descuento) y guardar el descuento en el Pedido.
4. ViewModel (NuevoPedidoViewModel.kt): función pura calcularDescuento(lineas), exponer subtotal y descuento como StateFlow, y calcular total como subtotal - descuento. Pasar descuento a repo.crearPedido(...).
5. Vista (NuevoPedidoScreen.kt): en la tarjeta "Resumen", mostrar Subtotal, fila "Descuento por volumen (10 %)" en negativo si descuento > 0 con el color primario, y Total.
6. Vista (DetallePedidoScreen.kt): si pedido.descuento > 0, agregar fila "Descuento por volumen" con monto negativo antes de Total.

**Que genero la IA:**
Modificó los 7 archivos por capa:
- Pedido.kt: Agregó el campo descuento con valor por defecto 0.0.
- LavaDatabase.kt: Incrementó la versión del esquema de 1 a 2.
- LavaRepository.kt y LavaRepositoryRoom.kt: Actualizó la firma de crearPedido con el parámetro descuento y calculó el total guardando dicho valor.
- NuevoPedidoViewModel.kt: Implementó la función pura calcularDescuento con constantes UMBRAL_KG_DESCUENTO (5.0) y PORCENTAJE_DESCUENTO_KG (0.10), y expuso StateFlows para subtotal, descuento y total.
- NuevoPedidoScreen.kt: Renderizó las filas de Subtotal, Descuento por volumen (10 %) en color primario cuando aplica, y Total.
- DetallePedidoScreen.kt: Mostró la fila de Descuento por volumen con monto negativo antes del Total si el pedido tiene descuento guardado.

**Que tuve que corregir yo:** La app se cerraba al abrir con IllegalStateException: A migration from 1 to 2 was required but not found, porque Gemini subió la versión de la base a 2 pero el emulador conservaba la base versión 1. Lo resolví agregando fallbackToDestructiveMigration(dropAllTables = true) en LavaDatabase, que recrea la base y vuelve a sembrar los datos ficticios.
**Respeta MVVM?:** Sí, porque la regla del descuento está en NuevoPedidoViewModel, las pantallas solo observan StateFlow sin usar DAO ni Repository, y crearPedido se cambió primero en la interface LavaRepository y el Repository solo guarda el valor recibido.

## Prompt 3
**Texto enviado a Gemini:**
Mejora "REPETIR UN PEDIDO". Permitir repetir un pedido desde el Historial de Mis pedidos cargando sus líneas en Nuevo pedido con precios actuales.
Cambios por capa:
1. Rutas (Rutas.kt): agregar NUEVO_PEDIDO_RUTA ("nuevo_pedido?repetir={repetir}") y función nuevoPedido(repetir: Int).
2. ViewModel (NuevoPedidoViewModel.kt): agregar parámetro opcional pedidoARepetir: Int? al constructor, avisoRepetir: StateFlow<String?>, limpiarAviso() y lógica en init para copiar cantidades ajustadas según reglas de servicios activos o informar servicios omitidos.
3. Factory (LavaViewModelFactory.kt): actualizar paraNuevoPedido(usuarioId, pedidoARepetir).
4. ViewModel de Mis pedidos (MisPedidosViewModel.kt): agregar función puedeRepetir(pedido) para validar estado ENTREGADO o CANCELADO.
5. Componente (PedidoCard.kt): agregar parámetro opcional onRepetir y renderizar enlace "Pedir de nuevo" en color primario y negrita.
6. Vista (MisPedidosScreen.kt): agregar parámetro onRepetir y pasar callback a PedidoCard únicamente en el historial.
7. Navegación (NavLava.kt): actualizar la ruta de Nuevo pedido para leer el argumento opcional repetir, usar ViewModel key dinámica "nuevo-${u.id}-$repetirArg", y conectar onRepetir desde MisPedidosScreen.
8. Vista (NuevoPedidoScreen.kt): observar vm.avisoRepetir y renderizar tarjeta informativa sobre "Elige tus servicios" si hay un aviso.

**Que genero la IA:**
Modificó los 8 archivos por capa:
- Rutas.kt: Definió el patrón de ruta parametrizada NUEVO_PEDIDO_RUTA y la función generadora nuevoPedido(repetir).
- NuevoPedidoViewModel.kt: Implementó la carga reactiva de líneas anteriores verificando que pertenezcan al mismo estudiante y que correspondan a servicios activos (ajustando límites min/max) en el bloque init, además de exponer avisoRepetir.
- LavaViewModelFactory.kt: Pasó pedidoARepetir a NuevoPedidoViewModel.
- MisPedidosViewModel.kt: Añadió la función de negocio puedeRepetir para pedidos ENTREGADO o CANCELADO.
- PedidoCard.kt: Mostró el enlace "Pedir de nuevo" cuando onRepetir no es nulo.
- MisPedidosScreen.kt: Conectó el evento de repetición desde la pestaña Historial.
- NavLava.kt: Registró la ruta con argumento opcional de repetición y ViewModel con key única.
- NuevoPedidoScreen.kt: Renderizó la tarjeta azul informativa de servicios omitidos sobre el selector de servicios.

**Que tuve que corregir yo:** PENDIENTE: lo completa el estudiante después de revisar el código
**Respeta MVVM?:** PENDIENTE: lo completa el estudiante (Si / No y por qué)

## Reflexion (5 lineas)
La IA mejoró la rapidez al generar en pocos minutos las consultas, las funciones del Repository y los cambios visuales, aunque no reconoció las convenciones del proyecto como Room 3 y requirió la revisión de cada archivo. Sin embargo, no cometió ningún error y la tarea se realizó correctamente. Aprendí que cualquier mejora atraviesa las capas de MVVM respetando su función única (el DAO consulta, el Repository delega, el ViewModel decide y la View muestra), además de entender cómo Room usa Flow para actualizar la pantalla y la necesidad de centralizar reglas de negocio probando casos límite.
