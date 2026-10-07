package org.example.Ejercicios.Hilos.Asincronos;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejercicio15 {
    public static void main(String[] args) {
        AtomicInteger contador = new AtomicInteger();
        ArrayList<CompletableFuture<Void>> tareas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tareas.add(CompletableFuture.runAsync(() -> {
                for (int j = 0; j < 10_000; j++) contador.getAndIncrement();
            }));
        }

        CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0]))
                .thenAccept(v ->
                    System.out.println("Contador: " + contador.get())
                )
                .join();
    }
}
