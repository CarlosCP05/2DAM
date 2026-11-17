Comparativa de herramientas de concurrencia y asincronía en Java
1. Creación y ejecución de tareas

Estas herramientas permiten ejecutar código en hilos diferentes, pero no ofrecen el mismo nivel de control.

Herramienta	¿Qué hace?	Ventajas	Desventajas	¿Cuándo utilizarla?
Thread	Representa un hilo de ejecución.	Control directo sobre el hilo. Permite utilizar start(), interrupt(), join(), etc.	Crear y administrar muchos hilos manualmente es complicado.	Para aprender cómo funcionan los hilos o cuando necesitamos control directo sobre uno.
Runnable	Representa una tarea que no devuelve resultado.	Separa la tarea del hilo. Permite reutilizar la tarea y pasarla a un executor.	No devuelve resultados directamente.	Opción habitual para tareas sin resultado.
Callable<T>	Representa una tarea que devuelve un resultado de tipo T.	Devuelve resultados y permite lanzar excepciones comprobadas.	Necesita un mecanismo de ejecución, como ExecutorService.	Cuando necesitamos calcular o devolver un resultado.
Executor	Abstracción que ejecuta tareas.	Separa la ejecución de la tarea de su implementación.	Tiene menos funcionalidades que ExecutorService.	Cuando únicamente necesitamos enviar tareas a un ejecutor.
ExecutorService	Administra la ejecución de tareas y un conjunto de hilos.	Reutiliza hilos, permite enviar tareas, obtener resultados y cerrar el servicio correctamente.	Hay que gestionar su cierre.	Opción recomendada para administrar tareas en segundo plano.
Thread frente a Runnable
Característica	Thread	Runnable
Es una clase o interfaz	Clase	Interfaz
Permite definir una tarea	Sí	Sí
Permite devolver resultados	No directamente	No
Se puede utilizar con un executor	No directamente como tarea, aunque implementa Runnable	Sí
Permite heredar de otra clase	No, porque ya hereda de Thread	Sí
Separa tarea y ejecución	No tanto	Sí

Recomendación: normalmente es mejor implementar Runnable y dejar que ExecutorService administre los hilos. Heredar de Thread tiene sentido en casos concretos, pero no suele ser necesario.

Runnable frente a Callable
Característica	Runnable	Callable<T>
Devuelve resultado	No	Sí
Método principal	run()	call()
Puede lanzar excepciones comprobadas	No	Sí
Se envía con	execute() o submit()	submit()
Resultado mediante Future	No directamente con execute()	Sí, mediante Future<T>

Ejemplo:

java
// Runnable: no devuelve nada.
Runnable tarea = () -> {
System.out.println("Ejecutando");
};

// Callable: devuelve un Integer.
Callable<Integer> calculo = () -> {
return 10 + 20;
};


¿Cuál utilizar?

Runnable: enviar mensajes, procesar archivos, realizar tareas que no necesitan devolver un resultado.
Callable: calcular importes, consultar datos, obtener resultados de operaciones.
2. Formas de ejecutar tareas: ExecutorService

Executors proporciona diferentes formas de crear un ExecutorService.

Método	Funcionamiento	Ventajas	Inconvenientes	¿Cuándo utilizarlo?
newFixedThreadPool(n)	Mantiene un número fijo de hilos.	Controlamos cuántos hilos trabajan simultáneamente y los reutilizamos.	Las tareas pueden acumularse en la cola.	Opción habitual cuando conocemos aproximadamente el nivel de concurrencia necesario.
newCachedThreadPool()	Crea hilos según las necesidades y reutiliza los que quedan libres.	Se adapta a cargas variables de tareas cortas.	Puede crear muchos hilos si llegan demasiadas tareas.	Cuando hay muchas tareas cortas y una carga variable, controlando los recursos.
newSingleThreadExecutor()	Ejecuta las tareas utilizando un único hilo.	Garantiza que las tareas se ejecuten secuencialmente.	No hay ejecución paralela entre sus tareas.	Cuando necesitamos mantener un orden de ejecución o evitar accesos concurrentes a determinados recursos.

Ejemplo:

java
ExecutorService executor =
Executors.newFixedThreadPool(4);


Recomendación: para los ejercicios y aplicaciones generales de DAM, empezaría utilizando newFixedThreadPool().

Es importante recordar que los métodos de fábrica tradicionales de Executors pueden utilizar colas sin límite. En aplicaciones reales también conviene estudiar ThreadPoolExecutor para controlar la capacidad de la cola y el comportamiento cuando se satura.

Métodos de cierre de ExecutorService
Método	¿Qué hace?	¿Interrumpe las tareas?	¿Cuándo utilizarlo?
shutdown()	Deja de aceptar nuevas tareas, pero permite terminar las enviadas.	No solicita interrumpirlas.	Cierre normal recomendado.
shutdownNow()	Intenta detener las tareas y devuelve las que estaban pendientes.	Sí, solicita interrupción a las tareas activas.	Cuando necesitamos un cierre forzado o ha vencido un tiempo de espera.
awaitTermination()	Espera durante un tiempo máximo a que el executor termine.	No por sí mismo.	Para comprobar si el cierre se ha completado.

Ejemplo habitual:

java
executor.shutdown();

if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
executor.shutdownNow();
}

3. Future frente a CompletableFuture

Ambos representan resultados de operaciones que pueden completarse en el futuro.

Característica	Future<T>	CompletableFuture<T>
Representa un resultado futuro	Sí	Sí
Obtener resultado	get()	get() o join()
Comprobar finalización	isDone()	isDone()
Cancelar	cancel()	cancel()
Establecer timeout	get(timeout, unidad)	orTimeout()
Encadenar operaciones	No cómodamente	Sí
Combinar resultados	No cómodamente	Sí
Gestionar errores	Mediante excepciones al recuperar el resultado	exceptionally(), handle(), whenComplete()
Completar manualmente	No de forma general desde la interfaz	Sí, mediante complete()
Composición asíncrona	Limitada	Sí
¿Cuál utilizar?
Situación	Elección
Lanzar una tarea y recuperar su resultado posteriormente	Future
Comprobar si una tarea ha terminado	Ambos
Cancelar una tarea	Ambos
Establecer un tiempo máximo de espera	Ambos, con mecanismos diferentes
Ejecutar B cuando termine A	CompletableFuture
Ejecutar varias tareas independientes y combinar resultados	CompletableFuture
Crear cadenas de operaciones asíncronas	CompletableFuture
Recuperarse de errores dentro de una cadena	CompletableFuture

Recomendación: si estamos trabajando con Java moderno y necesitamos componer operaciones asíncronas, normalmente elegiremos CompletableFuture.

Future sigue siendo útil para representar resultados de tareas enviadas mediante ExecutorService, especialmente en ejemplos sencillos y código que no necesita composición.

4. get() frente a join()

Ambos permiten obtener el resultado de un Future o CompletableFuture, pero tienen diferencias.

Característica	get()	join()
Espera al resultado	Sí	Sí
Puede bloquear el hilo	Sí	Sí
Excepciones comprobadas	InterruptedException y ExecutionException	No
Excepción de ejecución	ExecutionException	CompletionException
Timeout incorporado	Sí, con get(timeout, unidad)	No
Uso habitual	Future	CompletableFuture

Ejemplos:

java
Integer resultado = future.get();

java
Integer resultado = future.join();


Recomendación:

Con Future, normalmente utilizaremos get().
Con CompletableFuture, normalmente utilizaremos join() cuando queramos esperar el resultado.

Importante: ninguno es asíncrono. Ambos pueden bloquear el hilo que los llama.

5. Operaciones de CompletableFuture

Esta es una de las comparativas más importantes.

runAsync() frente a supplyAsync()
Método	¿Devuelve resultado?	Tipo devuelto	¿Cuándo utilizarlo?
runAsync()	No	CompletableFuture<Void>	Cuando queremos ejecutar una tarea sin devolver información.
supplyAsync()	Sí	CompletableFuture<T>	Cuando necesitamos obtener un resultado.

Ejemplo:

java
// Sin resultado.
CompletableFuture<Void> f1 =
CompletableFuture.runAsync(() -> {
System.out.println("Procesando");
});

// Con resultado.
CompletableFuture<Integer> f2 =
CompletableFuture.supplyAsync(() -> {
return 10 + 20;
});


Regla: runAsync() para ejecutar; supplyAsync() para calcular y devolver.

thenApply() frente a thenAccept() frente a thenRun()
Método	Recibe el resultado anterior	Devuelve otro resultado	Finalidad	¿Cuándo utilizarlo?
thenApply()	Sí	Sí	Transformar el resultado.	Cuando queremos modificar o calcular un nuevo valor.
thenAccept()	Sí	No	Consumir el resultado.	Cuando queremos mostrarlo, guardarlo o utilizarlo sin devolver otro valor.
thenRun()	No	No	Ejecutar una acción posterior.	Cuando solo queremos hacer algo después de terminar la operación anterior.

Ejemplo:

java
CompletableFuture
.supplyAsync(() -> 10)

        // Transformamos 10 en 20.
        .thenApply(n -> n * 2)

        // Mostramos 20.
        .thenAccept(n ->
                System.out.println(n))

        // Ejecutamos una acción sin necesitar el resultado.
        .thenRun(() ->
                System.out.println("Fin"));


Regla para memorizar:

thenApply: transformar.
thenAccept: consumir.
thenRun: hacer algo después.
thenApply() frente a thenCompose()

Ambos encadenan operaciones, pero el tipo de resultado de la función que reciben es diferente.

Característica	thenApply()	thenCompose()
Recibe el resultado anterior	Sí	Sí
La función devuelve	Un valor normal	Otro CompletableFuture
Combina futuros automáticamente	No necesita hacerlo	Sí, aplana el futuro resultante
Uso	Transformaciones normales	Encadenar operaciones asíncronas dependientes

Ejemplo:

java
CompletableFuture<Integer> numero =
CompletableFuture.supplyAsync(() -> 10);

// La función devuelve Integer.
CompletableFuture<Integer> resultado1 =
numero.thenApply(n -> n * 2);


Con thenCompose():

java
CompletableFuture<Integer> resultado2 =
numero.thenCompose(n ->
CompletableFuture.supplyAsync(() -> n * 2));


La diferencia importante:

java
// thenApply: si la función devuelve un Future,
// obtenemos un Future dentro de otro Future.
CompletableFuture<CompletableFuture<Integer>> anidado =
numero.thenApply(n ->
CompletableFuture.supplyAsync(() -> n * 2));

// thenCompose: aplana automáticamente el resultado.
CompletableFuture<Integer> compuesto =
numero.thenCompose(n ->
CompletableFuture.supplyAsync(() -> n * 2));


Recomendación:

thenApply() cuando la siguiente operación devuelve un valor normal.
thenCompose() cuando la siguiente operación devuelve otro CompletableFuture.
thenCombine() frente a allOf() frente a anyOf()

Los tres permiten trabajar con varias operaciones, pero resuelven problemas diferentes.

Método	¿Qué espera?	¿Qué devuelve?	¿Cuándo utilizarlo?
thenCombine()	Dos operaciones independientes.	Un resultado obtenido combinando ambos.	Cuando necesitamos unir dos resultados concretos.
allOf()	Todas las operaciones indicadas.	CompletableFuture<Void>.	Cuando necesitamos esperar a que terminen todas.
anyOf()	Cualquiera de las operaciones indicadas.	CompletableFuture<Object>.	Cuando nos interesa la primera que complete.

Ejemplo de thenCombine():

java
CompletableFuture<Integer> f1 =
CompletableFuture.supplyAsync(() -> 10);

CompletableFuture<Integer> f2 =
CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> resultado =
f1.thenCombine(f2, (a, b) -> a + b);


Ejemplo de allOf():

java
CompletableFuture.allOf(f1, f2).join();

System.out.println(f1.join());
System.out.println(f2.join());


Ejemplo de anyOf():

java
CompletableFuture<Object> primero =
CompletableFuture.anyOf(f1, f2);


Recomendación:

Dos resultados que queremos combinar: thenCombine().
Varias tareas que deben terminar todas: allOf().
Varias alternativas y nos interesa la primera que termine: anyOf().

Importante: allOf() no devuelve una lista de resultados. Hay que conservar los futuros originales y recuperar sus valores después.

6. Manejo de errores en CompletableFuture

Estos tres métodos se parecen, pero tienen finalidades distintas.

Método	¿Recibe resultado?	¿Recibe error?	¿Puede modificar el resultado?	Finalidad
exceptionally()	No	Sí	Sí	Recuperarse de un error proporcionando un valor alternativo.
handle()	Sí	Sí	Sí	Procesar tanto el resultado correcto como el error y generar un resultado final.
whenComplete()	Sí	Sí	No está pensado para transformarlo	Ejecutar acciones de seguimiento, registro o diagnóstico cuando termina.
exceptionally()

Se ejecuta cuando la cadena anterior termina excepcionalmente.

java
CompletableFuture<Integer> future =
CompletableFuture
.supplyAsync(() -> {
throw new RuntimeException("Error");
})
.exceptionally(error -> {
return 0;
});


Si hay un error, devolvemos un valor alternativo.

handle()

Se ejecuta tanto si hay resultado como si hay error.

java
CompletableFuture<Integer> future =
CompletableFuture
.supplyAsync(() -> 10)
.handle((resultado, error) -> {

                    if (error != null) {
                        return 0;
                    }

                    return resultado * 2;
                });

whenComplete()

Permite realizar acciones cuando termina la operación, sin cambiar voluntariamente su resultado.

java
CompletableFuture<Integer> future =
CompletableFuture
.supplyAsync(() -> 10)
.whenComplete((resultado, error) -> {

                    if (error != null) {
                        System.out.println("Error");
                    } else {
                        System.out.println(resultado);
                    }
                });


Recomendación:

Recuperar un error con un valor por defecto: exceptionally().
Gestionar de forma diferente el éxito y el error: handle().
Registrar qué ha ocurrido sin transformar el resultado: whenComplete().
7. Sincronización: synchronized frente a ReentrantLock

Ambos permiten proteger secciones críticas y evitar que varios hilos accedan simultáneamente a una sección protegida por el mismo bloqueo.

Característica	synchronized	ReentrantLock
Exclusión mutua	Sí	Sí
Bloqueo automático	Sí	No
Liberación automática al salir del bloque	Sí	No
tryLock()	No	Sí
tryLock() con timeout	No	Sí
lockInterruptibly()	No	Sí
Política de equidad configurable	No	Sí
Complejidad	Menor	Mayor

Ejemplo con synchronized:

java
public synchronized void incrementar() {
contador++;
}


Ejemplo con ReentrantLock:

java
private final ReentrantLock lock =
new ReentrantLock();

public void incrementar() {

    lock.lock();

    try {
        contador++;
    } finally {
        lock.unlock();
    }
}


¿Cuál utilizar?

synchronized: opción recomendada por defecto para proteger secciones críticas sencillas.
ReentrantLock: cuando necesitamos funcionalidades adicionales como tryLock(), interrupción durante la espera o equidad configurable.

No tiene sentido utilizar ReentrantLock simplemente por ser más avanzado. También exige liberar correctamente el bloqueo, normalmente mediante finally.

8. synchronized frente a AtomicInteger

Ambos pueden solucionar problemas de concurrencia, pero no sirven exactamente para lo mismo.

Característica	synchronized	AtomicInteger
Protege una sección crítica	Sí	No directamente
Incrementar un contador de forma segura	Sí	Sí
Operaciones atómicas	Mediante sincronización	Sí
Proteger varias variables conjuntamente	Sí	No por sí solo
Código para incrementar un contador	Más extenso	Muy sencillo
Uso habitual	Proteger operaciones sobre estado compartido	Contadores y operaciones atómicas individuales

Ejemplo:

java
// synchronized
private int contador = 0;

public synchronized void incrementar() {
contador++;
}

java
// AtomicInteger
private AtomicInteger contador =
new AtomicInteger(0);

public void incrementar() {
contador.incrementAndGet();
}


Recomendación:

Para un contador independiente: AtomicInteger.
Para modificar varias variables que deben permanecer coherentes entre sí: synchronized o ReentrantLock.

Importante: AtomicInteger no hace automáticamente atómica cualquier operación compuesta que realicemos sobre él.

9. wait()/notify() frente a BlockingQueue

Ambos permiten implementar comunicación entre hilos, especialmente el patrón productor-consumidor.

Característica	wait() / notify()	BlockingQueue
Comunicación entre hilos	Sí	Sí
Esperar si no se cumple una condición	Sí	Sí
Requiere synchronized	Sí	No
Gestión manual de condiciones	Sí	No
Gestión automática de la cola	No	Sí
put() y take()	No	Sí
Complejidad	Mayor	Menor

Ejemplo manual:

java
synchronized (this) {

    while (buffer.isEmpty()) {
        wait();
    }

    // Consumir elemento.
    notifyAll();
}


Ejemplo con BlockingQueue:

java
BlockingQueue<Integer> cola =
new ArrayBlockingQueue<>(5);

// Productor.
cola.put(10);

// Consumidor.
Integer valor = cola.take();


Recomendación: utilizar BlockingQueue siempre que el problema consista en intercambiar elementos mediante una cola entre productores y consumidores.

Utilizar wait()/notifyAll() cuando necesitemos implementar un mecanismo de coordinación personalizado que no se resuelva directamente con las herramientas disponibles.

ArrayBlockingQueue frente a LinkedBlockingQueue
Característica	ArrayBlockingQueue	LinkedBlockingQueue
Implementación	Array	Nodos enlazados
Capacidad	Fija y obligatoria	Opcional
Memoria	Reserva almacenamiento para su capacidad	Va creando nodos según los elementos
Inserción	put()	put()
Extracción	take()	take()
Uso	Colas con límite conocido	Colas que necesitan flexibilidad

Recomendación: utilizar ArrayBlockingQueue cuando queramos establecer un límite claro de elementos pendientes.

Esto también ayuda a evitar que el sistema acumule una cantidad excesiva de trabajo.

10. Semaphore frente a Lock

Ambos controlan el acceso a recursos, pero tienen objetivos diferentes.

Característica	Semaphore	ReentrantLock
Control de acceso	Mediante permisos	Mediante bloqueo
Número de accesos simultáneos	Puede permitir varios	Solo uno por bloqueo
Métodos principales	acquire() y release()	lock() y unlock()
Equidad configurable	Sí	Sí
Uso típico	Limitar recursos disponibles	Proteger una sección crítica

Ejemplo con Semaphore:

java
Semaphore hornos = new Semaphore(2);

hornos.acquire();

try {
cocinarPizza();
} finally {
hornos.release();
}


Ejemplo con ReentrantLock:

java
ReentrantLock lock = new ReentrantLock();

lock.lock();

try {
modificarDatos();
} finally {
lock.unlock();
}


Recomendación:

Semaphore: cuando tenemos un número limitado de recursos disponibles. Por ejemplo, 3 plazas de aparcamiento o 2 hornos.
ReentrantLock: cuando queremos que solamente un hilo entre en una sección crítica.
11. AtomicInteger frente a ConcurrentHashMap

No son alternativas directas, pero ambos pertenecen a las herramientas de concurrencia para trabajar con datos compartidos.

Característica	AtomicInteger	ConcurrentHashMap
Tipo de dato	Un entero	Un mapa de claves y valores
Acceso concurrente seguro	Sí	Sí
Operaciones atómicas	Sobre el valor individual	Ofrece operaciones atómicas específicas sobre el mapa
Uso típico	Contadores	Cachés, registros y datos compartidos

Ejemplo:

java
AtomicInteger contador =
new AtomicInteger(0);

contador.incrementAndGet();

java
ConcurrentHashMap<String, Integer> mapa =
new ConcurrentHashMap<>();

mapa.put("Juan", 25);
mapa.put("Ana", 30);


Recomendación:

AtomicInteger: para contadores y valores individuales.
ConcurrentHashMap: para almacenar y consultar información compartida entre varios hilos.

No debemos sustituir un HashMap normal por un ConcurrentHashMap automáticamente: solo tiene sentido cuando realmente necesitamos accesos concurrentes.

12. CountDownLatch frente a CyclicBarrier

Ambos sirven para coordinar varios hilos, pero funcionan de manera diferente.

Característica	CountDownLatch	CyclicBarrier
Finalidad	Esperar a que se completen N señales.	Esperar a que N hilos lleguen a un punto común.
Se puede reutilizar	No	Sí
Los hilos participantes deben esperar	No todos necesariamente	Sí
Operaciones principales	countDown() y await()	await()
Número inicial	Contador de señales	Número de participantes
Uso típico	Esperar a que terminen varias tareas	Sincronizar fases de varios hilos
CountDownLatch

Imaginemos que tenemos 5 trabajadores y el programa principal debe esperar a que todos terminen.

java
CountDownLatch latch =
new CountDownLatch(5);

// Cada trabajador, al terminar:
latch.countDown();

// El hilo principal:
latch.await();

CyclicBarrier

Imaginemos una carrera de 5 corredores que deben esperar a que todos estén preparados antes de empezar.

java
CyclicBarrier barrera =
new CyclicBarrier(5);

// Cada corredor:
barrera.await();


Recomendación:

CountDownLatch: cuando queremos esperar a que termine un conjunto de tareas.
CyclicBarrier: cuando varios hilos deben sincronizarse en un mismo punto, posiblemente en varias fases.
13. sleep() frente a wait()

Ambos pueden hacer que un hilo deje de ejecutar código durante un tiempo, pero tienen una diferencia fundamental.

Característica	Thread.sleep()	wait()
Detiene temporalmente el hilo	Sí	Sí
Necesita estar dentro de synchronized	No	Sí
Libera el monitor del objeto	No	Sí
Se puede despertar con notify()	No	Sí
Uso habitual	Simular tiempos de espera	Comunicación entre hilos

Ejemplo:

java
Thread.sleep(1000);


El hilo espera un segundo, pero conserva los monitores que tenga bloqueados.

Ejemplo:

java
synchronized (objeto) {
objeto.wait();
}


El hilo espera y libera el monitor de objeto, permitiendo que otro hilo pueda entrar en su sección sincronizada.

Recomendación:

sleep(): pausas temporales.
wait(): coordinación y comunicación entre hilos.
14. start() frente a run()
    Método	¿Crea un hilo nuevo?	¿Ejecuta la tarea?
    start()	Sí	Sí, en el nuevo hilo
    run()	No	Sí, en el hilo que realiza la llamada
    java
    Thread hilo = new Thread(() -> {
    System.out.println("Ejecutando");
    });

// Inicia otro hilo.
hilo.start();

java
// Ejecuta directamente en el hilo actual.
hilo.run();


Recomendación: si queremos concurrencia, utilizaremos start().

15. join() frente a CountDownLatch

Ambos permiten esperar a que otros hilos o tareas terminen.

Característica	join()	CountDownLatch
Esperar a un hilo	Sí	No directamente
Esperar a varios hilos	Varios join()	Un único await()
Necesita conocer los objetos Thread	Sí	No necesariamente
Reutilizable	Sí, para esperar a diferentes hilos	No
Uso habitual	Esperar a que termine un hilo concreto	Esperar a que finalice un conjunto de operaciones

Recomendación:

join(): cuando tenemos referencias a los hilos y queremos esperar a que terminen.
CountDownLatch: cuando varias tareas deben avisar de que han completado una fase.
16. interrupt() frente a cancel()

Ambos permiten solicitar que una tarea deje de ejecutarse, pero no funcionan exactamente igual.

Característica	Thread.interrupt()	Future.cancel(true)
Actúa sobre	Un hilo	Una tarea representada por un Future
Solicita interrupción	Sí	Sí, si se permite interrumpir la tarea
Detiene instantáneamente el código	No	No
Requiere cooperación de la tarea	Sí	Sí, para detener correctamente una tarea en ejecución
Uso habitual	Interrumpir un hilo concreto	Cancelar una tarea enviada a un executor

Recomendación:

interrupt(): cuando administramos directamente un hilo.
cancel(true): cuando trabajamos con Future y queremos cancelar la tarea correspondiente.

Ninguno garantiza que un código que ignore las interrupciones se detenga inmediatamente.

17. Asincronía, concurrencia y paralelismo

No son herramientas, sino conceptos que debemos diferenciar.

Concepto	Significado	Ejemplo
Asincronía	Podemos iniciar una operación y continuar sin esperar inmediatamente su resultado.	Lanzar una petición y procesar su respuesta posteriormente.
Concurrencia	Varias tareas progresan durante un mismo periodo de tiempo.	Dos tareas alternan su ejecución.
Paralelismo	Varias tareas se ejecutan realmente al mismo tiempo.	Dos tareas ejecutándose en núcleos diferentes.

Importante:

Una tarea asíncrona no tiene por qué ejecutarse en paralelo.
Tener varios hilos no garantiza que se ejecuten simultáneamente.
Lanzar más tareas no implica que el programa sea más rápido.
18. Tabla general: ¿Qué debería utilizar?

Esta es la tabla que conviene consultar cuando tengamos que diseñar un programa.

Necesidad	Herramienta recomendada	Alternativa
Ejecutar una tarea en otro hilo	ExecutorService	Thread
Definir una tarea sin resultado	Runnable	—
Definir una tarea con resultado	Callable<T>	CompletableFuture.supplyAsync()
Administrar un conjunto de hilos	ExecutorService	ThreadPoolExecutor
Obtener el resultado de una tarea sencilla	Future<T>	CompletableFuture<T>
Encadenar tareas asíncronas	CompletableFuture	—
Transformar un resultado	thenApply()	—
Consumir un resultado	thenAccept()	—
Ejecutar una acción posterior	thenRun()	—
Encadenar dos operaciones asíncronas dependientes	thenCompose()	—
Combinar dos resultados independientes	thenCombine()	allOf() si solo queremos esperar
Esperar a que terminen varias tareas	allOf()	CountDownLatch
Obtener la primera respuesta	anyOf()	—
Recuperar un error con un valor alternativo	exceptionally()	handle()
Procesar tanto éxito como error	handle()	—
Registrar el resultado o error al terminar	whenComplete()	—
Proteger una sección crítica sencilla	synchronized	ReentrantLock
Proteger un contador	AtomicInteger	synchronized
Limitar el acceso a recursos	Semaphore	—
Comunicar productores y consumidores	BlockingQueue	wait() / notifyAll()
Compartir un mapa entre hilos	ConcurrentHashMap	synchronized sobre un mapa
Esperar a que termine un hilo	join()	CountDownLatch
Esperar a que terminen N tareas	CountDownLatch	CompletableFuture.allOf()
Sincronizar varios hilos en un punto común	CyclicBarrier	Phaser
Pausar temporalmente un hilo	Thread.sleep()	—
Solicitar la interrupción de un hilo	interrupt()	Future.cancel(true)
Establecer un tiempo máximo para Future	get(timeout, unidad)	—
Establecer un timeout para CompletableFuture	orTimeout()	completeOnTimeout()
Cerrar normalmente un executor	shutdown()	—
19. Ejemplos prácticos: qué elegir en cada situación
    Caso 1. Tenemos 20 tareas que deben ejecutarse en segundo plano.

Utilizaría:

java
ExecutorService executor =
Executors.newFixedThreadPool(4);


No crearía 20 objetos Thread manualmente.

Caso 2. Necesitamos calcular el precio de un producto y devolverlo.

Utilizaría:

java
Callable<Double> calculo = () -> {
return 25.50 * 1.21;
};


O CompletableFuture.supplyAsync() si necesitamos continuar con otras operaciones asíncronas.

Caso 3. Tenemos que consultar usuario y pedidos de forma independiente.

Utilizaría:

java
CompletableFuture<Usuario> usuario = ...;

CompletableFuture<List<Pedido>> pedidos = ...;

CompletableFuture.allOf(usuario, pedidos).join();

Caso 4. Primero necesitamos obtener un usuario y después sus pedidos.

Utilizaría:

java
obtenerUsuarioAsync()
.thenCompose(usuario ->
obtenerPedidosAsync(usuario));

Caso 5. Tenemos una variable contador compartida.

Utilizaría:

java
AtomicInteger contador =
new AtomicInteger(0);

Caso 6. Tenemos tres hornos y diez cocineros.

Utilizaría:

java
Semaphore hornos = new Semaphore(3);

Caso 7. Tenemos varios productores y consumidores.

Utilizaría:

java
BlockingQueue<Pedido> pedidos =
new ArrayBlockingQueue<>(5);

Caso 8. Necesitamos esperar a que cinco trabajadores terminen de cargar datos.

Utilizaría:

java
CountDownLatch latch =
new CountDownLatch(5);

Caso 9. Tenemos cinco corredores que deben comenzar simultáneamente.

Utilizaría:

java
CyclicBarrier barrera =
new CyclicBarrier(5);

Caso 10. Tenemos que proteger una operación que modifica varios atributos relacionados.

Utilizaría:

java
synchronized (this) {
// Modificar los atributos.
}


Si necesitamos funcionalidades adicionales de bloqueo, estudiaría ReentrantLock.

20. Reglas para memorizar de cara al examen
    Thread vs Runnable: normalmente definimos tareas con Runnable y dejamos la gestión de hilos a un executor.
    Runnable vs Callable: Runnable no devuelve resultados; Callable sí.
    Future vs CompletableFuture: Future representa un resultado; CompletableFuture también permite componer operaciones.
    get() vs join(): ambos pueden bloquear; get() utiliza excepciones comprobadas.
    runAsync vs supplyAsync: sin resultado frente a con resultado.
    thenApply vs thenAccept vs thenRun: transformar, consumir y ejecutar una acción.
    thenApply vs thenCompose: valor normal frente a otro futuro.
    thenCombine vs allOf vs anyOf: combinar dos resultados, esperar a todos o esperar a cualquiera.
    exceptionally vs handle vs whenComplete: recuperar errores, procesar éxito/error o realizar seguimiento.
    synchronized vs ReentrantLock: bloqueo sencillo frente a bloqueo con funcionalidades adicionales.
    synchronized vs AtomicInteger: proteger operaciones sobre estado compartido frente a operaciones atómicas sobre un valor individual.
    Semaphore vs Lock: limitar varios accesos frente a exclusión mutua.
    wait/notify vs BlockingQueue: comunicación manual frente a una cola concurrente preparada.
    CountDownLatch vs CyclicBarrier: esperar a que terminen tareas frente a sincronizar participantes.
    sleep vs wait: pausa temporal frente a espera que libera el monitor.
    join vs CountDownLatch: esperar hilos concretos frente a coordinar un conjunto de tareas.
    shutdown vs shutdownNow: cierre ordenado frente a intento de cierre inmediato.
    Asincronía no significa paralelismo ni mayor velocidad.
21. Fuentes de referencia
    Documentación oficial de Java: java.util.concurrent
    Documentación oficial de Java: ExecutorService
    Documentación oficial de Java: CompletableFuture
    Documentación oficial de Java: ReentrantLock