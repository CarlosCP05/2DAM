package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio10 {
    public static void main(String[] args) {
        CompletableFuture
            .supplyAsync(() -> {
                throw new RuntimeException("Error al consultar el servidor");
            })
            .exceptionally(e -> "DATOS POR DEFECTO")
            .thenAccept(System.out::println)
            .join();
    }
}
