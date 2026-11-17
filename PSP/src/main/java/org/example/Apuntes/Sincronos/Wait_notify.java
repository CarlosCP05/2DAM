package org.example.Apuntes.Sincronos;


public class Wait_notify {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO: PRODUCTOR Y CONSUMIDOR
        // ==========================================

        // Creamos un buffer de una sola posición.
        Buffer buffer = new Buffer(1);

        // PRODUCTOR:
        // Introduce números en el buffer.
        Thread productor = new Thread(() -> {

            try {

                for (int i = 1; i <= 10; i++) {

                    buffer.producir(i);

                    Thread.sleep(500);
                }

                // Introducimos -1 para indicar que hemos terminado.
                buffer.producir(-1);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }

        });

        // CONSUMIDOR:
        // Recoge números del buffer.
        Thread consumidor = new Thread(() -> {

            try {

                while (true) {

                    int numero = buffer.consumir();

                    // -1 indica que no quedan más productos.
                    if (numero == -1) {
                        break;
                    }

                    System.out.println("Consumido: " + numero);

                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }

        });

        productor.start();
        consumidor.start();

        productor.join();
        consumidor.join();

        System.out.println("Productor y consumidor terminados");
    }


    // ==========================================
    // CLASE BUFFER
    // ==========================================

    static class Buffer {

        private final int[] datos;

        private int cantidad = 0;

        public Buffer(int capacidad) {

            datos = new int[capacidad];
        }

        // ======================================
        // MÉTODO PRODUCIR
        // ======================================

        public synchronized void producir(int numero)
                throws InterruptedException {

            // synchronized protege el buffer compartido.
            // Si está lleno, el productor debe esperar.
            while (cantidad == datos.length) {

                // wait() suspende el hilo y libera el monitor.
                wait();
            }

            // Introducimos el producto.
            datos[cantidad] = numero;
            cantidad++;

            System.out.println("Producido: " + numero);

            // Despertamos a los hilos que esperan en este monitor.
            notifyAll();
        }


        // ======================================
        // MÉTODO CONSUMIR
        // ======================================

        public synchronized int consumir()
                throws InterruptedException {

            // Si está vacío, el consumidor debe esperar.
            while (cantidad == 0) {

                // Espera hasta que haya algún producto.
                wait();
            }

            // Recogemos el producto.
            int numero = datos[0];

            cantidad--;

            // Despertamos a los hilos que esperan.
            notifyAll();

            return numero;
        }
    }
}
