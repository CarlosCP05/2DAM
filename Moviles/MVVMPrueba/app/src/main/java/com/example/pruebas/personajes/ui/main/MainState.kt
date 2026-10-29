package com.example.pruebas.personajes.ui.main

import com.example.pruebas.personajes.domain.model.Personaje

data class MainState (
    val mentiroso: String,
    val personajes: List<Personaje>,
    val error: String? = null,

)