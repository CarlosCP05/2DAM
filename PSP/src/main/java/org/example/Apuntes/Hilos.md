Apuntes de concurrencia en Java

Versión de referencia: Java 21+

1. Síncrono y asíncrono
   Síncrono

La siguiente instrucción espera a que termine la operación anterior.

String resultado = consultarServicio();
procesar(resultado);


Usar cuando: el flujo es sencillo y necesitas el resultado antes de continuar.

Asíncrono

Permite iniciar una operación y continuar con otras tareas sin esperar inmediatamente su resultado.

CompletableFuture<String> future =
CompletableFuture.supplyAsync(() -> consultarServicio());

future.thenAccept(resultado -> procesar(resultado));


Usar cuando: existen operaciones independientes que pueden ejecutarse concurrentemente.

Asíncrono no significa necesariamente paralelo.

2. Thread

Representa un hilo de ejecución.

Métodos principales:

start(): inicia un hilo.
run(): ejecuta el método en el hilo actual si se invoca directamente.
join(): espera a que termine otro hilo.
sleep(ms): suspende temporalmente el hilo actual.
interrupt(): solicita una interrupción.
isInterrupted(): consulta el estado de interrupción.
currentThread(): devuelve el hilo actual.
isAlive(): comprueba si el hilo sigue activo.
Thread t = new Thread(() -> System.out.println("Hola"));
t.start();
t.join();


Usar cuando: necesitas crear un hilo directamente. En aplicaciones reales suele ser preferible utilizar un executor.

3. Runnable y Callable
   Runnable

Representa una tarea sin resultado.

Runnable tarea = () -> System.out.println("Trabajando");
new Thread(tarea).start();


Método: run().

Callable<T>

Representa una tarea que devuelve un resultado y puede lanzar excepciones checked.

Callable<Integer> tarea = () -> 10 + 20;


Método: call().

Se utiliza normalmente con ExecutorService.submit().

4. ExecutorService

Permite enviar tareas sin gestionar manualmente cada hilo.

Métodos principales:

execute(Runnable)
submit(Runnable)
submit(Callable<T>)
invokeAll(...)
invokeAny(...)
shutdown()
shutdownNow()
awaitTermination(...)
ExecutorService executor = Executors.newFixedThreadPool(4);

try {
Future<Integer> future = executor.submit(() -> 10 + 20);
System.out.println(future.get());
} finally {
executor.shutdown();
}


Usar cuando: necesitas ejecutar tareas con un ciclo de vida y un modelo de concurrencia controlados.

newFixedThreadPool(4) limita a cuatro los hilos del pool, pero su cola de tareas predeterminada no tiene una capacidad limitada.

5. ScheduledExecutorService

Ejecuta tareas con retraso o periódicamente.

Métodos:

schedule()
scheduleAtFixedRate()
scheduleWithFixedDelay()
ScheduledExecutorService scheduler =
Executors.newScheduledThreadPool(1);

scheduler.scheduleAtFixedRate(
() -> System.out.println("Cada segundo"),
0, 1, TimeUnit.SECONDS
);


Usar cuando: necesitas ejecutar tareas periódicas o programadas.

6. Hilos de plataforma y virtuales
   Thread.ofPlatform()

Crea un builder de hilos tradicionales.

Thread t = Thread.ofPlatform()
.name("worker")
.start(() -> trabajar());


Disponible desde Java 19.

Thread.ofVirtual()

Crea un hilo virtual.

Thread t = Thread.ofVirtual()
.start(() -> consultarBaseDatos());


Disponible de forma estable desde Java 21.

Usar hilos virtuales cuando: tienes muchas tareas concurrentes que pasan tiempo esperando operaciones bloqueantes de I/O.

No aceleran por sí mismos los cálculos intensivos de CPU.

Executor de hilos virtuales
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
executor.submit(() -> consultarAPI());
executor.submit(() -> consultarBaseDatos());
}


Crea un hilo virtual por tarea. No limita automáticamente el número de tareas concurrentes.

7. Future

Representa el resultado futuro de una tarea.

Métodos principales:

get()
get(timeout, unit)
isDone()
isCancelled()
cancel(true)
Future<String> future =
executor.submit(() -> "Resultado");

System.out.println(future.get());


get() bloquea al hilo que lo invoca hasta que el resultado está disponible.

8. CompletableFuture

Permite encadenar, combinar y gestionar operaciones asíncronas.

Métodos principales:

supplyAsync(): ejecuta una función con resultado.
runAsync(): ejecuta una tarea sin resultado.
thenApply(): transforma un resultado.
thenCompose(): encadena otra operación asíncrona.
thenCombine(): combina dos resultados.
allOf(): espera a varios futures.
exceptionally(): define una recuperación ante errores.
handle(): procesa resultado o error.
orTimeout(): establece un timeout.
join(): espera el resultado.
CompletableFuture<Integer> resultado =
CompletableFuture
.supplyAsync(() -> "Java")
.thenApply(String::length)
.exceptionally(error -> 0);


Usar cuando: necesitas componer varias operaciones asíncronas.

thenApply() transforma un resultado; thenCompose() evita anidar futures.

9. synchronized

Protege una sección crítica mediante un monitor.

class Contador {
private int valor;

    public synchronized void incrementar() {
        valor++;
    }

    public synchronized int obtener() {
        return valor;
    }
}


Usar cuando: varios hilos comparten y modifican el mismo estado.

Los métodos de instancia sincronizados utilizan el monitor de this.

10. volatile

Garantiza la visibilidad de las escrituras de una variable entre hilos.

private volatile boolean activo = true;

void detener() {
activo = false;
}


Usar cuando: necesitas publicar un estado simple sin operaciones compuestas.

volatile no hace atómico contador++.

11. AtomicInteger y AtomicReference

Permiten realizar operaciones atómicas.

AtomicInteger contador = new AtomicInteger();

contador.incrementAndGet();
contador.addAndGet(5);

int valor = contador.get();


Métodos:

get()
set()
incrementAndGet()
getAndIncrement()
addAndGet()
compareAndSet()
getAndSet()

AtomicReference<T> ofrece operaciones equivalentes para referencias.

Usar cuando: necesitas contadores o actualizaciones atómicas sin implementar manualmente un lock.

12. Lock y ReentrantLock

Proporcionan exclusión mutua con más opciones que synchronized.

Métodos:

lock()
unlock()
tryLock()
lockInterruptibly()
newCondition()
Lock lock = new ReentrantLock();

lock.lock();
try {
modificarEstado();
} finally {
lock.unlock();
}


Usar cuando: necesitas adquirir un lock con timeout, interrumpir la espera o utilizar condiciones.

Libera siempre el lock en finally.

13. ReadWriteLock

Permite múltiples lectores simultáneos y un escritor exclusivo.

ReadWriteLock rw = new ReentrantReadWriteLock();

rw.readLock().lock();
try {
leerDatos();
} finally {
rw.readLock().unlock();
}


Para escribir:

rw.writeLock().lock();
try {
actualizarDatos();
} finally {
rw.writeLock().unlock();
}


Usar cuando: hay muchas lecturas y pocas escrituras, siempre que las mediciones justifiquen su coste.

14. Semaphore

Limita el número de tareas que pueden acceder simultáneamente a un recurso.

Métodos:

acquire()
release()
tryAcquire()
availablePermits()
Semaphore sem = new Semaphore(3);

sem.acquire();
try {
consultarServicio();
} finally {
sem.release();
}


Como máximo tres tareas pueden poseer permisos simultáneamente.

Usar cuando: necesitas limitar peticiones HTTP, accesos a bases de datos u otros recursos.

15. BlockingQueue

Cola segura para varios hilos que puede bloquear productores y consumidores.

Métodos:

put(e): inserta y espera si está llena.
take(): extrae y espera si está vacía.
offer(e): intenta insertar sin esperar indefinidamente.
poll(): intenta extraer sin esperar indefinidamente.
peek(): consulta el primer elemento.
BlockingQueue<String> cola = new ArrayBlockingQueue<>(100);

// Productor
cola.put("tarea");

// Consumidor
String tarea = cola.take();
procesar(tarea);

Implementaciones
ArrayBlockingQueue: capacidad fija.
LinkedBlockingQueue: cola enlazada con capacidad configurable.
SynchronousQueue: no almacena elementos; productor y consumidor deben encontrarse.
PriorityBlockingQueue: ordena por prioridad y no tiene capacidad limitada por defecto.

Usar cuando: necesitas desacoplar productores y consumidores y controlar la acumulación de trabajo.

16. ConcurrentLinkedQueue

Cola concurrente no bloqueante.

Queue<String> cola = new ConcurrentLinkedQueue<>();

cola.offer("A");
String elemento = cola.poll();


poll() devuelve null si está vacía.

Diferencia con BlockingQueue: no espera si no hay elementos disponibles y no tiene una capacidad fija configurable.

17. ConcurrentHashMap

Mapa diseñado para el acceso concurrente.

ConcurrentHashMap<String, Integer> mapa =
new ConcurrentHashMap<>();

mapa.put("visitas", 1);
mapa.merge("visitas", 1, Integer::sum);


Métodos importantes:

putIfAbsent()
compute()
computeIfAbsent()
computeIfPresent()
merge()
replace()
remove()

Usar cuando: varios hilos necesitan leer o actualizar un mapa compartido.

18. CountDownLatch

Permite esperar hasta que un contador llegue a cero.

Métodos:

countDown()
await()
getCount()
CountDownLatch latch = new CountDownLatch(3);

// Cada trabajador:
try {
trabajar();
} finally {
latch.countDown();
}

// Coordinador:
latch.await();
continuar();


Usar cuando: necesitas esperar a que terminen varias tareas o a que varios componentes estén preparados.

No se puede reiniciar.

19. CyclicBarrier

Sincroniza varios hilos hasta que todos llegan a un punto.

CyclicBarrier barrier = new CyclicBarrier(3);

// En cada uno de los tres trabajadores:
prepararDatos();
barrier.await();
procesarSiguienteFase();


Usar cuando: varios trabajadores deben completar una fase antes de continuar.

Puede reutilizarse en varias rondas.

20. Phaser

Permite coordinar fases con un número variable de participantes.

Métodos:

register()
arrive()
arriveAndAwaitAdvance()
arriveAndDeregister()
awaitAdvance()
getPhase()
Phaser phaser = new Phaser(3);

// En cada trabajador:
preparar();
phaser.arriveAndAwaitAdvance();

procesar();
phaser.arriveAndAwaitAdvance();


Usar cuando: hay varias fases de trabajo y los participantes pueden incorporarse o retirarse dinámicamente.

21. Exchanger

Permite que dos hilos intercambien objetos.

Exchanger<String> exchanger = new Exchanger<>();

// Hilo A
String recibido = exchanger.exchange("dato A");

// Hilo B
String recibido = exchanger.exchange("dato B");


Cada hilo recibe el objeto enviado por el otro.

Usar cuando: dos tareas necesitan intercambiar buffers u objetos en un punto de encuentro.

22. wait(), notify() y notifyAll()

Métodos de Object para coordinar hilos mediante monitores.

synchronized (lock) {
while (!hayTrabajo()) {
lock.wait();
}

    procesarTrabajo();
}


Notificación:

synchronized (lock) {
publicarTrabajo();
lock.notifyAll();
}

wait() libera el monitor mientras espera.
notify() despierta a un hilo que espera en ese monitor.
notifyAll() despierta a todos los que esperan en ese monitor.

Utiliza siempre while para volver a comprobar la condición.

En código nuevo, suele ser preferible utilizar BlockingQueue, Condition u otras abstracciones de alto nivel.

23. ThreadLocal

Almacena un valor independiente para cada hilo.

ThreadLocal<String> usuario =
ThreadLocal.withInitial(() -> "anónimo");

usuario.set("usuario-1");

try {
procesarPeticion();
} finally {
usuario.remove();
}


Métodos:

get()
set()
remove()
withInitial()

Usar cuando: necesitas estado asociado a un hilo, por ejemplo, en algunas bibliotecas heredadas.

En pools, limpia los valores cuando corresponda para evitar que una tarea deje estado para otra.

24. Interrupciones

La interrupción solicita cooperativamente que una tarea termine.

Thread t = new Thread(() -> {
while (!Thread.currentThread().isInterrupted()) {
trabajar();
}
});

t.start();
t.interrupt();


Métodos:

interrupt()
isInterrupted()
Thread.interrupted()

Si capturas InterruptedException y no puedes propagarla, normalmente restaura la señal:

try {
Thread.sleep(1000);
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
return;
}


Interrumpir no mata a la fuerza el hilo.

25. TimeUnit y timeouts

Permite expresar tiempos de espera.

future.get(2, TimeUnit.SECONDS);
lock.tryLock(500, TimeUnit.MILLISECONDS);
executor.awaitTermination(10, TimeUnit.SECONDS);


Un timeout limita la espera de la operación correspondiente, pero no garantiza que un servicio externo se cancele.

26. ForkJoinPool

Diseñado para dividir tareas en subtareas y combinarlas.

Clases relacionadas:

ForkJoinPool
ForkJoinTask
RecursiveTask<T>
RecursiveAction

Ejemplo:

class Suma extends RecursiveTask<Long> {
private final int[] datos;
private final int inicio;
private final int fin;

    Suma(int[] datos, int inicio, int fin) {
        this.datos = datos;
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    protected Long compute() {
        if (fin - inicio <= 1000) {
            long total = 0;
            for (int i = inicio; i < fin; i++) {
                total += datos[i];
            }
            return total;
        }

        int medio = (inicio + fin) / 2;
        Suma izquierda = new Suma(datos, inicio, medio);
        Suma derecha = new Suma(datos, medio, fin);

        izquierda.fork();
        long resultadoDerecha = derecha.compute();
        long resultadoIzquierda = izquierda.join();

        return resultadoIzquierda + resultadoDerecha;
    }
}


Usar cuando: puedes dividir una tarea CPU-bound en subtareas independientes.

27. StructuredTaskScope

La concurrencia estructurada agrupa tareas relacionadas en un ámbito común.

La API ha cambiado entre versiones del JDK y algunas versiones requieren opciones de preview. Comprueba la documentación de la versión concreta antes de utilizarla.

En JDK 21, con preview habilitado, existía este patrón:

try (var scope =
new StructuredTaskScope.ShutdownOnFailure()) {

    var usuario = scope.fork(() -> buscarUsuario());
    var pedidos = scope.fork(() -> buscarPedidos());

    scope.join();
    scope.throwIfFailed();

    mostrar(usuario.get(), pedidos.get());
}


Usar cuando: varias tareas forman parte de una misma operación y deben compartir un ciclo de vida, manejo de errores y cancelación.

28. Comparativa rápida
    Necesidad	Herramienta
    Crear un hilo	Thread
    Definir una tarea sin resultado	Runnable
    Definir una tarea con resultado	Callable
    Gestionar tareas	ExecutorService
    Programar tareas periódicas	ScheduledExecutorService
    Muchas tareas bloqueantes	Hilos virtuales
    Representar un resultado futuro	Future
    Encadenar tareas asíncronas	CompletableFuture
    Proteger estado compartido	synchronized o Lock
    Publicar un estado simple	volatile
    Actualizar contadores atómicamente	AtomicInteger o LongAdder
    Limitar accesos concurrentes	Semaphore
    Transferir tareas entre hilos	BlockingQueue
    Compartir un mapa concurrente	ConcurrentHashMap
    Esperar a varias tareas	CountDownLatch
    Sincronizar fases	CyclicBarrier o Phaser
    Intercambiar objetos entre dos hilos	Exchanger
    Mantener estado por hilo	ThreadLocal
    Dividir trabajo CPU-bound	ForkJoinPool
29. Errores frecuentes
    Llamar a run() esperando crear otro hilo.
    Usar volatile para intentar hacer atómico contador++.
    Olvidar unlock() o release() en un finally.
    Crear hilos sin límites para tareas intensivas en CPU.
    Acumular tareas en colas ilimitadas hasta agotar memoria.
    Ignorar excepciones de Future o CompletableFuture.
    Suponer que cancelar una tarea la detiene inmediatamente.
    Mantener locks durante operaciones de red prolongadas.
    Suponer que los hilos virtuales eliminan los límites de CPU o conexiones.
    Acceder a datos mutables compartidos sin una estrategia de sincronización.
30. Regla práctica de elección
    Necesito ejecutar una tarea: ExecutorService.
    Tengo muchas operaciones bloqueantes: hilos virtuales.
    Tengo cálculos intensivos de CPU: pool de tamaño controlado.
    Necesito proteger datos compartidos: synchronized, Lock o atomics.
    Necesito limitar el número de accesos simultáneos: Semaphore.
    Necesito transferir trabajo: BlockingQueue.
    Necesito combinar resultados asíncronos: CompletableFuture.
    Necesito esperar a varias tareas: CountDownLatch o futures.
    Necesito coordinar fases: CyclicBarrier o Phaser.

La elección depende del problema: ejecución, resultado, estado compartido, limitación de recursos, coordinación o cancelación.