package org.example.Ejercicios.Hilos.Sincrona.Ejercicios8;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Ejercicio8_1 {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch preparados = new CountDownLatch(5);

        CountDownLatch salida = new CountDownLatch(1);

        Random rand = new Random();

        for (int i = 1; i <= 5; i++) {
            int id = i;

            Thread corredor = new Thread(() -> {
                try {
                    int tiempo = rand.nextInt(3) + 1;

                    System.out.println("Corredor " + id + " calentando durante " + tiempo + " segundos.");

                    Thread.sleep(tiempo * 1000L);

                    System.out.println("Corredor " + id + " está listo.");
                    preparados.countDown();
                    salida.await();
                    System.out.println("Corredor " + id + " ¡CORRE!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            corredor.start();
        }
        preparados.await();
        System.out.println("Todos están listos. ¡Preparados... YA!");
        salida.countDown();
    }
}
