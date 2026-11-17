package org.example.Apuntes.Sincronos;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EjemploExplicado {

    // ==========================================
    // CONFIGURACION DE LA PIZZERIA
    // ==========================================

    // Cola compartida donde se guardan los pedidos.
    // Tiene capacidad para 5 pedidos.
    private static final BlockingQueue<Pedido> pedidos =
            new ArrayBlockingQueue<>(5);

    // Solo existen 2 hornos.
    // Como máximo podrán cocinar 2 cocineros simultáneamente.
    private static final Semaphore hornos = new Semaphore(2);

    // Contador compartido y seguro para contar pedidos terminados.
    private static final AtomicInteger completados =
            new AtomicInteger(0);

    // Número de cocineros.
    private static final int NUM_COCINEROS = 3;

    // Número de clientes.
    private static final int NUM_CLIENTES = 4;

    // Pedidos que realizará cada cliente.
    private static final int PEDIDOS_POR_CLIENTE = 3;


    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // PASO 1: CREAR EL POOL DE COCINEROS
        // ==========================================

        // Creamos un pool de 3 hilos para los cocineros.
        ExecutorService cocineros =
                Executors.newFixedThreadPool(NUM_COCINEROS);

        // Enviamos las tareas de los cocineros al pool.
        for (int i = 1; i <= NUM_COCINEROS; i++) {

            cocineros.submit(new Cocinero(i));
        }


        // ==========================================
        // PASO 2: CREAR LOS CLIENTES
        // ==========================================

        Thread[] clientes = new Thread[NUM_CLIENTES];

        for (int i = 0; i < NUM_CLIENTES; i++) {

            // Cada cliente genera sus propios pedidos.
            clientes[i] = new Thread(
                    new Cliente(i + 1),
                    "Cliente-" + (i + 1)
            );

            clientes[i].start();
        }


        // ==========================================
        // PASO 3: ESPERAR A LOS CLIENTES
        // ==========================================

        // join() espera a que todos los clientes terminen
        // de introducir sus pedidos en la cola.
        for (Thread cliente : clientes) {

            cliente.join();
        }

        System.out.println("\nTodos los clientes han terminado.");


        // ==========================================
        // PASO 4: CERRAR LOS COCINEROS
        // ==========================================

        // Introducimos un pedido especial de finalización
        // por cada cocinero.
        // Los cocineros terminarán cuando reciban este pedido.
        for (int i = 0; i < NUM_COCINEROS; i++) {

            pedidos.put(Pedido.finalizar());
        }

        // Indicamos que no se enviarán más tareas al pool.
        cocineros.shutdown();

        // Esperamos a que todos los cocineros terminen.
        boolean terminado = cocineros.awaitTermination(
                1, TimeUnit.MINUTES
        );

        // ==========================================
        // PASO 5: RESUMEN FINAL
        // ==========================================

        if (terminado) {

            System.out.println("\n================================");
            System.out.println("RESUMEN FINAL DE LA PIZZERIA");
            System.out.println("================================");

            System.out.println(
                    "Pedidos completados: " + completados.get()
            );

            System.out.println(
                    "Pedidos esperados: "
                            + (NUM_CLIENTES * PEDIDOS_POR_CLIENTE)
            );

            System.out.println("Todos los cocineros han terminado.");
            System.out.println("Pizzería cerrada correctamente.");

        } else {

            // Si no terminan a tiempo, solicitamos la interrupción.
            cocineros.shutdownNow();

            System.out.println("Tiempo de espera agotado.");
        }
    }


    // ==========================================
    // CLASE PEDIDO
    // ==========================================

    static class Pedido {

        private final int numero;
        private final int cliente;
        private final boolean finalizar;

        public Pedido(int numero, int cliente) {

            this.numero = numero;
            this.cliente = cliente;
            this.finalizar = false;
        }

        // Constructor para crear pedidos especiales de finalización.
        private Pedido() {

            this.numero = -1;
            this.cliente = -1;
            this.finalizar = true;
        }

        public static Pedido finalizar() {

            return new Pedido();
        }

        public boolean esFinalizacion() {

            return finalizar;
        }

        public int getNumero() {

            return numero;
        }

        public int getCliente() {

            return cliente;
        }
    }


    // ==========================================
    // CLASE CLIENTE
    // ==========================================

    static class Cliente implements Runnable {

        private final int numero;

        public Cliente(int numero) {

            this.numero = numero;
        }

        @Override
        public void run() {

            try {

                for (int i = 1; i <= PEDIDOS_POR_CLIENTE; i++) {

                    Pedido pedido = new Pedido(i, numero);

                    System.out.println(
                            "Cliente " + numero
                                    + " realiza pedido " + i
                    );

                    // put() introduce el pedido en la cola.
                    // Si está llena, el cliente espera.
                    pedidos.put(pedido);

                    // Simulamos el tiempo entre pedidos.
                    Thread.sleep(300);
                }

                System.out.println(
                        "Cliente " + numero + " ha terminado."
                );

            } catch (InterruptedException e) {

                // Si el cliente es interrumpido, recuperamos
                // el estado de interrupción.
                Thread.currentThread().interrupt();
            }
        }
    }


    // ==========================================
    // CLASE COCINERO
    // ==========================================

    static class Cocinero implements Runnable {

        private final int numero;

        public Cocinero(int numero) {

            this.numero = numero;
        }

        @Override
        public void run() {

            try {

                while (true) {

                    // take() recoge el siguiente pedido.
                    // Si no hay pedidos, el cocinero espera.
                    Pedido pedido = pedidos.take();

                    // Comprobamos si es un pedido de finalización.
                    if (pedido.esFinalizacion()) {

                        System.out.println(
                                "Cocinero " + numero
                                        + " termina su trabajo."
                        );

                        break;
                    }

                    boolean hornoObtenido = false;

                    try {

                        // acquire() solicita un permiso del semáforo.
                        // Solo dos cocineros pueden conseguirlo a la vez.
                        hornos.acquire();

                        hornoObtenido = true;

                        System.out.println(
                                "Cocinero " + numero
                                        + " prepara pedido "
                                        + pedido.getNumero()
                                        + " del cliente "
                                        + pedido.getCliente()
                        );

                        // Simulamos el tiempo de cocinado.
                        Thread.sleep(1000 + (int)(Math.random() * 2000));

                        // Incrementamos el contador de forma atómica.
                        completados.incrementAndGet();

                        System.out.println(
                                "Cocinero " + numero
                                        + " termina el pedido "
                                        + pedido.getNumero()
                        );

                    } finally {

                        // release() devuelve el permiso del horno.
                        // Solo lo liberamos si conseguimos el permiso.
                        if (hornoObtenido) {

                            hornos.release();
                        }
                    }
                }

            } catch (InterruptedException e) {

                // Permitimos que el cocinero termine de forma cooperativa.
                Thread.currentThread().interrupt();

                System.out.println(
                        "Cocinero " + numero + " interrumpido."
                );
            }
        }
    }
}
