package com.yldefonso.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class PanelViewModel(repo: LavaRepository) : ViewModel() {

    private val hoy = LocalDate.now()
    // Lunes de esta semana (si hoy es domingo, el lunes anterior)
    private val inicioSemana: String =
        hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    private val estadosGrafico = listOf(
        EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
    )

    // Violeta intenso para el mayor y tonos cada vez más claros, como en el mockup
    private val coloresServicio = listOf(
        Color(0xFF6D28D9), Color(0xFFC4B5FD), Color(0xFFDDD6FE), Color(0xFFEDE9FE)
    )

    private fun <T> Flow<T>.comoEstado(inicial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    // ---- Contadores ----
    val pedidosHoy: StateFlow<Int> =
        repo.observarPedidosDeFecha(hoy.toString()).comoEstado(0)

    val enProceso: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO).comoEstado(0)

    val porRecoger: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoPedido.LISTO).comoEstado(0)

    // ---- Ingresos: suma del total de pedidos pagados desde el lunes ----
    val ingresosSemana: StateFlow<Double> =
        repo.observarIngresosDesde(inicioSemana).comoEstado(0.0)

    // ---- Gráficos: Room -> DatoGrafico ----
    val porEstado: StateFlow<List<DatoGrafico>> =
        combine(estadosGrafico.map { repo.observarCantidadPorEstado(it) }) { cantidades ->
            estadosGrafico.mapIndexed { i, estado ->
                DatoGrafico(estado.etiqueta, cantidades[i].toFloat(), estado.color)
            }
        }.comoEstado(emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> =
        repo.observarIngresosPorServicio(inicioSemana)
            .map { lista ->
                lista.sortedByDescending { it.total }
                    .mapIndexed { i, ing ->
                        DatoGrafico(etiquetaCorta(ing.nombre), ing.total.toFloat(), coloresServicio[i % coloresServicio.size])
                    }
            }
            .comoEstado(emptyList())
}

/** "Lavado y secado" -> "Lavado" · "Lavado en seco" -> "Seco" · "Planchado" -> "Planchado" */
private fun etiquetaCorta(nombre: String): String =
    (if (nombre.contains(" en ")) nombre.substringAfter(" en ") else nombre.substringBefore(" "))
        .replaceFirstChar { it.uppercase() }