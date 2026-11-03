package com.clase.micompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clase.micompose.ui.theme.MiComposeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModel.MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiComposeTheme {
                MainPantalla(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainPantalla(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainPantallaContent(
        modifier = modifier,
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onEdadChange = viewModel::onEdadChange,
        onAdivinoChange = viewModel::onAdivinoChange,
        onAddClick = viewModel::respuesta
    )
}

@Composable
fun MainPantallaContent(
    modifier: Modifier = Modifier,
    state: MainState,
    onNombreChange: (String) -> Unit,
    onEdadChange: (Int) -> Unit,
    onAdivinoChange: (Boolean) -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                ElevatedButton(onClick = { onAddClick() }) {
                    Text("Add")
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = { texto -> onNombreChange(texto) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = if (state.edad == 0) "" else state.edad.toString(),
                        onValueChange = { texto ->
                            val entero = texto.toIntOrNull() ?: 0
                            onEdadChange(entero)
                        },
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Adivino?", style = MaterialTheme.typography.bodyMedium)
                Switch(
                    checked = state.adivino,
                    onCheckedChange = { valorBooleano -> onAdivinoChange(valorBooleano) }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (state.contador > 0) {
                    Text(
                        text = (if (!state.adivino) "¡Me da igual que no quieras que adivine! (si, estaba aburrido y sin ideas)\n" else "") +
                                "Se como te llamas ${state.nombre} y que tu edad es ${state.edad}, que has pulsado el boton ${state.contador} veces y ademas sin pistas.",
                        color = Color.White
                    )
                }
            }
        }
    }
}
