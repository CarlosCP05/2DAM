package com.example.mvvmprueba.data

import com.example.mvvmprueba.domain.model.Politico

object Politicos {
    private val politicos =  mutableListOf(
        Politico("Pepe", "Sandia", 2),
        Politico("Leg", "Umbre", 100),
        Politico("Melon", "Polisa", 0),
    )

    fun damePresidente() = politicos[1];
}