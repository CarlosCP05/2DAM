package org.example.Ejercicios.Hilos.Sincronos.Ejercicios7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Ejercicio7_2 {

    public static void main(String[] args) throws InterruptedException {

        List<Callable<String>> tareas = new ArrayList<>();

        Random rand = new Random();

        for (int i = 1; i <= 5; i++) {

            int id = i;

            tareas.add(() -> {

                int tiempo = rand.nextInt(3) + 1;

                System.out.println("API " + id + " empieza.");

                Thread.sleep(tiempo * 1000L);

                return "Respuesta de API " + id +
                        " (tardó " + tiempo + " segundos)";
            });
        }

        try (ExecutorService executor = Executors.newFixedThreadPool(5)) {

            List<Future<String>> resultados = executor.invokeAll(tareas);

            for (Future<String> future : resultados) System.out.println(future.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

