package org.example.Ejercicios.Hilos.Sincronos.Ejercicios5;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio5_3 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread hilo1 = new Thread(() -> {
            lock.lock();

            try {
                System.out.println("Hilo 1 tiene el lock.");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("Hilo 1 libera el lock.");
            }
        });
        Thread hilo2 = new Thread(() -> {

            while (true) {
                try {
                    if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {

                        try {
                            System.out.println("Hilo 2 ha conseguido el lock.");
                            break;

                        } finally {
                            lock.unlock();
                        }

                    } else {
                        System.out.println("Ocupado, lo intento más tarde");
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        hilo1.start();
        hilo2.start();
    }
}
