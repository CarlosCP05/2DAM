
package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;

public class Composicion {

    public static void main(String[] args) {

        ejemploThenCompose();

        ejemploThenApplyVsThenCompose();

        ejemploThenCombine();
    }

    // ==========================================
    // EJEMPLO 1: THENCOMPOSE()
    // ==========================================

    public static void ejemploThenCompose() {

        System.out.println("\n===== THENCOMPOSE =====");

        // Primera tarea: obtener un usuario.
        CompletableFuture<String> usuario =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println("Obteniendo usuario...");

                    dormir(1000);

                    return "Carlos";
                });

        // Segunda tarea: obtener los pedidos del usuario.
        // Esta operación también devuelve un CompletableFuture.
        CompletableFuture<String> pedidos =
                usuario.thenCompose(nombre -> {

                    return CompletableFuture.supplyAsync(() -> {

                        System.out.println(
                                "Buscando pedidos de " + nombre
                        );

                        dormir(1500);

                        return "Pedido 1, Pedido 2, Pedido 3";
                    });
                });

        System.out.println("Resultado: " + pedidos.join());
    }

    // ==========================================
    // EJEMPLO 2: THENAPPLY VS THENCOMPOSE
    // ==========================================

    public static void ejemploThenApplyVsThenCompose() {

        System.out.println("\n===== THENAPPLY VS THENCOMPOSE =====");

        CompletableFuture<String> usuario =
                CompletableFuture.supplyAsync(() -> "Carlos");

        // --------------------------------------
        // THENAPPLY
        // --------------------------------------

        // La función devuelve un String.
        CompletableFuture<Integer> longitud =
                usuario.thenApply(nombre -> nombre.length());

        System.out.println(
                "Longitud del nombre: " + longitud.join()
        );

        // --------------------------------------
        // THENAPPLY CON FUTURE ANIDADO
        // --------------------------------------

        // Esta función devuelve otro CompletableFuture.
        CompletableFuture<CompletableFuture<String>> anidado =
                usuario.thenApply(nombre -> {

                    return CompletableFuture.supplyAsync(() -> {

                        return "Pedidos de " + nombre;
                    });
                });

        // Tenemos que obtener primero el Future interior.
        System.out.println(
                "Resultado anidado: " + anidado.join().join()
        );

        // --------------------------------------
        // THENCOMPOSE
        // --------------------------------------

        // thenCompose evita el Future anidado.
        CompletableFuture<String> compuesto =
                usuario.thenCompose(nombre -> {

                    return CompletableFuture.supplyAsync(() -> {

                        return "Pedidos de " + nombre;
                    });
                });

        System.out.println(
                "Resultado compuesto: " + compuesto.join()
        );
    }

    // ==========================================
    // EJEMPLO 3: THENCOMBINE()
    // ==========================================

    public static void ejemploThenCombine() {

        System.out.println("\n===== THENCOMBINE =====");

        // TAREA 1: consultar los pedidos.
        CompletableFuture<Integer> pedidos =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println("Consultando pedidos...");

                    dormir(2000);

                    return 5;
                });

        // TAREA 2: consultar el saldo.
        CompletableFuture<Double> saldo =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println("Consultando saldo...");

                    dormir(1000);

                    return 250.50;
                });

        // Ambas tareas son independientes.
        // thenCombine espera sus resultados y los combina.
        CompletableFuture<String> resultado =
                pedidos.thenCombine(
                        saldo,
                        (numeroPedidos, saldoActual) -> {

                            return "Pedidos: " + numeroPedidos
                                    + " | Saldo: " + saldoActual;
                        }
                );

        System.out.println(resultado.join());
    }

    // ==========================================
    // METODO AUXILIAR
    // ==========================================

    public static void dormir(long milisegundos) {

        try {

            Thread.sleep(milisegundos);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
