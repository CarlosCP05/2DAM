package org.example.Ejercicios.Hilos.Problemas.Restaurante;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurante {
    static Random rand = new Random();
    AtomicInteger numCocinero = new AtomicInteger(0);
    AtomicInteger numCamarero = new AtomicInteger(0);
    AtomicInteger clientesAtendidos = new AtomicInteger();
    AtomicInteger clientesPerdidos = new AtomicInteger(0);

    BlockingQueue<Pedido> mesas = new java.util.concurrent.LinkedBlockingQueue<>(10);
    private final Thread[] clientes = new Thread[100];
    private final ExecutorService cocineros = Executors.newFixedThreadPool(3, r ->  new Thread(r, "Cocinero-" + numCocinero.incrementAndGet()));
    private final ExecutorService camareros = Executors.newFixedThreadPool(5,  r ->  new Thread(r, "Camarero-" + numCamarero.incrementAndGet()));
    Map<Integer, Boolean> mesasDisponibles =  new ConcurrentHashMap<>();
    private Boolean abierto = true;

    public Restaurante() {
        for  (int i = 1; i <= 10; i++) mesasDisponibles.put(i, true);
    }

    public static void main(String[] args) throws InterruptedException {
        Restaurante restaurante = new Restaurante();
        restaurante.abrirRestaurante();
    }

    public void abrirRestaurante() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            cocineros.execute(this::prepararPedidos);
        }

        for(int i = 0; i < clientes.length; i++) {
            clientes[i] = Thread.ofVirtual().name("Cliente-"+i).start(this::gestionarPedidos);
            Thread.sleep(500);
        }
        for(Thread cliente: clientes) cliente.join();

        camareros.shutdown();
        camareros.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.SECONDS);
        abierto = false;
        for(int i = 0; i < 3; i++) mesas.put(new Pedido(-1, abierto));
        cocineros.shutdown();
        cocineros.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.SECONDS);
        Thread.sleep(1000);
        System.out.println("Restaurante cerrado. Clientes atendidos: " + clientesAtendidos.get() + " Clientes perdidos: " + clientesPerdidos.get());
    }

    public void gestionarPedidos() {
        int mesa = ocuparMesa();
        if(mesa == -1){
            try {
                Thread.sleep(rand.nextInt(5000)+1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mesa = ocuparMesa();
            if(mesa == -1) clientesPerdidos.incrementAndGet();
        }
        if(mesa > 0){
            int mesaPed = mesa;
            camareros.execute(() -> {
                Pedido pedido = new Pedido(mesaPed, abierto);
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " ► Pedido de la mesa " + pedido.getMesa() + " recibido.");
                    mesas.put(pedido);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void prepararPedidos(){
        while(true){
            try {
                Pedido pedido = mesas.take();
                if(!pedido.getAbierto()) return;
                try {
                    Nota nota = pedido.tomarNota();
                    Thread.sleep(obtenerTiempoPlatos(nota.getPlatos()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " ► Pedido de la mesa " + pedido.getMesa() + " preparado. \n\tTiempo de preparacion: " + obtenerTiempoPlatos(pedido.tomarNota().getPlatos()));
                liberarMesa(pedido.getMesa());
                clientesAtendidos.incrementAndGet();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int ocuparMesa() {
        for(int i = 1; i <= mesasDisponibles.size(); i++) {
            if(mesasDisponibles.replace(i, true, false)) return i;
        }
        return -1;
    }

    private void liberarMesa(int mesa) {
        mesasDisponibles.put(mesa, true);
    }

    private int obtenerTiempoPlatos(List<TipoPlato> platos) {
        AtomicInteger tiempo = new AtomicInteger();
        platos.forEach(plato -> { tiempo.addAndGet(plato.getTiempo());});
        return tiempo.get();
    }
}