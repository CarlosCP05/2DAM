package com.example.mvvmprueba.domain.useCases

import com.example.mvvmprueba.data.Politicos

class CargarPoliticosUseCase {
    fun cargarLista() = Politicos.dameLista()
}