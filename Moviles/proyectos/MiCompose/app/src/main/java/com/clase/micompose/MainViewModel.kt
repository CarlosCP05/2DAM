package com.clase.micompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun onNombreChange(nuevo: String) {
        _state.update { it.copy(nombre = nuevo) }
    }

    fun onEdadChange(nuevo: Int) {
        _state.update { it.copy(edad = nuevo) }
    }

    fun onAdivinoChange(nuevo: Boolean){
        _state.update { it.copy(adivino = nuevo) }
    }

    fun respuesta() {
        _state.update { it.copy(contador = it.contador + 1) }
        val datos = _state.value

        if (datos.adivino){
            println("¡Me da igual que no quieras que adivine! (si, estaba aburrido y sin ideas)")
        }
        println("Se como te llamas ${datos.nombre} y que tu edad es ${datos.edad}, que has pulsado el boton ${datos.contador} veces y ademas sin pistas.")
    }

    class MainViewModelFactory: ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHEKED_CAST") return MainViewModel() as T
            }
            throw IllegalArgumentException("Unknow ViewModel class")
        }
    }
}