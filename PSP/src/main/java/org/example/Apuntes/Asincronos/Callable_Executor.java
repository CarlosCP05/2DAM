package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;

public class Callable_Executor {

    public static void main(String[] args) throws Exception {

        ejemploRunnable();
        ejemploCallable();
        ejemploExecutorService();
    }

    // ==========================================
    // EJEMPLO 1: RUNNABLE
    // ==========================================

    public static void ejemploRunnable() throws Exception {

        System.out.println("\n===== EJEMPLO RUNNABLE =====");

        // Runnable ejecuta una tarea que no devuelve resultado.
        Runnable tarea = () -> {

            System.out.println(
                    "Ejecutando Runnable en "
                            + Thread.currentThread().getName()
            );
        };

        Thread hilo = new Thread(tarea);

        hilo.start();

        // Esperamos a que termine el hilo.
        hilo.join();
    }

    // ==========================================
    // EJEMPLO 2: CALLABLE
    // ==========================================

    public static void ejemploCallable() throws Exception {

        System.out.println("\n===== EJEMPLO CALLABLE =====");

        // Callable permite devolver un resultado.
        Callable<Integer> tarea = () -> {

            System.out.println("Calculando...");

            Thread.sleep(1500);

            return 10 + 20;
        };

        // Creamos un executor con 2 hilos.
        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        // Enviamos la tarea al executor.
        Future<Integer> future = executor.submit(tarea);

        System.out.println("Tarea enviada.");

        // get() espera hasta obtener el resultado.
        Integer resultado = future.get();

        System.out.println("Resultado: " + resultado);

        executor.shutdown();
    }

    // ==========================================
    // EJEMPLO 3: EXECUTORSERVICE
    // ==========================================

    public static void ejemploExecutorService() throws Exception {

        System.out.println("\n===== EJEMPLO EXECUTOR =====");

        // Creamos un pool de 3 hilos.
        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        // Enviamos 5 tareas.
        for (int i = 1; i <= 5; i++) {

            int numero = i;

            executor.submit(() -> {

                System.out.println(
                        "Tarea " + numero
                                + " ejecutada por "
                                + Thread.currentThread().getName()
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // No aceptamos más tareas.
        executor.shutdown();

        // Esperamos a que terminen.
        boolean terminado = executor.awaitTermination(
                30, TimeUnit.SECONDS
        );

        if (terminado) {
            System.out.println("Todas las tareas terminadas.");
        } else {
            executor.shutdownNow();
            System.out.println("Tiempo agotado.");
        }
    }
}
