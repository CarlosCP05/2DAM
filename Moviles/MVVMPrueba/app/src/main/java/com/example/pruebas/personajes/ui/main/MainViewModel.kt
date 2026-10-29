package com.example.pruebas.personajes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmprueba.ui.main.MainViewModel
import com.example.pruebas.personajes.domain.model.Personaje
import com.example.pruebas.personajes.domain.useCase.AddPersonajeUseCase
import com.example.pruebas.personajes.domain.useCase.DameMentirosoUseCase
import com.example.pruebas.personajes.domain.useCase.DelPersonajeUseCase
import com.example.pruebas.personajes.domain.useCase.ObtenerPersonajesUseCase

class MainViewModel(
    private val dameMentirosoUseCase: DameMentirosoUseCase,
    private val obtenerPersonajesUseCase: ObtenerPersonajesUseCase,
    private val addPersonajeUseCase: AddPersonajeUseCase,
    private val delPersonajeUseCase: DelPersonajeUseCase,
): ViewModel() {
    private val _state = MutableLiveData<MainState>(MainState("inicio", listOf()))

    val state: LiveData<MainState> = _state

    init {
        handleObtenerPersonajes()
    }

    fun handleDameMentiroso() {
        var mentiroso = dameMentirosoUseCase.dameMentiroso()

        _state.value = _state.value?.copy(mentiroso = mentiroso.toString())
    }

    fun handleObtenerPersonajes() {
        _state.value = _state.value?.copy(personajes = obtenerPersonajesUseCase.obtenerPersonajes())
    }

    fun handleAdd(p: Personaje){
        try {
            addPersonajeUseCase.addPersonaje(p)
            _state.value = _state.value?.copy(personajes = obtenerPersonajesUseCase.obtenerPersonajes())
        } catch (e: Exception) {
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleDel(p: Personaje){
        try {
            delPersonajeUseCase.delPersonaje(p)
            _state.value = _state.value?.copy(personajes = obtenerPersonajesUseCase.obtenerPersonajes())
        } catch (e: Exception) {
            _state.value = _state.value?.copy(error = e.message)
        }
    }

    fun handleLimpiarError() {
        _state.value = _state.value?.copy(error = null)
    }
}

class MainViewModelFactory(
    private val dameMentirosoUseCase: DameMentirosoUseCase,
    val obtenerPersonajesUseCase: ObtenerPersonajesUseCase,
    val addPersonajeUseCase: AddPersonajeUseCase,
    val delPersonajeUseCase: DelPersonajeUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST") return MainViewModel(
                dameMentirosoUseCase,
                obtenerPersonajesUseCase,
                addPersonajeUseCase,
                delPersonajeUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}