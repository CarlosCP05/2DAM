package org.example.Ejercicios.Hilos.Asincronos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Ejercicio14 {
    public static void main(String[] args) {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        List<CompletableFuture<Integer>> tareas = new ArrayList<>();

        for (int i = 0; i < numeros.size(); i++) {
            int numero = numeros.get(i);
            tareas.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return numero * numero;
            }));
        }

        CompletableFuture.allOf(
                    tareas.toArray(new CompletableFuture[0])
                )
                .thenAccept(v -> {
                    for (int i = 0; i < tareas.size(); i++) {
                        int numero = numeros.get(i);
                        int resultado = tareas.get(i).join();
                        System.out.println(numero + " → " + resultado);
                    }
                }
        ).join();

    }
}
