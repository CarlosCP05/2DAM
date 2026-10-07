package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.CompletableFuture;

public class Ejercicio11 {
    public static void main(String[] args) {
        System.out.println("Resultado: " + new Ejercicio11().dividir(10, 2).join());
        System.out.println("Resultado: " + new Ejercicio11().dividir(10, 0).join());
    }

    CompletableFuture<Integer> dividir(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
        .handle((r, e) -> {
            if (e != null) return 0;
            return r;
        });
    }
}
