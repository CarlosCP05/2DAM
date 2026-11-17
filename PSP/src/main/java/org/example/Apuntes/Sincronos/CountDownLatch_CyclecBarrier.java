package org.example.Apuntes.Sincronos;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class CountDownLatch_CyclecBarrier {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: COUNTDOWNLATCH
        // ==========================================

        // Creamos un CountDownLatch con 5 cuentas.
        // El main esperará hasta que las cinco cuentas lleguen a cero.
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 1; i <= 5; i++) {

            final int numero = i;

            Thread trabajador = new Thread(() -> {

                try {

                    System.out.println(
                            "Trabajador " + numero + " cargando datos..."
                    );

                    Thread.sleep(1000 + numero * 300);

                    System.out.println(
                            "Trabajador " + numero + " ha terminado"
                    );

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                } finally {

                    // countDown() resta una unidad al contador.
                    // Se utiliza cuando un trabajador termina su fase.
                    latch.countDown();
                }

            });

            trabajador.start();
        }

        System.out.println("Main esperando a los trabajadores...");

        // await() bloquea el main hasta que el contador llegue a cero.
        latch.await();

        System.out.println("Sistema listo: todos han terminado");


        // ==========================================
        // EJEMPLO 2: CYCLICBARRIER
        // ==========================================

        // Creamos una barrera para 5 corredores.
        // Ninguno podrá continuar hasta que lleguen los cinco.
        CyclicBarrier barrera = new CyclicBarrier(5, () -> {

            // Esta acción se ejecuta cuando todos llegan a la barrera.
            System.out.println("¡Todos preparados! ¡Comienza la carrera!");
        });

        Thread[] corredores = new Thread[5];

        for (int i = 1; i <= 5; i++) {

            final int numero = i;

            corredores[i - 1] = new Thread(() -> {

                try {

                    System.out.println(
                            "Corredor " + numero + " preparándose..."
                    );

                    // Simulamos que cada corredor tarda
                    // un tiempo diferente en prepararse.
                    Thread.sleep(numero * 500);

                    System.out.println(
                            "Corredor " + numero + " está en la salida"
                    );

                    // await() espera a que lleguen todos los corredores.
                    barrera.await();

                    System.out.println(
                            "Corredor " + numero + " empieza a correr"
                    );

                    // Simulamos la carrera.
                    Thread.sleep(1000 + numero * 300);

                    System.out.println(
                            "Corredor " + numero + " ha llegado a la meta"
                    );

                } catch (Exception e) {

                    Thread.currentThread().interrupt();
                }

            });

            corredores[i - 1].start();
        }

        // Esperamos a que terminen todos los corredores.
        for (Thread corredor : corredores) {
            corredor.join();
        }

        System.out.println("Carrera finalizada");
    }
}
