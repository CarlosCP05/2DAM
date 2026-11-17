package org.example.Apuntes.Asincronos;

public class Completable_Future {

    public static void main(String[] args) {

        ejemploRunAsync();
        ejemploSupplyAsync();
        ejemploThenApply();
        ejemploThenAccept();
        ejemploThenRun();
        ejemploEncadenamiento();
    }

    // ==========================================
    // EJEMPLO 1: RUNASYNC()
    // ==========================================

    public static void ejemploRunAsync() {

        System.out.println("\n===== RUNASYNC =====");

        // runAsync() ejecuta una tarea sin devolver resultado.
        java.util.concurrent.CompletableFuture<Void> future =
                java.util.concurrent.CompletableFuture.runAsync(() -> {

                    System.out.println("Ejecutando tarea...");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    System.out.println("Tarea terminada.");
                });

        System.out.println("La tarea se ha lanzado.");

        // join() espera a que termine.
        future.join();

        System.out.println("Programa continúa.");
    }

    // ==========================================
    // EJEMPLO 2: SUPPLYASYNC()
    // ==========================================

    public static void ejemploSupplyAsync() {

        System.out.println("\n===== SUPPLYASYNC =====");

        // supplyAsync() permite devolver un resultado.
        java.util.concurrent.CompletableFuture<Integer> future =
                java.util.concurrent.CompletableFuture.supplyAsync(() -> {

                    System.out.println("Calculando...");

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    return 10 + 20;
                });

        System.out.println("Tarea enviada.");

        // join() obtiene el resultado.
        Integer resultado = future.join();

        System.out.println("Resultado: " + resultado);
    }

    // ==========================================
    // EJEMPLO 3: THENAPPLY()
    // ==========================================

    public static void ejemploThenApply() {

        System.out.println("\n===== THENAPPLY =====");

        java.util.concurrent.CompletableFuture<Integer> future =
                java.util.concurrent.CompletableFuture
                        .supplyAsync(() -> 10)

                        // Recibe el resultado y lo transforma.
                        .thenApply(numero -> numero * 2)

                        // Recibe 20 y devuelve 25.
                        .thenApply(numero -> numero + 5);

        System.out.println("Resultado: " + future.join());
    }

    // ==========================================
    // EJEMPLO 4: THENACCEPT()
    // ==========================================

    public static void ejemploThenAccept() {

        System.out.println("\n===== THENACCEPT =====");

        java.util.concurrent.CompletableFuture<Void> future =
                java.util.concurrent.CompletableFuture
                        .supplyAsync(() -> 100)

                        // Recibe el resultado.
                        // No devuelve un nuevo resultado.
                        .thenAccept(resultado -> {

                            System.out.println(
                                    "Resultado recibido: " + resultado
                            );
                        });

        future.join();
    }

    // ==========================================
    // EJEMPLO 5: THENRUN()
    // ==========================================

    public static void ejemploThenRun() {

        System.out.println("\n===== THENRUN =====");

        java.util.concurrent.CompletableFuture<Void> future =
                java.util.concurrent.CompletableFuture
                        .supplyAsync(() -> {

                            System.out.println("Calculando...");

                            return 50;
                        })

                        // No recibe el resultado 50.
                        // Simplemente ejecuta una acción posterior.
                        .thenRun(() -> {

                            System.out.println(
                                    "La operación ha terminado."
                            );
                        });

        future.join();
    }

    // ==========================================
    // EJEMPLO 6: ENCADENAMIENTO
    // ==========================================

    public static void ejemploEncadenamiento() {

        System.out.println("\n===== ENCADENAMIENTO =====");

        java.util.concurrent.CompletableFuture<Void> future =
                java.util.concurrent.CompletableFuture

                        // PASO 1: Obtener un número.
                        .supplyAsync(() -> {

                            System.out.println("Obteniendo número...");

                            return 10;
                        })

                        // PASO 2: Multiplicar por 2.
                        .thenApply(numero -> {

                            System.out.println("Multiplicando...");

                            return numero * 2;
                        })

                        // PASO 3: Sumar 5.
                        .thenApply(numero -> {

                            System.out.println("Sumando...");

                            return numero + 5;
                        })

                        // PASO 4: Mostrar el resultado.
                        .thenAccept(resultado -> {

                            System.out.println(
                                    "Resultado final: " + resultado
                            );
                        })

                        // PASO 5: Acción final.
                        .thenRun(() -> {

                            System.out.println("Proceso terminado.");
                        });

        future.join();
    }
}

