package org.example.Ejercicios.Hilos.Sincronos.Ejercicios6;

import java.util.concurrent.ArrayBlockingQueue;

//No está el ejercicio 4.1 en los ejercicios
//Es un ejemplo para ver el ArrayBlocking de GPT
public class Ejercicio6_1 {
    public static void main(String[] args) throws InterruptedException {
        // Creamos una cola de enteros con capacidad máxima de 5 elementos.
        // ArrayBlockingQueue se encarga automáticamente de la sincronización.
        // Si la cola está llena, put() espera.
        // Si la cola está vacía, take() espera.
        ArrayBlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

        // Creamos un hilo que será el productor.
        Thread productor = new Thread(() -> {
            try {
                // El productor va a producir los números del 1 al 10.
                for (int i = 1; i <= 10; i++) {

                    // Introduce el número en la cola.
                    //
                    // Si la cola tiene espacio, introduce el número.
                    // Si la cola está llena (5 elementos), el hilo ESPERA
                    // hasta que haya espacio.
                    cola.put(i);

                    System.out.println("Productor: produce " + i);

                    // Simulamos que el productor tarda 0,5 segundos
                    // en producir el siguiente elemento.
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {

                // Si el hilo es interrumpido, restauramos su estado
                // de interrupción.
                Thread.currentThread().interrupt();
            }
        });

        // Creamos un hilo que será el consumidor.
        Thread consumidor = new Thread(() -> {
            try {
                // El consumidor va a consumir 10 elementos.
                for (int i = 1; i <= 10; i++) {

                    // Saca un elemento de la cola.
                    // Si hay elementos, lo devuelve inmediatamente.
                    // Si la cola está VACÍA, el hilo ESPERA
                    // hasta que haya un elemento disponible.
                    int numero = cola.take();

                    System.out.println("Consumidor: consume " + numero);

                    // Simulamos que el consumidor tarda 1 segundo
                    // en procesar cada elemento.
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {

                // Restauramos el estado de interrupción del hilo.
                Thread.currentThread().interrupt();
            }
        });

        // Arrancamos el productor.
        productor.start();
        // Arrancamos el consumidor.
        consumidor.start();
        // Esperamos a que termine el productor.
        productor.join();
        // Esperamos a que termine el consumidor.
        consumidor.join();
        System.out.println("Fin del programa");
    }
}