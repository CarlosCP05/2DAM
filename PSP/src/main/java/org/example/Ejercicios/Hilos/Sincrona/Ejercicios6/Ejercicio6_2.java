package org.example.Ejercicios.Hilos.Sincrona.Ejercicios6;

import java.util.concurrent.ConcurrentHashMap;

public class Ejercicio6_2 {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentHashMap<String, Integer> frecuencia =
                new ConcurrentHashMap<>();

        String[] fragmentos = {
                "el profesor que regalan puntos son muy majos",
                "el profesor que lea esto es el mejor del mundo",
                "no soy pelota, solo estoy aburrido y quizá tu también"
        };

        Thread[] hilos = new Thread[fragmentos.length];

        for (int i = 0; i < fragmentos.length; i++) {

            String fragmento = fragmentos[i];

            hilos[i] = new Thread(() -> {

                String[] palabras = fragmento.split(" ");

                for (String palabra : palabras) {
                    frecuencia.merge(palabra, 1, Integer::sum);
                }
            });
            hilos[i].start();
        }

        for (Thread hilo : hilos) hilo.join();

        System.out.println(frecuencia);
    }
}
