package com.clase.listacompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clase.listacompose.domain.model.Alumno
import com.clase.listacompose.ui.component.FormularioAlumno

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.state.collectAsState()

            MaterialTheme {
                MainScreen(
                    state = state,
                    onSeleccionar = viewModel::seleccionarAlumno,
                    onBorrar = viewModel::borrarAlumno,
                    onInsertar = viewModel::insertarAlumno,
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    state: MainState,
    onSeleccionar: (Int) -> Unit,
    onBorrar: () -> Unit,
    onInsertar: (String, String, String, Int, String, Boolean) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)){
        Text(
            text = "Gestión de alumnos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        ListaAlumnos(
            alumnos = state.alumnos,
            seleccionado = state.alumnoSeleccionado,
            onSeleccionar = onSeleccionar
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormularioAlumno(
            onInsertar = onInsertar
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBorrar,
            enabled = state.alumnoSeleccionado != null
        ) {
            Text("Borrar alumno")
        }
    }
}

@Composable
fun ListaAlumnos(
    alumnos: List<Alumno>,
    seleccionado: Int?,
    onSeleccionar: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier
        .heightIn(max = 400.dp)
        //.fillMaxHeight(0.5f)//La mitad pero no se adapta
    ) {
        items(alumnos) { alumno ->
            AlumnoItem(
                alumno = alumno,
                seleccionado = seleccionado == alumno.id,
                onSeleccionar = { onSeleccionar(alumno.id) }
            )
        }
    }
}

@Composable
fun AlumnoItem(
    alumno: Alumno,
    seleccionado: Boolean,
    onSeleccionar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable{ onSeleccionar() },
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${alumno.nombre} ${alumno.apellido}")
            Text("Curso: ${alumno.curso}")
            Text("Edad: ${alumno.edad}")
            Text("Email: ${alumno.email}")
            Text(if (alumno.activo) "Activo" else "Inactivo")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    val alumnos = listOf(
        Alumno(
            id = 1,
            nombre = "Carlos",
            apellido = "García López",
            curso = "DAM",
            edad = 22,
            email = "carlos@gmail.com",
            activo = true
        ),
        Alumno(
            id = 2,
            nombre = "María",
            apellido = "Fernández Ruiz",
            curso = "DAW",
            edad = 20,
            email = "maria@gmail.com",
            activo = true
        )
    )

    val state = MainState(
        alumnos = alumnos,
        alumnoSeleccionado = 1
    )

    MaterialTheme {
        MainScreen(
            state = state,
            onSeleccionar = {},
            onBorrar = {},
            onInsertar = { _, _, _, _, _, _ -> }
        )
    }
}