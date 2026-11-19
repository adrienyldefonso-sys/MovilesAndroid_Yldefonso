
package com.yldefonso.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

import com.yldefonso.campusgps.R
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.ui.components.BototonPrincipal
import com.yldefonso.campusgps.ui.components.IconosCampus
import com.yldefonso.campusgps.ui.theme.Dimens
import com.yldefonso.campusgps.util.formatearDistancia
import com.yldefonso.campusgps.viewmodel.MapaViewModel

/**
 * [G] Pantalla principal de CampusGPS.
 *
 * Conserva el funcionamiento original de MVVM,
 * permisos, mapa, filtros y navegación.
 *
 * Mejora visualmente el botón de lugar más cercano.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    // Estados observados desde el ViewModel.
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val lugaresVisibles by vm.lugaresVisibles.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val radioMetros by vm.radioMetros.collectAsState()
    val idsDentroDelRadio by vm.idsDentroDelRadio.collectAsState()
    val categoriasActivas by vm.categoriasActivas.collectAsState()

    val contexto = LocalContext.current
    val cicloVidaOwner = LocalLifecycleOwner.current

    // Mantiene el control de permisos existente.
    val permisoUbicacion = remember {
        val fine = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        mutableStateOf(fine || coarse)
    }

    // Actualiza el estado del permiso al volver a la app.
    DisposableEffect(cicloVidaOwner) {

        val observador = LifecycleEventObserver { _, evento ->

            if (evento == Lifecycle.Event.ON_RESUME) {

                val fine = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarse = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                permisoUbicacion.value = fine || coarse
            }
        }

        cicloVidaOwner.lifecycle.addObserver(observador)

        onDispose {
            cicloVidaOwner.lifecycle.removeObserver(observador)
        }
    }

    // Solicita los permisos cuando sea necesario.
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        val concedido = resultado.values.any { it }

        permisoUbicacion.value = concedido

        if (concedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    /**
     * Mantiene la búsqueda de ubicación original.
     */
    fun alPulsar() {

        val fine = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        permisoUbicacion.value = fine || coarse

        if (fine || coarse) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = stringResource(R.string.subtitulo_app),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                                .copy(alpha = 0.85f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Mapa: conserva el espacio flexible y la lógica original.
            MapaUbicacion(
                estado = estado,
                lugares = lugaresVisibles,
                seleccionado = seleccionado,
                masCercano = masCercano,
                radioMetros = radioMetros,
                permisoUbicacion = permisoUbicacion.value,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            // Panel de filtros y acciones.
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.espacioS),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.espacioM),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Filtros con los nuevos iconos vectoriales.
                    FiltroCategorias(
                        categorias = vm.categoriasDisponibles,
                        categoriasActivas = categoriasActivas,
                        onToggleCategoria = {
                            vm.alternarCategoria(it)
                        }
                    )

                    Spacer(
                        Modifier.height(Dimens.espacioXs)
                    )

                    // Selector del radio de cercanía.
                    SelectorRadio(
                        radioActual = radioMetros,
                        onRadioChange = {
                            vm.cambiarRadio(it)
                        },
                        cantidadDentro = idsDentroDelRadio.size
                    )

                    Spacer(
                        Modifier.height(Dimens.espacioM)
                    )

                    // Botón principal: conserva su comportamiento.
                    BototonPrincipal(
                        texto = stringResource(
                            R.string.btn_obtener_ubicacion
                        ),
                        onClick = {
                            alPulsar()
                        },
                        enabled = estado !is UbicacionUi.Buscando
                    )

                    Spacer(
                        Modifier.height(Dimens.espacioS)
                    )

                    // [G] Botón secundario destacado.
                    // Solo se habilita cuando existe un lugar cercano.
                    OutlinedButton(
                        onClick = {
                            masCercano?.let {
                                vm.seleccionar(it.id)
                            }
                        },
                        enabled = masCercano != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (masCercano != null) {
                                Color(0xFF6547F5)
                            } else {
                                Color(0xFFB9B4CB)
                            }
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFEAE4FF),
                            contentColor = Color(0xFF4D35C8),
                            disabledContainerColor = Color(0xFFF0EDF6),
                            disabledContentColor = Color(0xFF89849B)
                        )
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = IconosCampus.Navegacion,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(
                                Modifier.size(Dimens.espacioS)
                            )

                            Text(
                                text = stringResource(
                                    R.string.btn_ir_mas_cercano
                                ),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    // Información del lugar más cercano.
                    masCercano?.let { cercano ->

                        val metros = lugares.firstOrNull {
                            it.lugar.id == cercano.id
                        }?.metros

                        Spacer(
                            Modifier.height(Dimens.espacioS)
                        )

                        Text(
                            text = if (metros != null) {
                                stringResource(
                                    R.string.mas_cercano_formato,
                                    cercano.nombre,
                                    formatearDistancia(metros)
                                )
                            } else {
                                stringResource(
                                    R.string.mas_cercano_simple,
                                    cercano.nombre
                                )
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(
                        Modifier.height(Dimens.espacioS)
                    )

                    // Estados de ubicación y errores.
                    when (val e = estado) {

                        UbicacionUi.Inicial -> {
                            Text(
                                text = stringResource(
                                    R.string.texto_inicial
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        UbicacionUi.Buscando -> {
                            CircularProgressIndicator()
                        }

                        is UbicacionUi.Lista -> {
                            Text(
                                text = stringResource(
                                    R.string.lat_long_formato,
                                    e.posicion.latitud,
                                    e.posicion.longitud
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        is UbicacionUi.Error -> {
                            MensajeError(
                                error = e,
                                onReintentar = {
                                    alPulsar()
                                }
                            )
                        }
                    }
                }
            }

            // Lista: mantiene su altura fija y sus callbacks.
            ListaLugares(
                lugares = lugaresVisibles,
                seleccionado = seleccionado,
                idsDentroDelRadio = idsDentroDelRadio,
                onClick = { id ->
                    vm.seleccionar(id)
                },
                onComoLlegar = { lugar ->
                    abrirRutaAPie(contexto, lugar)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.alturaLista)
            )
        }
    }
}
