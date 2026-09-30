package org.example.Ejercicios.Ejercicio1;

public class Ejercicio1_1_2 implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread() + " ► " + i + "%");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
             Thread.ofPlatform().name("descarga-" + i).start(new Ejercicio1_1_2());
        }
    }
}