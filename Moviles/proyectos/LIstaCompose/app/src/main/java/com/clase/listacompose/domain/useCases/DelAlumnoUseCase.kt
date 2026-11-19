package com.clase.listacompose.domain.useCases

import com.clase.listacompose.data.AlumnoRepository

class DelAlumnoUseCase(private val repository: AlumnoRepository) {

    operator fun invoke(id: Int) {
        repository.borrarAlumno(id)
    }
}