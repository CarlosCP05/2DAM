package org.example.Ejercicios.Hilos.Asincronos;

import java.util.concurrent.*;

public class Ejercicio1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> tarea = () -> {
            Thread.sleep(2000);
            return 42;
        };

        Future<Integer> futuro = executor.submit(tarea);
        System.out.println("Tarea enviada");
        System.out.println("Esperando resultado");
        System.out.println(futuro.get());

        executor.shutdown();
    }
}
