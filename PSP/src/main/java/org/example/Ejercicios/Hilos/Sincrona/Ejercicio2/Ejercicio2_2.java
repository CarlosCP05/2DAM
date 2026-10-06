package org.example.Ejercicios.Hilos.Sincrona.Ejercicio2;

public class Ejercicio2_2 {
    public static void main(String[] args) {

        Thread cocinero = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    System.out.println("Cocinando... segundo :" + i);
                    Thread.sleep(1000);
                }

                System.out.println("Plato cocinado");

            } catch (InterruptedException e) {
                System.out.println("Cocción cancelada");
            }
        });

        System.out.println("Estado antes de start(): " + cocinero.getState());
        cocinero.start();
        System.out.println("Estado después de start(): " + cocinero.getState());

        try {
            Thread.sleep(1000);
            System.out.println("Estado mientras cocina: " + cocinero.getState());
            Thread.sleep(2000);
            cocinero.interrupt();
            cocinero.join();
            System.out.println("Estado final: " + cocinero.getState());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
