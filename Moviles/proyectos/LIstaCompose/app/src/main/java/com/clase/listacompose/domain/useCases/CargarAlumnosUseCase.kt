package com.clase.listacompose.domain.useCases

import com.clase.listacompose.data.AlumnoRepository
import com.clase.listacompose.domain.model.Alumno

class CargarAlumnosUseCase(private val repository: AlumnoRepository) {

    operator fun invoke(): List<Alumno>{
        return repository.cargarAlumnos()
    }
}