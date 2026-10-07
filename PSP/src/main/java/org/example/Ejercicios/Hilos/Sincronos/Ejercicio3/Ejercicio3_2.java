package org.example.Ejercicios.Hilos.Sincronos.Ejercicio3;

public class Ejercicio3_2 {
    public static void main(String[] args) throws InterruptedException {
        CuentaBancaria a = new CuentaBancaria();
        CuentaBancaria b = new CuentaBancaria();
        a.saldo = 500;
        b.saldo = 1000;

        Thread ab = new Thread(() -> {
            a.transefir(b, 100);
        });

        Thread ba = new Thread(() -> {
            b.transefir(a, 350);
        });

        ab.start();
        ba.start();

        ab.join();
        ba.join();

        System.out.println("Saldo A: " + a.saldo);
        System.out.println("Saldo B: " + b.saldo);
    }
}
