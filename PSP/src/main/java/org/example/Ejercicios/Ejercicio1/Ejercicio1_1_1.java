package org.example.Ejercicios.Ejercicio1;

public class Ejercicio1_1_1 extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(getName() + " ► " + i + "%");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Ejercicio1_1_1 t1 = new Ejercicio1_1_1();
        Ejercicio1_1_1 t2 = new Ejercicio1_1_1();
        Ejercicio1_1_1 t3 = new Ejercicio1_1_1();
        Ejercicio1_1_1 t4 = new Ejercicio1_1_1();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}