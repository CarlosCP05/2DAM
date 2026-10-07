package org.example.Ejercicios.Hilos.Sincronos.Ejercicios8;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Ejercicio8_2 {

    public static void main(String[] args) {

        Random rand = new Random();
        
        CyclicBarrier barrera = new CyclicBarrier(4, () -> {
            System.out.println("Fase completada, todos avanzan");
        });

        for (int i = 1; i <= 4; i++) {
            int id = i;

            Thread hilo = new Thread(() -> {
                try {
                    for (int fase = 1; fase <= 3; fase++) {
                        System.out.println(
                                "Hilo " + id +
                                        " empieza la fase " + fase
                        );

                        int tiempo = rand.nextInt(3) + 1;
                        Thread.sleep(tiempo * 1000L);

                        System.out.println(
                                "Hilo " + id +
                                        " termina la fase " + fase
                        );
                        barrera.await();
                    }

                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
            hilo.start();
        }
    }
}
