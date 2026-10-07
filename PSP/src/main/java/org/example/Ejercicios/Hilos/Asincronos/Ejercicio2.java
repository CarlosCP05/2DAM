package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ejercicio2 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> futuro = executor.submit(() -> {
            System.out.println("Ejecutando " + Thread.currentThread().getName());
            Thread.sleep(5000);
            return 1;
        });

        Thread.sleep(1);
        while (!futuro.isDone()) {
            System.out.println("Esperando... ");
            Thread.sleep(1000);
        }

        System.out.println("FIN");
    }
}
