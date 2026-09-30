package org.example.Ejercicios.Ejercicio1;

public class Ejercicio1_2 implements Runnable {
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

    public static void main(String[] args) throws InterruptedException {
        Thread[] archivos = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
             archivos[i] = Thread.ofVirtual().name("descarga-" + i).start(new Ejercicio1_2());
        }
        for (Thread archivo : archivos) {
            try {
                archivo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}