package com.clase.listacompose.ui.main

import com.clase.listacompose.domain.model.Alumno

data class MainState(
    val alumnos: List<Alumno> = emptyList(),
    val alumnoSeleccionado: Int? = null
)