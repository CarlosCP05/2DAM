
package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;

public class Errores_Cancelacion {

    public static void main(String[] args) {

        ejemploExceptionally();

        ejemploHandle();

        ejemploWhenComplete();

        ejemploOrTimeout();

        ejemploCompleteOnTimeout();

        ejemploCancelacion();
    }

    // ==========================================
    // EJEMPLO 1: EXCEPTIONALLY()
    // ==========================================

    public static void ejemploExceptionally() {

        System.out.println("\n===== EXCEPTIONALLY =====");

        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> {

                            System.out.println("Ejecutando tarea...");

                            // Provocamos un error.
                            if(true) throw new RuntimeException("Error al calcular");

                            return 20;
                        })

                        // Si ocurre un error, recuperamos la ejecución.
                        .exceptionally(error -> {

                            System.out.println(
                                    "Se ha producido un error: "
                                            + error.getMessage()
                            );

                            // Devolvemos un valor alternativo.
                            return 0;
                        });

        System.out.println("Resultado: " + future.join());
    }

    // ==========================================
    // EJEMPLO 2: HANDLE()
    // ==========================================

    public static void ejemploHandle() {

        System.out.println("\n===== HANDLE =====");

        CompletableFuture<Integer> future =
                CompletableFuture
                        .supplyAsync(() -> {

                            // Cambiar a 10 para probar el caso correcto.
                            int numero = 0;

                            if (numero == 0) {

                                throw new RuntimeException(
                                        "No se puede dividir entre cero"
                                );
                            }

                            return 100 / numero;
                        })

                        // handle() recibe el resultado y el error.
                        .handle((resultado, error) -> {

                            if (error != null) {

                                System.out.println(
                                        "Error detectado: "
                                                + error.getMessage()
                                );

                                return 0;
                            }

                            return resultado * 2;
                        });

        System.out.println("Resultado: " + future.join());
    }

    // ==========================================
    // EJEMPLO 3: WHENCOMPLETE()
    // ==========================================

    public static void ejemploWhenComplete() {

        System.out.println("\n===== WHENCOMPLETE =====");

        CompletableFuture<Integer> future =
                CompletableFuture
                        .supplyAsync(() -> {

                            return 50;
                        })

                        // Se ejecuta cuando termina la tarea.
                        // Recibe resultado y error.
                        .whenComplete((resultado, error) -> {

                            if (error != null) {

                                System.out.println(
                                        "La tarea ha fallado."
                                );

                            } else {

                                System.out.println(
                                        "La tarea ha terminado."
                                );

                                System.out.println(
                                        "Resultado: " + resultado
                                );
                            }
                        });

        System.out.println("Resultado final: " + future.join());
    }

    // ==========================================
    // EJEMPLO 4: ORTIMEOUT()
    // ==========================================

    public static void ejemploOrTimeout() {

        System.out.println("\n===== ORTIMEOUT =====");

        CompletableFuture<Integer> future =
                CompletableFuture
                        .supplyAsync(() -> {

                            System.out.println("Tarea iniciada.");

                            dormir(5000);

                            return 100;
                        })

                        // Si no termina en 2 segundos,
                        // el CompletableFuture termina excepcionalmente.
                        .orTimeout(2, TimeUnit.SECONDS)

                        .exceptionally(error -> {

                            System.out.println(
                                    "Tiempo de espera agotado o error: "
                                            + error.getMessage()
                            );

                            return -1;
                        });

        System.out.println("Resultado: " + future.join());
    }

    // ==========================================
    // EJEMPLO 5: COMPLETEONTIMEOUT()
    // ==========================================

    public static void ejemploCompleteOnTimeout() {

        System.out.println("\n===== COMPLETEONTIMEOUT =====");

        CompletableFuture<Integer> future =
                CompletableFuture
                        .supplyAsync(() -> {

                            System.out.println("Calculando...");

                            dormir(5000);

                            return 100;
                        })

                        // Si pasan 2 segundos sin terminar,
                        // completamos el Future con el valor 0.
                        .completeOnTimeout(
                                0,
                                2,
                                TimeUnit.SECONDS
                        );

        System.out.println(
                "Resultado obtenido: " + future.join()
        );
    }

    // ==========================================
    // EJEMPLO 6: CANCELACION
    // ==========================================

    public static void ejemploCancelacion() {

        System.out.println("\n===== CANCELACION =====");

        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {

                    for (int i = 1; i <= 10; i++) {

                        System.out.println(
                                "Procesando elemento " + i
                        );

                        dormir(1000);
                    }
                });

        // Esperamos un poco antes de cancelar.
        dormir(2500);

        // Intentamos cancelar la tarea.
        // No garantiza que el código en ejecución se detenga.
        boolean cancelado = future.cancel(true);

        System.out.println("Cancelación solicitada: " + cancelado);

        System.out.println(
                "¿Cancelado? " + future.isCancelled()
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
