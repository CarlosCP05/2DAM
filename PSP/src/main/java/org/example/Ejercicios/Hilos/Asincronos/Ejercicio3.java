package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.*;

public class Ejercicio3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long inicio = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<Integer> tarea1 = () -> {
            Thread.sleep(2000);
            return 10;
        };

        Callable<Integer> tarea2 = () -> {
            Thread.sleep(1000);
            return 20;
        };

        Callable<Integer> tarea3 = () -> {
            Thread.sleep(3000);
            return 30;
        };

        Future<Integer> resultado1 = executor.submit(tarea1);
        Future<Integer> resultado2 = executor.submit(tarea2);
        Future<Integer> resultado3 = executor.submit(tarea3);

        System.out.println("Inciando tareas...");
        System.out.println(resultado1.get() + resultado2.get() + resultado3.get());
        long fin = System.currentTimeMillis();

        System.out.println("Duración: " + (fin - inicio));
    }
}
