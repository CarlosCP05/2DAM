package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ejercicio13 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() -> {
            return "Tarea 1 - hilo: " + Thread.currentThread().getName();
        }, executor).thenAccept(System.out::println);

        CompletableFuture.supplyAsync(() -> {
            return "Tarea 2 - hilo: " + Thread.currentThread().getName();
        }, executor).thenAccept(System.out::println);

        CompletableFuture.supplyAsync(() -> {
            return "Tarea 3 - hilo: " + Thread.currentThread().getName();
        }, executor).thenAccept(System.out::println);

        CompletableFuture.supplyAsync(() -> {
            return "Tarea 4 - hilo: " + Thread.currentThread().getName();
        }, executor).thenAccept(System.out::println);

        CompletableFuture.supplyAsync(() -> {
            return "Tarea 5 - hilo: " + Thread.currentThread().getName();
        }, executor).thenAccept(System.out::println);
    }
}
