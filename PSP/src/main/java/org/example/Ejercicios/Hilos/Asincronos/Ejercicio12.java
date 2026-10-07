package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Ejercicio12 {
    public static void main(String[] args) {
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "He esperado 10 segundos";
                })
                .completeOnTimeout("Resultado no disponible", 3, TimeUnit.SECONDS)
                .thenAccept(System.out::println)
                .join();
    }
}
