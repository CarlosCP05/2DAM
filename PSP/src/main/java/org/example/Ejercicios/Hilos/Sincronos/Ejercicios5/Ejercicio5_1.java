package org.example.Ejercicios.Hilos.Sincronos.Ejercicios5;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Ejercicio5_1 {
    public static void main(String[] args) throws InterruptedException {
        Semaphore parking = new Semaphore(3);
        Random rand = new Random();
        Thread[] coches = new Thread[10];

        for (int i = 0; i < coches.length; i++) {
            String coche ="Coche " + i+1;
            coches[i] = new Thread(() -> {
                try{
                    int espera = rand.nextInt(100, 5000);
                    System.out.println(coche + " entra al parking.");

                    if (parking.availablePermits() == 0) System.out.println(coche + " espera: no hay plazas.");

                    parking.acquire();
                    Thread.sleep(espera);
                    System.out.println(coche + " entra al parking.");
                    parking.release();
                } catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            });
            coches[i].start();
        }

        for(Thread c : coches) c.join();

        System.out.println("FIN");
    }
}
