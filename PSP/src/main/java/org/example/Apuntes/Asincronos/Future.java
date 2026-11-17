package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;

public class Future {

    public static void main(String[] args) throws Exception {

        ejemploGet();
        ejemploTimeout();
        ejemploIsDone();
        ejemploCancelacion();
        ejemploVariasTareas();
    }

    // ==========================================
    // EJEMPLO 1: GET()
    // ==========================================

    public static void ejemploGet() throws Exception {

        System.out.println("\n===== GET =====");

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        java.util.concurrent.Future<Integer> future = executor.submit(() -> {

            Thread.sleep(2000);

            return 100;
        });

        System.out.println("Tarea enviada.");

        // get() bloquea hasta que llega el resultado.
        Integer resultado = future.get();

        System.out.println("Resultado: " + resultado);

        executor.shutdown();
    }

    // ==========================================
    // EJEMPLO 2: GET CON TIMEOUT
    // ==========================================

    public static void ejemploTimeout() throws Exception {

        System.out.println("\n===== GET TIMEOUT =====");

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        java.util.concurrent.Future<Integer> future = executor.submit(() -> {

            Thread.sleep(4000);

            return 50;
        });

        try {

            // Esperamos como máximo 2 segundos.
            Integer resultado = future.get(
                    2, TimeUnit.SECONDS
            );

            System.out.println("Resultado: " + resultado);

        } catch (TimeoutException e) {

            System.out.println("Se ha agotado el tiempo de espera.");
        }

        // Esperamos a que la tarea termine.
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    // ==========================================
    // EJEMPLO 3: ISDONE()
    // ==========================================

    public static void ejemploIsDone() throws Exception {

        System.out.println("\n===== ISDONE =====");

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        java.util.concurrent.Future<String> future = executor.submit(() -> {

            Thread.sleep(2000);

            return "Proceso terminado";
        });

        // Consultamos sin bloquear.
        while (!future.isDone()) {

            System.out.println("La tarea sigue ejecutándose.");

            Thread.sleep(500);
        }

        System.out.println("La tarea ha terminado.");

        System.out.println(future.get());

        executor.shutdown();
    }

    // ==========================================
    // EJEMPLO 4: CANCELACION
    // ==========================================

    public static void ejemploCancelacion() throws Exception {

        System.out.println("\n===== CANCELACION =====");

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        java.util.concurrent.Future<?> future = executor.submit(() -> {

            try {

                for (int i = 1; i <= 10; i++) {

                    System.out.println("Trabajando: " + i);

                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {

                System.out.println("Tarea interrumpida.");

                Thread.currentThread().interrupt();
            }
        });

        Thread.sleep(2500);

        // Intentamos cancelar permitiendo interrupción.
        boolean cancelado = future.cancel(true);

        System.out.println("Cancelado: " + cancelado);

        System.out.println(
                "¿Está cancelado? " + future.isCancelled()
        );

        executor.shutdown();

        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    // ==========================================
    // EJEMPLO 5: VARIAS TAREAS CON FUTURE
    // ==========================================

    public static void ejemploVariasTareas() throws Exception {

        System.out.println("\n===== VARIAS TAREAS =====");

        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        java.util.concurrent.Future<Integer> f1 = executor.submit(() -> {

            Thread.sleep(2000);

            return 10;
        });

        java.util.concurrent.Future<Integer> f2 = executor.submit(() -> {

            Thread.sleep(1000);

            return 20;
        });

        java.util.concurrent.Future<Integer> f3 = executor.submit(() -> {

            Thread.sleep(3000);

            return 30;
        });

        // Las tres tareas se ejecutan concurrentemente.
        // Pero esperamos los resultados en este orden.
        int resultado1 = f1.get();

        int resultado2 = f2.get();

        int resultado3 = f3.get();

        System.out.println(
                "Suma: "
                        + (resultado1 + resultado2 + resultado3)
        );

        executor.shutdown();
    }
}
