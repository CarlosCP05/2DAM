
package org.example.Apuntes.Asincronos;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EjemploTienda {

    // ==========================================
    // CONFIGURACION DE LA TIENDA
    // ==========================================

    // Executor personalizado con 4 hilos.
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    // Contador seguro de compras confirmadas.
    private static final AtomicInteger comprasConfirmadas =
            new AtomicInteger(0);

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       TIENDA ONLINE");
        System.out.println("================================");

        // ==========================================
        // PASO 1: COMPROBAR EL STOCK
        // ==========================================

        // Esta tarea comprueba si hay unidades disponibles.
        CompletableFuture<Boolean> stock =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println(
                            "Comprobando stock en "
                                    + Thread.currentThread().getName()
                    );

                    dormir(2000);

                    // Simulamos que hay stock disponible.
                    return true;

                }, executor);


        // ==========================================
        // PASO 2: CALCULAR EL PRECIO
        // ==========================================

        // Esta tarea calcula el precio del producto.
        CompletableFuture<Double> precio =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println(
                            "Calculando precio en "
                                    + Thread.currentThread().getName()
                    );

                    dormir(1500);

                    return 49.99;

                }, executor);


        // ==========================================
        // PASO 3: CONSULTAR EL ENVIO
        // ==========================================

        // Esta tarea consulta la disponibilidad del envío.
        CompletableFuture<String> envio =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println(
                            "Consultando envío en "
                                    + Thread.currentThread().getName()
                    );

                    dormir(3000);

                    return "Disponible";

                }, executor);


        // ==========================================
        // PASO 4: ESPERAR A TODAS LAS TAREAS
        // ==========================================

        // allOf() termina cuando finalizan las tres tareas.
        CompletableFuture<Void> todas =
                CompletableFuture.allOf(
                        stock,
                        precio,
                        envio
                );


        // ==========================================
        // PASO 5: COMBINAR LOS RESULTADOS
        // ==========================================

        CompletableFuture<String> compra =
                todas.thenApply(resultado -> {

                            // Como allOf() ya ha terminado,
                            // podemos recoger los resultados.
                            boolean hayStock = stock.join();

                            double precioFinal = precio.join();

                            String estadoEnvio = envio.join();

                            // Comprobamos si podemos realizar la compra.
                            if (!hayStock) {

                                return "COMPRA RECHAZADA: Sin stock.";
                            }

                            if (!estadoEnvio.equals("Disponible")) {

                                return "COMPRA RECHAZADA: Envío no disponible.";
                            }

                            // Incrementamos el contador de compras.
                            comprasConfirmadas.incrementAndGet();

                            return "COMPRA CONFIRMADA"
                                    + " | Precio: " + precioFinal
                                    + " | Envío: " + estadoEnvio;
                        })

                        // ==========================================
                        // PASO 6: ESTABLECER UN TIMEOUT
                        // ==========================================

                        // Si el proceso completo supera 5 segundos,
                        // se completa excepcionalmente.
                        .orTimeout(5, TimeUnit.SECONDS)

                        // ==========================================
                        // PASO 7: MANEJAR LOS ERRORES
                        // ==========================================

                        .exceptionally(error -> {

                            System.out.println(
                                    "Error durante la compra: "
                                            + error.getMessage()
                            );

                            return "COMPRA NO DISPONIBLE";
                        });


        // ==========================================
        // PASO 8: OBTENER EL RESULTADO FINAL
        // ==========================================

        // join() espera hasta obtener el resultado.
        String resultadoFinal = compra.join();

        System.out.println("\n================================");
        System.out.println("         RESULTADO");
        System.out.println("================================");

        System.out.println(resultadoFinal);


        // ==========================================
        // PASO 9: RESUMEN FINAL
        // ==========================================

        System.out.println("\n================================");
        System.out.println("       RESUMEN DE LA TIENDA");
        System.out.println("================================");

        System.out.println(
                "Compras confirmadas: "
                        + comprasConfirmadas.get()
        );


        // ==========================================
        // PASO 10: CERRAR EL EXECUTOR
        // ==========================================

        executor.shutdown();

        System.out.println("\nExecutor cerrado.");
        System.out.println("Programa finalizado.");
    }


    // ==========================================
    // METODO AUXILIAR
    // ==========================================

    public static void dormir(long tiempo) {

        try {

            Thread.sleep(tiempo);

        } catch (InterruptedException e) {

            // Recuperamos el estado de interrupción.
            Thread.currentThread().interrupt();

            // Propagamos el error para que
            // CompletableFuture pueda gestionarlo.
            throw new CompletionException(e);
        }
    }
}
