package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio6 {
    public static void main(String[] args) {
         CompletableFuture
            .supplyAsync(() -> 10)
            .thenApply(x -> x * 2)
            .thenApply(x -> x + 5)
            .thenApply(String::valueOf)
            .thenAccept(x -> System.out.println("Resultado: " + x))
            .join();
    }
}
