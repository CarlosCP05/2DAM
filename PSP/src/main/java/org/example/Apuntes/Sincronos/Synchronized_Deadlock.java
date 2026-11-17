package org.example.Apuntes.Sincronos;


public class Synchronized_Deadlock {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: CONTADOR SIN SINCRONIZAR
        // ==========================================

        Contador contadorRoto = new Contador();

        Thread[] hilos = new Thread[10];

        for (int i = 0; i < 10; i++) {

            hilos[i] = new Thread(() -> {

                for (int j = 0; j < 10000; j++) {

                    // Varios hilos modifican la misma variable.
                    // Puede producirse una condición de carrera.
                    contadorRoto.incrementarSincronizadoNo();

                }

            });

            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            hilo.join();
        }

        System.out.println(
                "Contador sin sincronizar: " + contadorRoto.getValor()
        );


        // ==========================================
        // EJEMPLO 2: CONTADOR CON SYNCHRONIZED
        // ==========================================

        Contador contadorCorrecto = new Contador();

        Thread[] hilos2 = new Thread[10];

        for (int i = 0; i < 10; i++) {

            hilos2[i] = new Thread(() -> {

                for (int j = 0; j < 10000; j++) {

                    // synchronized permite que solo un hilo
                    // ejecute este método a la vez.
                    contadorCorrecto.incrementar();

                }

            });

            hilos2[i].start();
        }

        for (Thread hilo : hilos2) {
            hilo.join();
        }

        System.out.println(
                "Contador sincronizado: " + contadorCorrecto.getValor()
        );


        // ==========================================
        // EJEMPLO 3: BLOQUE SYNCHRONIZED
        // ==========================================

        ContadorBloques contador = new ContadorBloques();

        Thread hilo1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        });

        Thread hilo2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        });

        hilo1.start();
        hilo2.start();

        hilo1.join();
        hilo2.join();

        System.out.println(
                "Contador con bloque: " + contador.getValor()
        );


        // ==========================================
        // EJEMPLO 4: EVITAR DEADLOCK
        // ==========================================

        Object recursoA = new Object();
        Object recursoB = new Object();

        Thread trabajador1 = new Thread(() -> {

            // Ambos trabajadores adquieren los recursos
            // siempre en el mismo orden: A y después B.
            synchronized (recursoA) {

                System.out.println("Hilo 1 tiene A");

                synchronized (recursoB) {

                    System.out.println("Hilo 1 tiene A y B");

                }
            }
        });

        Thread trabajador2 = new Thread(() -> {

            // Mantenemos el mismo orden de adquisición.
            synchronized (recursoA) {

                System.out.println("Hilo 2 tiene A");

                synchronized (recursoB) {

                    System.out.println("Hilo 2 tiene A y B");

                }
            }
        });

        trabajador1.start();
        trabajador2.start();

        trabajador1.join();
        trabajador2.join();

        System.out.println("Programa finalizado");
    }


    // ==========================================
    // CLASE CONTADOR
    // ==========================================

    static class Contador {

        private int valor = 0;

        // SIN SYNCHRONIZED:
        // Varios hilos pueden modificar el contador a la vez.
        public void incrementarSincronizadoNo() {
            valor++;
        }

        // CON SYNCHRONIZED:
        // El monitor del objeto permite que entre un solo hilo
        // cada vez en este método.
        public synchronized void incrementar() {
            valor++;
        }

        public int getValor() {
            return valor;
        }
    }


    // ==========================================
    // CLASE CONTADOR CON BLOQUE SYNCHRONIZED
    // ==========================================

    static class ContadorBloques {

        private int valor = 0;

        public void incrementar() {

            // Solo protegemos la parte que modifica el contador.
            // synchronized(this) utiliza el monitor de este objeto.
            synchronized (this) {

                valor++;

            }
        }

        public synchronized int getValor() {
            return valor;
        }
    }
}
