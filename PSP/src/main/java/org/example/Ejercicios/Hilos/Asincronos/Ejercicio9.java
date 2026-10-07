package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Ejercicio9 {
    public static void main(String[] args) {
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> descargar(2)),
            CompletableFuture.runAsync(() -> descargar(1)),
            CompletableFuture.runAsync(() -> descargar(3)),
            CompletableFuture.runAsync(() -> descargar(2)),
            CompletableFuture.runAsync(() -> descargar(1))
        )
        .thenRun(() -> System.out.println("Todas las descargas han terminado"))
        .join();
    }

    static void descargar(int tiempo) {
        try {
            Thread.sleep(tiempo * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }}
