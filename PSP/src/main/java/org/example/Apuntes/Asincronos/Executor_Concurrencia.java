
package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Executor_Concurrencia {

    // ==========================================
    // CONTADOR COMPARTIDO
    // ==========================================

    // AtomicInteger permite incrementar el contador
    // de forma segura desde varios hilos.
    private static final AtomicInteger contador =
            new AtomicInteger(0);

    public static void main(String[] args)
            throws InterruptedException {

        ejemploExecutorPersonalizado();

        ejemploCPUyIO();

        ejemploAtomicInteger();

        ejemploEvitarBloqueos();
    }

    // ==========================================
    // EJEMPLO 1: EXECUTOR PERSONALIZADO
    // ==========================================

    public static void ejemploExecutorPersonalizado()
            throws InterruptedException {

        System.out.println("\n===== EXECUTOR PERSONALIZADO =====");

        // Creamos un pool con 4 hilos.
        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        // Lanzamos 8 tareas.
        for (int i = 1; i <= 8; i++) {

            int numero = i;

            executor.submit(() -> {

                System.out.println(
                        "Tarea " + numero
                                + " ejecutada por "
                                + Thread.currentThread().getName()
                );

                dormir(1000);
            });
        }

        executor.shutdown();

        // Esperamos a que termine el pool.
        boolean terminado = executor.awaitTermination(
                30, TimeUnit.SECONDS
        );

        if (!terminado) {

            executor.shutdownNow();

            System.out.println("Se ha agotado el tiempo.");
        }
    }

    // ==========================================
    // EJEMPLO 2: SEPARAR CPU E IO
    // ==========================================

    public static void ejemploCPUyIO() {

        System.out.println("\n===== EXECUTORS CPU E IO =====");

        // Executor para operaciones de entrada y salida.
        // Puede tener más hilos porque muchas tareas
        // pasan tiempo esperando respuestas externas.
        ExecutorService executorIO =
                Executors.newFixedThreadPool(10);

        // Executor para operaciones de CPU.
        // Utilizamos el número de procesadores disponibles.
        int nucleos = Runtime.getRuntime()
                .availableProcessors();

        ExecutorService executorCPU =
                Executors.newFixedThreadPool(nucleos);

        // --------------------------------------
        // TAREA IO
        // --------------------------------------

        CompletableFuture<String> consulta =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println(
                            "Consulta IO en "
                                    + Thread.currentThread().getName()
                    );

                    dormir(2000);

                    return "Datos descargados";
                }, executorIO);

        // --------------------------------------
        // TAREA CPU
        // --------------------------------------

        CompletableFuture<Long> calculo =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println(
                            "Cálculo CPU en "
                                    + Thread.currentThread().getName()
                    );

                    long suma = 0;

                    for (int i = 0; i < 1_000_000; i++) {

                        suma += i;
                    }

                    return suma;
                }, executorCPU);

        // Combinamos los resultados.
        CompletableFuture<String> resultado =
                consulta.thenCombine(
                        calculo,
                        (datos, suma) -> {

                            return datos + " | Suma: " + suma;
                        }
                );

        System.out.println(resultado.join());

        // Cerramos ambos executors.
        executorIO.shutdown();
        executorCPU.shutdown();
    }

    // ==========================================
    // EJEMPLO 3: ATOMICINTEGER
    // ==========================================

    public static void ejemploAtomicInteger()
            throws InterruptedException {

        System.out.println("\n===== ATOMIC INTEGER =====");

        contador.set(0);

        ExecutorService executor =
                Executors.newFixedThreadPool(5);

        // Lanzamos 100 tareas.
        for (int i = 0; i < 100; i++) {

            executor.submit(() -> {

                // Incremento atómico y seguro.
                contador.incrementAndGet();
            });
        }

        executor.shutdown();

        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(
                "Contador final: " + contador.get()
        );

        System.out.println("Resultado esperado: 100");
    }

    // ==========================================
    // EJEMPLO 4: EVITAR BLOQUEOS INNECESARIOS
    // ==========================================

    public static void ejemploEvitarBloqueos() {

        System.out.println("\n===== EVITAR BLOQUEOS =====");

        // --------------------------------------
        // FORMA POCO RECOMENDABLE
        // --------------------------------------

        // Lanzamos la primera tarea y esperamos inmediatamente.
        CompletableFuture<Integer> f1 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return 10;
                });

        int resultado1 = f1.join();

        // Solo después de esperar a f1 lanzamos f2.
        CompletableFuture<Integer> f2 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return 20;
                });

        int resultado2 = f2.join();

        System.out.println(
                "Resultado secuencial: "
                        + (resultado1 + resultado2)
        );

        // --------------------------------------
        // FORMA RECOMENDABLE
        // --------------------------------------

        // Lanzamos ambas tareas sin esperar entre ellas.
        CompletableFuture<Integer> a =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return 10;
                });

        CompletableFuture<Integer> b =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return 20;
                });

        // Esperamos a las dos conjuntamente.
        CompletableFuture.allOf(a, b).join();

        System.out.println(
                "Resultado concurrente: "
                        + (a.join() + b.join())
        );
    }

    // ==========================================
    // METODO AUXILIAR
    // ==========================================

    public static void dormir(long tiempo) {

        try {

            Thread.sleep(tiempo);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
