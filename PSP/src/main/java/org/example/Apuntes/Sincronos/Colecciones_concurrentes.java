package org.example.Apuntes.Sincronos;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Colecciones_concurrentes {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: BLOCKINGQUEUE
        // ==========================================

        // ArrayBlockingQueue crea una cola de capacidad limitada.
        // En este caso solo puede almacenar 5 elementos.
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

        // PRODUCTOR
        Thread productor = new Thread(() -> {

            try {

                for (int i = 1; i <= 10; i++) {

                    // put() introduce un elemento.
                    // Si la cola está llena, el hilo espera.
                    cola.put(i);

                    System.out.println("Producido: " + i);

                    Thread.sleep(300);
                }

                // Valor especial para indicar que hemos terminado.
                cola.put(-1);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });


        // CONSUMIDOR
        Thread consumidor = new Thread(() -> {

            try {

                while (true) {

                    // take() recoge un elemento.
                    // Si la cola está vacía, el hilo espera.
                    int numero = cola.take();

                    if (numero == -1) {
                        break;
                    }

                    System.out.println("Consumido: " + numero);

                    Thread.sleep(700);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        productor.start();
        consumidor.start();

        productor.join();
        consumidor.join();


        // ==========================================
        // EJEMPLO 2: CONCURRENTHASHMAP
        // ==========================================

        // ConcurrentHashMap es un mapa diseñado para
        // permitir accesos concurrentes de varios hilos.
        ConcurrentHashMap<String, Integer> inventario =
                new ConcurrentHashMap<>();

        // Creamos cinco hilos que modifican el mismo mapa.
        Thread[] trabajadores = new Thread[5];

        for (int i = 0; i < 5; i++) {

            final int numero = i;

            trabajadores[i] = new Thread(() -> {

                for (int j = 0; j < 100; j++) {

                    // put() introduce o actualiza un valor.
                    inventario.put(
                            "Producto " + numero + "-" + j,
                            j
                    );

                    // get() consulta un valor.
                    Integer valor = inventario.get(
                            "Producto " + numero + "-" + j
                    );
                }

                System.out.println(
                        "Hilo " + numero + " ha terminado"
                );
            });

            trabajadores[i].start();
        }

        for (Thread hilo : trabajadores) {
            hilo.join();
        }

        System.out.println(
                "Elementos del inventario: " + inventario.size()
        );


        // ==========================================
        // EJEMPLO 3: OPERACIONES ATOMICAS DEL MAPA
        // ==========================================

        // putIfAbsent() introduce el valor solo si la clave
        // todavía no existe.
        inventario.putIfAbsent("Pan", 20);

        // compute() actualiza el valor de una clave.
        inventario.compute("Pan", (clave, valor) -> valor + 5);

        // merge() combina un valor nuevo con el existente.
        inventario.merge("Pan", 10, Integer::sum);

        System.out.println("Pan disponible: " + inventario.get("Pan"));

        System.out.println("Programa finalizado");
    }
}

