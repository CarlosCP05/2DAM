package org.example.Ejercicios.Hilos.Sincronos.Ejercicios7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Ejercicio7_1 {

    public static void main(String[] args) throws Exception {

        int[] numeros = new int[10000000];

        Arrays.fill(numeros, 1);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<Long>> resultados = new ArrayList<>();

        int tamanio = numeros.length / 4;

        for (int i = 0; i < 4; i++) {

            int inicio = i * tamanio;
            int fin;

            if (i == 3) fin = numeros.length;
            else fin = (i + 1) * tamanio;

            Callable<Long> tarea = () -> {
                long suma = 0;

                for (int j = inicio; j < fin; j++) suma += numeros[j];

                return suma;
            };

            Future<Long> resultado = executor.submit(tarea);

            resultados.add(resultado);
        }

        long sumaTotal = 0;

        for (Future<Long> resultado : resultados) sumaTotal += resultado.get();

        executor.shutdown();

        System.out.println("Suma total: " + sumaTotal);
    }
}
