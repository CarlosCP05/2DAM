package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio7 {
    public static void main(String[] args) throws InterruptedException {
        obtenerUsuario().thenCompose(u -> {
            try {
                return obtenerEmail(u);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(u -> System.out.println("Email: " + u)).join();
    }

    static CompletableFuture<String> obtenerUsuario() throws InterruptedException {
        Thread.sleep(1000);
        return  CompletableFuture.supplyAsync(() -> "Oscar");
    }

    static CompletableFuture<String> obtenerEmail(String usuario) throws InterruptedException {
        Thread.sleep(1000);
        return  CompletableFuture.supplyAsync(() -> usuario + "@example.com");
    }
}
