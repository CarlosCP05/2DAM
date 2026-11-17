package org.example.Apuntes.Sincronos;


public class Creacion_y_ejecucion {

    public static void main(String[] args) {

        // ==========================================
        // EJEMPLO 1: CREAR HILOS CON THREAD
        // ==========================================

        // Creamos dos objetos de nuestra clase que hereda de Thread.
        ContadorThread hilo1 = new ContadorThread("Hilo 1");
        ContadorThread hilo2 = new ContadorThread("Hilo 2");

        // start() inicia un nuevo hilo y ejecuta su método run().
        // Ambos hilos pueden ejecutarse concurrentemente.
        hilo1.start();
        hilo2.start();


        // ==========================================
        // EJEMPLO 2: CREAR HILOS CON RUNNABLE
        // ==========================================

        // Runnable representa una tarea que queremos ejecutar.
        MiTarea tarea = new MiTarea();

        // Creamos un Thread y le pasamos la tarea que debe ejecutar.
        Thread hilo3 = new Thread(tarea, "Hilo 3");

        // Iniciamos el nuevo hilo.
        hilo3.start();


        // ==========================================
        // EJEMPLO 3: DIFERENCIA ENTRE RUN() Y START()
        // ==========================================

        MiTarea tarea2 = new MiTarea();

        // run() ejecuta las instrucciones directamente en el hilo actual.
        // NO crea un hilo nuevo.
        tarea2.run();

        // start() sí crea un nuevo hilo de ejecución.
        Thread hilo4 = new Thread(tarea2);
        hilo4.start();


        // ==========================================
        // EJEMPLO 4: HILOS DAEMON
        // ==========================================

        // Un hilo daemon es un hilo secundario que no impide que Java cierre el programa cuando terminan los hilos normales.
        Thread daemon = new Thread(() -> {

            while (true) {

                System.out.println("El hilo daemon sigue trabajando...");

                try {

                    // sleep() pausa el hilo actual durante 1 segundo.
                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    // Si se interrumpe mientras duerme, recuperamos
                    // el estado de interrupción y terminamos el hilo.
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Marcamos el hilo como daemon ANTES de iniciarlo.
        daemon.setDaemon(true);

        // Iniciamos el hilo daemon.
        daemon.start();

        System.out.println("El main ha terminado.");

        // Cuando no quedan hilos normales activos,
        // Java puede finalizar aunque el daemon siga trabajando.
    }


    // ==========================================
    // CLASE 1: HEREDAR DE THREAD
    // ==========================================

    static class ContadorThread extends Thread {

        public ContadorThread(String nombre) {

            // super(nombre) asigna un nombre al hilo.
            super(nombre);
        }

        @Override
        public void run() {

            // run() contiene las instrucciones que ejecutará el hilo.
            for (int i = 1; i <= 10; i++) {

                // getName() devuelve el nombre del hilo actual.
                System.out.println(getName() + ": " + i);

                try {

                    // Pausamos este hilo durante 500 milisegundos.
                    Thread.sleep(500);

                } catch (InterruptedException e) {

                    // Si el hilo es interrumpido durante sleep(),
                    // recuperamos la interrupción y terminamos su ejecución.
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }


    // ==========================================
    // CLASE 2: IMPLEMENTAR RUNNABLE
    // ==========================================

    static class MiTarea implements Runnable {

        @Override
        public void run() {

            // Runnable define el trabajo que realizará el hilo.
            for (int i = 1; i <= 5; i++) {

                // currentThread() permite obtener el hilo que ejecuta
                // actualmente este código.
                System.out.println(
                        Thread.currentThread().getName()
                                + " ejecutando: " + i
                );

                try {

                    // Pausamos el hilo actual durante medio segundo.
                    Thread.sleep(500);

                } catch (InterruptedException e) {

                    // Terminamos de forma cooperativa si nos interrumpen.
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
