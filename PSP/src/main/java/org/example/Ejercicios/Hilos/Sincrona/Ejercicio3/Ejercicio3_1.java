package org.example.Ejercicios.Hilos.Sincrona.Ejercicio3;

public class Ejercicio3_1 {

    public static void main(String[] args) throws InterruptedException {
        Thread[] depositos = new Thread[100];
        CuentaBancaria cuenta = new CuentaBancaria();

        for (int i = 0; i < 100; i++) {
            depositos[i] = new Thread(() -> {
                cuenta.depositar(1);
            });
            depositos[i].start();
        }

        for (Thread deposito : depositos) {
            try {
                deposito.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Saldo: " + cuenta.saldo);
    }
}
