package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio8 {
    public static void main(String[] args) {
        CompletableFuture<String> temperatura = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                        return "Temperatura: 25°C";
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        CompletableFuture<String> humedad = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        Thread.sleep(3000);
                        return "Humedad: 60%";
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        temperatura.thenCombine(humedad, (temp, hum) -> temp + "\n" + hum)
                .thenAccept(System.out::println)
                .join();
    }
}
