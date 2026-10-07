package org.example.Ejercicios.Hilos.Sincronos.Ejercicio2;

public class Ejercicio2_1 {

    public static void main(String[] args) throws InterruptedException {
        Thread cuenta = new Thread(() -> {
            for(int i = 10; i > 0; i--){
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cuenta.start();
        cuenta.join();
        System.out.println("¡¡¡Despegue!!!");
    }
}
