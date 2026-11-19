package com.clase.listacompose.ui.main

import androidx.lifecycle.ViewModel
import com.clase.listacompose.data.AlumnoRepository
import com.clase.listacompose.domain.model.Alumno
import com.clase.listacompose.domain.useCases.AddAlumnoUseCase
import com.clase.listacompose.domain.useCases.CargarAlumnosUseCase
import com.clase.listacompose.domain.useCases.DelAlumnoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel(){

    private val repository = AlumnoRepository()

    private val addAlumno = AddAlumnoUseCase(repository)
    private val cargarAlumnos = CargarAlumnosUseCase(repository)
    private val delAlumno = DelAlumnoUseCase(repository)

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    public fun cargar() {
        _state.value = _state.value.copy(
            alumnos = cargarAlumnos()
        )
    }

    public fun insertarAlumno(
            nombre: String, apellidos: String,
            curso: String, edad: Int,
            email: String, activo: Boolean
    ) {
        val id = (_state.value.alumnos.maxOfOrNull { it.id } ?: 0) + 1

        addAlumno(
            Alumno(id, nombre, apellidos, curso, edad, email, activo)
        )

        cargar()
    }


    public fun seleccionarAlumno(id: Int) {
        _state.value = _state.value.copy(
            alumnoSeleccionado = id
        )
    }

    public fun borrarAlumno() {
        val id = _state.value.alumnoSeleccionado ?: return

        delAlumno(id)

        _state.value = _state.value.copy(
            alumnos = cargarAlumnos(),
            alumnoSeleccionado = null
        )
    }
}