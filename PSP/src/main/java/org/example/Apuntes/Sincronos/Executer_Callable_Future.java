package org.example.Apuntes.Sincronos;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Executer_Callable_Future {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: FIXED THREAD POOL
        // ==========================================

        // Creamos un pool con 4 hilos.
        // Aunque enviemos 20 tareas, solo habrá 4
        // ejecutándose simultáneamente como máximo.
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 20; i++) {

            final int pedido = i;

            // submit() envía una tarea al pool.
            pool.submit(() -> {

                System.out.println(
                        "Procesando pedido " + pedido
                                + " con " + Thread.currentThread().getName()
                );

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }
            });
        }

        // shutdown() impide aceptar nuevas tareas.
        // Las tareas enviadas anteriormente siguen ejecutándose.
        pool.shutdown();

        // awaitTermination() espera a que termine el pool.
        pool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Todos los pedidos procesados");


        // ==========================================
        // EJEMPLO 2: CALLABLE Y FUTURE
        // ==========================================

        ExecutorService pool2 = Executors.newFixedThreadPool(3);

        // Guardamos los resultados futuros de las tareas.
        List<Future<Integer>> resultados = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            final int numero = i;

            // Callable permite que una tarea devuelva un resultado.
            Callable<Integer> tarea = () -> {

                int factorial = 1;

                for (int j = 1; j <= numero; j++) {
                    factorial *= j;
                }

                return factorial;
            };

            // submit() devuelve un Future que representa
            // el resultado pendiente de la tarea.
            Future<Integer> futuro = pool2.submit(tarea);

            resultados.add(futuro);
        }

        // get() espera al resultado de cada tarea si todavía
        // no ha terminado.
        for (int i = 0; i < resultados.size(); i++) {

            try {

                System.out.println(
                        "Factorial de " + (i + 1)
                                + " = " + resultados.get(i).get()
                );

            } catch (ExecutionException e) {

                System.out.println("Error en la tarea");
            }
        }

        pool2.shutdown();
        pool2.awaitTermination(1, TimeUnit.MINUTES);


        // ==========================================
        // EJEMPLO 3: DIFERENTES TIPOS DE POOL
        // ==========================================

        // FixedThreadPool:
        // Mantiene un número fijo de hilos.
        ExecutorService poolFijo =
                Executors.newFixedThreadPool(4);

        // CachedThreadPool:
        // Puede crear nuevos hilos según las tareas recibidas
        // y reutilizar hilos disponibles.
        ExecutorService poolCache =
                Executors.newCachedThreadPool();

        // SingleThreadExecutor:
        // Ejecuta las tareas mediante un único hilo.
        ExecutorService poolUnico =
                Executors.newSingleThreadExecutor();

        // Enviamos una tarea a cada pool.
        poolFijo.submit(() -> System.out.println("Pool fijo"));
        poolCache.submit(() -> System.out.println("Pool cache"));
        poolUnico.submit(() -> System.out.println("Pool único"));

        // Cerramos los tres pools.
        poolFijo.shutdown();
        poolCache.shutdown();
        poolUnico.shutdown();

        poolFijo.awaitTermination(1, TimeUnit.MINUTES);
        poolCache.awaitTermination(1, TimeUnit.MINUTES);
        poolUnico.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Programa finalizado");
    }
}
