package com.clase.listacompose.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun FormularioAlumno(
    onInsertar: (String, String, String, Int, String, Boolean) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    Column( verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Nuevo alumno", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre = it },
            label = { Text("Nombre") }
        )

        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") }
        )

        OutlinedTextField(
            value = curso,
            onValueChange = { curso = it },
            label = { Text("Curso") }
        )

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Row {
            Checkbox(
                checked = activo,
                onCheckedChange = { activo = it }
            )
        }

        Button(
            onClick = {
                val edadNumero = edad.toIntOrNull()

                if( nombre.isNotBlank() && apellido.isNotBlank() &&
                    curso.isNotBlank() && edadNumero != null &&
                    email.isNotBlank()
                ) {
                    onInsertar(nombre, apellido, curso, edadNumero, email, activo)

                    nombre = ""
                    apellido = ""
                    curso = ""
                    edad = ""
                    email = ""
                    activo = true
                }
            }
        ) {
            Text("Añadir alumno")
        }
    }
}
