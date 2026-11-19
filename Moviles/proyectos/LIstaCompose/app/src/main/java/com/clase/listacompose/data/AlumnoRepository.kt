package com.clase.listacompose.data

import com.clase.listacompose.domain.model.Alumno

class AlumnoRepository {

    private val alumnos = mutableListOf(
        Alumno(1, "Lucía", "Álvarez García", "1º DAM", 18, "lucia@gmail.com", true),
        Alumno(2, "Marco", "Castillo Ruiz", "2º ASIR", 19, "marco@gmail.com", true),
        Alumno(3, "Sofía", "Pérez Martín", "1º SMR", 17, "sofia@gmail.com", false),
        Alumno(4, "Daniel", "Rodríguez López", "2º DAM", 20, "daniel@gmail.com", true),
        Alumno(5, "Elena", "Moreno Sánchez", "1º ASIR", 18, "elena@gmail.com", true)
    )

    fun cargarAlumnos(): List<Alumno> {
        return alumnos.toList()
    }

    fun insertarAlumno(alumno: Alumno) {
        alumnos.add(alumno)
    }

    fun borrarAlumno(id: Int) {
        alumnos.removeAll { it.id == id }
    }
}