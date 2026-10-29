package com.example.pruebas.personajes.data

import com.example.pruebas.personajes.domain.model.Personaje

object Personajes {

    private val personajes = mutableListOf(
        Personaje("Juanito", true),
        Personaje("Lobo"),
        Personaje("Vecino")
    )

    fun dameMentiroso() = personajes[0]

    fun dameLista() = personajes.toList()

    fun add(p: Personaje) = personajes.add(p)

    fun del(p: Personaje) = personajes.remove(p)
}