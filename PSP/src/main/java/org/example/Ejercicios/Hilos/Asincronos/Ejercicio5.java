package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio5 {
    public static void main(String[] args) {
        CompletableFuture<Integer> resultado = CompletableFuture.supplyAsync(() -> {
            return 10+ 20;
        }).thenApply(x -> x * 2);
        resultado.thenAccept(x -> System.out.println("Resultado: " + x));
    }
}
