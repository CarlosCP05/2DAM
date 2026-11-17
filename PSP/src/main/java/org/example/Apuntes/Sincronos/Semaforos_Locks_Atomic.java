package org.example.Apuntes.Sincronos;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Semaforos_Locks_Atomic {

    public static void main(String[] args) throws InterruptedException {

        // ==========================================
        // EJEMPLO 1: REENTRANTLOCK
        // ==========================================

        ContadorLock contador = new ContadorLock();

        Thread h1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        });

        Thread h2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        });

        h1.start();
        h2.start();

        h1.join();
        h2.join();

        System.out.println("Contador con Lock: " + contador.getValor());


        // ==========================================
        // EJEMPLO 2: ATOMICINTEGER
        // ==========================================

        // AtomicInteger permite modificar un entero
        // compartido de forma atómica, sin usar synchronized.
        AtomicInteger contadorAtomico = new AtomicInteger(0);

        Thread h3 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {

                // Incrementa el valor de forma atómica.
                contadorAtomico.incrementAndGet();
            }
        });

        Thread h4 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {

                contadorAtomico.incrementAndGet();
            }
        });

        h3.start();
        h4.start();

        h3.join();
        h4.join();

        // get() devuelve el valor actual.
        System.out.println("Contador atómico: " + contadorAtomico.get());


        // ==========================================
        // EJEMPLO 3: SEMAPHORE
        // ==========================================

        // Semaphore(3) crea un semáforo con 3 permisos.
        // Permite que hasta 3 coches aparquen simultáneamente.
        Semaphore parking = new Semaphore(3);

        Thread[] coches = new Thread[10];

        for (int i = 0; i < 10; i++) {

            final int numeroCoche = i + 1;

            coches[i] = new Thread(() -> {

                // Indica si este coche ha conseguido un permiso.
                boolean permisoObtenido = false;

                try {

                    System.out.println(
                            "Coche " + numeroCoche + " quiere aparcar"
                    );

                    // acquire() solicita un permiso.
                    // Si no quedan permisos, el hilo espera.
                    parking.acquire();

                    permisoObtenido = true;

                    System.out.println(
                            "Coche " + numeroCoche + " aparcado. "
                                    + "Plazas libres: " + parking.availablePermits()
                    );

                    // Simulamos el tiempo que permanece aparcado.
                    Thread.sleep(2000);

                    System.out.println(
                            "Coche " + numeroCoche + " sale"
                    );

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                } finally {

                    // release() devuelve el permiso.
                    // Solo lo liberamos si realmente lo conseguimos.
                    if (permisoObtenido) {

                        parking.release();

                        System.out.println(
                                "Plazas libres: " + parking.availablePermits()
                        );
                    }
                }

            });

            coches[i].start();
        }

        for (Thread coche : coches) {
            coche.join();
        }


        // ==========================================
        // EJEMPLO 4: TRYLOCK
        // ==========================================

        ReentrantLock lock = new ReentrantLock();

        Thread trabajador = new Thread(() -> {

            boolean conseguido = false;

            try {

                // tryLock intenta conseguir el bloqueo.
                // Espera como máximo 2 segundos.
                conseguido = lock.tryLock(2, TimeUnit.SECONDS);

                if (conseguido) {

                    System.out.println("Lock conseguido");

                    Thread.sleep(1000);

                } else {

                    System.out.println("No se pudo conseguir el lock");
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            } finally {

                // Solo liberamos el lock si lo hemos conseguido.
                if (conseguido) {
                    lock.unlock();
                }
            }
        });

        trabajador.start();
        trabajador.join();

        System.out.println("Programa finalizado");
    }


    // ==========================================
    // CLASE CONTADOR CON REENTRANTLOCK
    // ==========================================

    static class ContadorLock {

        private int valor = 0;

        // ReentrantLock permite bloquear manualmente
        // una sección crítica para que entre un solo hilo.
        private final ReentrantLock lock = new ReentrantLock();

        public void incrementar() {

            // lock() adquiere el bloqueo.
            lock.lock();

            try {

                valor++;

            } finally {

                // unlock() libera el bloqueo.
                // finally garantiza que no olvidemos liberarlo.
                lock.unlock();
            }
        }

        public int getValor() {

            lock.lock();

            try {
                return valor;
            } finally {
                lock.unlock();
            }
        }
    }
}
