package org.example.Ejercicios.Hilos.Sincrona.Ejercicio3;

public class CuentaBancaria {
    public int saldo = 0;

    public  void depositar(int cantidad) {
        this.saldo += cantidad;
    }

    public void transefir(CuentaBancaria destino, int cantidad) {
        synchronized (this) {
            // Para que se bloque y ver el deadlock que si no no va
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (destino) {
                this.saldo -= cantidad;
                destino.depositar(cantidad);
            }
        }
    }
}