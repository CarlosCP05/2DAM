package org.example.Pruebas.Hilos.examen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
## Ejercicio 2. Entender un programa asíncrono defectuoso (1 puntos)

Una aplicación consulta dos servicios independientes: temperatura y humedad.
Se desea que ambas consultas comiencen sin esperar a que termine la otra y que,
cuando las dos hayan acabado, se muestre el resultado.

 */
public class Ejercicio2 {

    static void main() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> temperatura =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "25 ºC";
                }, executor);


        CompletableFuture<String> humedad =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "60 %";
                }, executor);

        String tem = temperatura.join();
        String hum = humedad.join();

        System.out.println("Temperatura: " + tem);
        System.out.println("Humedad: " + hum);

        executor.shutdown();
    }
}

/*
He movido el print de Temperatura abajo junto con el print de humedad ya que el join que tenia impedia empzara a humedad
y también he cambiado lso join a variables tipo String para que esperara a tener las 2 variables listas antes de hacer los print y sacar los datos juntos
*/
