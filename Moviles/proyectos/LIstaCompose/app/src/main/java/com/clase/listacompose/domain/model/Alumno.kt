package com.clase.listacompose.domain.model

data class Alumno (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val curso: String,
    val edad: Int,
    val email: String,
    val activo: Boolean
) {}
