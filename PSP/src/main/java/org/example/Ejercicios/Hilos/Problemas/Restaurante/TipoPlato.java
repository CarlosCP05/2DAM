package org.example.Ejercicios.Hilos.Problemas.Restaurante;

//Reduzco el tiempo que tarda cada plato debido a que cada cliente puede pedir varios platos
public enum TipoPlato {
    ENSALADA(2000),
    PASTA(300),
    PIZZA(400),
    CARNE(500);

    private final int tiempo;

    TipoPlato(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getTiempo() {
        return tiempo;
    }
}
