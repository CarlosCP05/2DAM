package org.example.Apuntes.Sincronos;


public class Ciclo_vida {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: OBSERVAR ESTADOS
        // ==========================================

        Thread hilo = new Thread(() -> {

            try {
                System.out.println("Hilo trabajando...");

                // Durante 3 segundos estará en TIMED_WAITING.
                Thread.sleep(3000);

                System.out.println("Hilo terminado");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        });

        // NEW: creado pero todavía no iniciado.
        System.out.println("Estado: " + hilo.getState());

        hilo.start();

        // Comprobamos el estado mientras siga vivo.
        while (hilo.isAlive()) {

            // getState() devuelve el estado actual del hilo.
            System.out.println("Estado: " + hilo.getState());

            Thread.sleep(500);
        }

        // TERMINATED: el hilo ya ha terminado.
        System.out.println("Estado final: " + hilo.getState());


        // ==========================================
        // EJEMPLO 2: UTILIZAR JOIN()
        // ==========================================

        Thread descarga1 = crearDescarga("Archivo 1", 2000);
        Thread descarga2 = crearDescarga("Archivo 2", 3000);
        Thread descarga3 = crearDescarga("Archivo 3", 1500);

        descarga1.start();
        descarga2.start();
        descarga3.start();

        // join() hace que el main espere a que termine cada hilo.
        descarga1.join();
        descarga2.join();
        descarga3.join();

        System.out.println("Todas las descargas han finalizado");


        // ==========================================
        // EJEMPLO 3: INTERRUPT()
        // ==========================================

        Thread trabajador = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {

                System.out.println("Trabajador trabajando...");

                try {

                    Thread.sleep(500);

                } catch (InterruptedException e) {

                    // sleep() lanza esta excepción si recibe
                    // una interrupción mientras está esperando.
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Trabajador detenido correctamente");
        });

        trabajador.start();

        // El main espera dos segundos.
        Thread.sleep(2000);

        // Solicitamos al trabajador que termine.
        trabajador.interrupt();

        // Esperamos a que termine.
        trabajador.join();

        System.out.println("Programa finalizado");
    }


    // Método que crea una tarea de descarga.
    public static Thread crearDescarga(String nombre, int tiempo) {

        return new Thread(() -> {

            System.out.println(nombre + " comenzando...");

            try {

                Thread.sleep(tiempo);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;
            }

            System.out.println(nombre + " descargado");

        });
    }
}
