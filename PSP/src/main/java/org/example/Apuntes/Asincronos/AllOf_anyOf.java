
package org.example.Apuntes.Asincronos;

import java.util.List;
import java.util.concurrent.*;

public class AllOf_anyOf {

    public static void main(String[] args) {

        ejemploAllOf();

        ejemploAllOfResultados();

        ejemploAnyOf();
    }

    // ==========================================
    // EJEMPLO 1: ALLOF()
    // ==========================================

    public static void ejemploAllOf() {

        System.out.println("\n===== ALLOF =====");

        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> tarea(1, 2000));

        CompletableFuture<Void> f2 =
                CompletableFuture.runAsync(() -> tarea(2, 1000));

        CompletableFuture<Void> f3 =
                CompletableFuture.runAsync(() -> tarea(3, 3000));

        System.out.println("Se han lanzado las tres tareas.");

        // allOf() crea un Future que termina
        // cuando terminan todos los Futures.
        CompletableFuture<Void> todas =
                CompletableFuture.allOf(f1, f2, f3);

        todas.join();

        System.out.println("Todas las tareas han terminado.");
    }

    // ==========================================
    // EJEMPLO 2: ALLOF CON RESULTADOS
    // ==========================================

    public static void ejemploAllOfResultados() {

        System.out.println("\n===== ALLOF CON RESULTADOS =====");

        // Lanzamos tres tareas que devuelven números.
        CompletableFuture<Integer> f1 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return 10;
                });

        CompletableFuture<Integer> f2 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(1000);

                    return 20;
                });

        CompletableFuture<Integer> f3 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(3000);

                    return 30;
                });

        // Esperamos a que terminen todas.
        CompletableFuture.allOf(f1, f2, f3).join();

        // allOf() no devuelve automáticamente una lista.
        // Tenemos que recoger los resultados individualmente.
        List<Integer> resultados = List.of(
                f1.join(),
                f2.join(),
                f3.join()
        );

        System.out.println("Resultados: " + resultados);

        int suma = resultados.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Suma: " + suma);
    }

    // ==========================================
    // EJEMPLO 3: ANYOF()
    // ==========================================

    public static void ejemploAnyOf() {

        System.out.println("\n===== ANYOF =====");

        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(3000);

                    return "Respuesta del servidor 1";
                });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(1000);

                    return "Respuesta del servidor 2";
                });

        CompletableFuture<String> f3 =
                CompletableFuture.supplyAsync(() -> {

                    dormir(2000);

                    return "Respuesta del servidor 3";
                });

        // anyOf() termina cuando cualquiera termina.
        CompletableFuture<Object> primero =
                CompletableFuture.anyOf(f1, f2, f3);

        System.out.println(
                "Primera respuesta: " + primero.join()
        );
    }

    // ==========================================
    // METODO AUXILIAR: SIMULAR TAREA
    // ==========================================

    public static void tarea(int numero, long tiempo) {

        System.out.println("Iniciando tarea " + numero);

        dormir(tiempo);

        System.out.println("Terminando tarea " + numero);
    }

    public static void dormir(long tiempo) {

        try {

            Thread.sleep(tiempo);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
