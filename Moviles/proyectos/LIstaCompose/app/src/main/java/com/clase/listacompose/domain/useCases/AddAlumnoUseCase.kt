package com.clase.listacompose.domain.useCases

import com.clase.listacompose.data.AlumnoRepository
import com.clase.listacompose.domain.model.Alumno

class AddAlumnoUseCase(private val repository: AlumnoRepository) {
    operator fun invoke(alumno: Alumno) {
        repository.insertarAlumno(alumno)
    }
}