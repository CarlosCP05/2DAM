Te lo preparo como un documento Markdown (`PSP_Chuleta_Concurrencia_Java.md`), organizado por bloques y con una tabla principal de métodos, clases y opciones, incluyendo qué hacen, cómo se utilizan, cuándo conviene usarlos y sus diferencias.

Redacción

# PSP — Chuleta de concurrencia y programación asíncrona en Java

**Programación de Servicios y Procesos (DAM)**

---

# 1\. CREACIÓN Y GESTIÓN DE HILOS

## 1.1. Conceptos básicos

- **Proceso:** programa en ejecución con sus propios recursos y espacio de memoria.
- **Hilo (Thread):** unidad de ejecución dentro de un proceso. Varios hilos pueden compartir memoria.
- **Concurrencia:** varias tareas progresan durante un mismo periodo de tiempo.
- **Paralelismo:** varias tareas se ejecutan realmente al mismo tiempo.

## 1.2. Tabla de métodos y clases

| Elemento | Función | Información importante |
| --- | --- | --- |
| `Thread` | Representa un hilo de ejecución. | Podemos extender esta clase para crear un hilo. |
| `Runnable` | Define una tarea que no devuelve resultado. | Se implementa mediante `run()`. Permite separar la tarea del hilo. |
| `Callable<T>` | Define una tarea que devuelve un resultado. | Su método es `call()`. Puede lanzar excepciones comprobadas. |
| `start()` | Inicia un hilo nuevo. | La JVM ejecuta `run()` en otro hilo. |
| `run()` | Contiene el código que ejecutará el hilo. | Llamarlo directamente NO crea un hilo nuevo. |
| `sleep(ms)` | Pausa temporalmente el hilo actual. | Es estático. No libera los monitores que tenga bloqueados. |
| `join()` | Espera a que otro hilo termine. | Bloquea al hilo que llama a `join()`. |
| `interrupt()` | Solicita interrumpir un hilo. | No lo mata directamente: solicita una interrupción. |
| `isInterrupted()` | Consulta si un hilo está interrumpido. | No modifica el estado de interrupción. |
| `Thread.interrupted()` | Consulta y limpia la interrupción del hilo actual. | Es estático y reinicia el indicador. |
| `currentThread()` | Devuelve el hilo que se está ejecutando. | Permite consultar su nombre o estado. |
| `getName()` | Obtiene el nombre del hilo. | Útil para identificar tareas en consola. |
| `setName()` | Cambia el nombre del hilo. | Facilita la depuración. |
| `getState()` | Consulta el estado del hilo. | Devuelve un valor de `Thread.State`. |
| `isAlive()` | Comprueba si el hilo ha comenzado y no ha terminado. | Devuelve `true` mientras siga vivo. |
| `setDaemon(true)` | Marca un hilo como daemon. | No impide que termine el programa cuando los hilos normales hayan terminado. Debe establecerse antes de `start()`. |

## 1.3. Estados de un hilo

| Estado | Significado |
| --- | --- |
| `NEW` | Hilo creado, pero todavía no iniciado. |
| `RUNNABLE` | Hilo preparado o ejecutándose. |
| `BLOCKED` | Esperando conseguir un monitor para entrar en un bloque sincronizado. |
| `WAITING` | Esperando indefinidamente a que otro hilo realice una acción. |
| `TIMED_WAITING` | Esperando durante un tiempo limitado. |
| `TERMINATED` | Hilo que ya ha terminado. |

Ejemplos:

- `sleep()` → `TIMED_WAITING`.
- `wait()` → `WAITING` (sin timeout).
- `join()` → puede producir `WAITING` o `TIMED_WAITING`.
- Esperar un monitor `synchronized` → `BLOCKED`.

---

# 2\. SINCRONIZACIÓN Y CONCURRENCIA

## 2.1. Problemas de concurrencia

| Concepto | Explicación |
| --- | --- |
| Condición de carrera (Race Condition) | Dos o más hilos acceden a datos compartidos y el resultado depende del orden de ejecución. |
| Sección crítica | Parte del código que accede a un recurso compartido y necesita protección. |
| Exclusión mutua | Garantiza que solamente un hilo accede a una sección crítica protegida a la vez. |
| Deadlock | Dos o más hilos se quedan bloqueados esperando recursos que tienen otros hilos. |
| Starvation | Un hilo no consigue acceder a los recursos porque otros tienen prioridad continuamente. |
| Estado compartido | Información utilizada por varios hilos. Puede provocar problemas si es mutable. |

## 2.2. `synchronized`

Permite proteger secciones críticas mediante el monitor intrínseco de un objeto.

| Opción | Función | Información |
| --- | --- | --- |
| `synchronized` en un método de instancia | Sincroniza el método completo. | Utiliza como monitor `this`. |
| `synchronized` en un método estático | Sincroniza el método estático. | Utiliza el monitor del objeto `Class`. |
| `synchronized(objeto)` | Sincroniza un bloque de código. | Utiliza el objeto indicado como monitor. |
| `wait()` | Libera el monitor y espera una notificación. | Debe ejecutarse teniendo el monitor correspondiente. |
| `notify()` | Despierta a uno de los hilos que esperan en ese monitor. | No libera inmediatamente el monitor. |
| `notifyAll()` | Despierta a todos los hilos que esperan en ese monitor. | Después competirán por conseguir el monitor. |

### Ejemplo

java

```java
public synchronized void incrementar() {
    contador++;
}
```

Equivale conceptualmente a:

java

```java
public void incrementar() {
    synchronized (this) {
        contador++;
    }
}
```

### `wait()` y `notifyAll()`

Reglas importantes:

1. Deben utilizarse dentro de un contexto sincronizado sobre el mismo monitor.
2. `wait()` libera el monitor mientras espera.
3. `notify()` despierta un hilo.
4. `notifyAll()` despierta todos los hilos que esperan.
5. Es recomendable comprobar las condiciones con `while`, no con `if`, porque un hilo puede despertarse sin que la condición siga siendo válida.

Ejemplo:

java

```java
synchronized (this) {

    while (bufferVacio) {
        wait();
    }

    // Consumir elemento.
    notifyAll();
}
```

---

# 3\. LOCKS, SEMÁFOROS Y ATÓMICOS

Importación habitual:

java

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
```

## 3.1. Tabla principal

| Clase / Método | Función | Información importante |
| --- | --- | --- |
| `ReentrantLock` | Bloqueo explícito para proteger secciones críticas. | Alternativa a `synchronized`. |
| `lock()` | Adquiere el bloqueo. | Puede esperar indefinidamente. |
| `unlock()` | Libera el bloqueo. | Debe realizarse normalmente en `finally`. |
| `tryLock()` | Intenta adquirir el bloqueo sin esperar indefinidamente. | Devuelve `true` si lo consigue. |
| `tryLock(tiempo, unidad)` | Intenta conseguir el bloqueo durante un tiempo máximo. | Puede lanzar `InterruptedException`. |
| `lockInterruptibly()` | Adquiere el bloqueo permitiendo que la espera sea interrumpida. | Útil para cancelación cooperativa. |
| `isLocked()` | Comprueba si el bloqueo está adquirido por algún hilo. | No garantiza que siga bloqueado después de consultar. |
| `Semaphore(n)` | Crea un semáforo con `n` permisos. | Limita cuántos hilos pueden acceder simultáneamente a un recurso. |
| `acquire()` | Solicita un permiso. | Si no quedan permisos, el hilo espera. |
| `release()` | Devuelve un permiso. | Permite que otro hilo pueda continuar. |
| `availablePermits()` | Consulta los permisos disponibles. | Útil para mostrar el estado de un recurso. |
| `AtomicInteger` | Entero con operaciones atómicas. | Evita condiciones de carrera en operaciones individuales. |
| `AtomicLong` | Igual que `AtomicInteger`, pero con `long`. | Adecuado para contadores grandes. |
| `get()` | Obtiene el valor atómico. | Lectura segura. |
| `set(valor)` | Establece el valor. | Escritura segura. |
| `incrementAndGet()` | Incrementa y devuelve el nuevo valor. | Operación atómica. |
| `getAndIncrement()` | Devuelve el valor anterior y después incrementa. | Operación atómica. |
| `decrementAndGet()` | Decrementa y devuelve el nuevo valor. | Operación atómica. |
| `addAndGet(n)` | Suma `n` y devuelve el resultado. | Operación atómica. |
| `compareAndSet(a,b)` | Cambia el valor de `a` a `b` si el valor actual es `a`. | Operación atómica basada en CAS. |

## 3.2. Diferencias entre `synchronized` y `ReentrantLock`

| Característica | `synchronized` | `ReentrantLock` |
| --- | --- | --- |
| Bloqueo automático | Sí | No |
| Liberación automática | Sí, al salir del bloque | No, hay que llamar a `unlock()` |
| `tryLock()` | No | Sí |
| Tiempo máximo de espera | No directamente | Sí |
| Interrupción durante espera | No directamente | `lockInterruptibly()` |
| Equidad configurable | No | Sí, mediante `new ReentrantLock(true)` |
| Reentrante | Sí | Sí |

### Ejemplo de `ReentrantLock`

java

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();

try {
    contador++;
} finally {
    lock.unlock();
}
```

**Importante:** liberar siempre el bloqueo en `finally` para evitar dejar otros hilos bloqueados.

## 3.3. Semáforos

Ejemplo: aparcamiento con 3 plazas.

java

```java
Semaphore plazas = new Semaphore(3);

plazas.acquire();

try {
    System.out.println("Coche aparcado");
    Thread.sleep(2000);
} finally {
    plazas.release();
}
```

Un semáforo limita el número de hilos que pueden utilizar simultáneamente un recurso.

---

# 4\. COLECCIONES CONCURRENTES Y COMUNICACIÓN

## 4.1. Colecciones concurrentes

| Clase / Método | Función | Información importante |
| --- | --- | --- |
| `BlockingQueue<T>` | Cola segura para varios hilos. | Proporciona operaciones bloqueantes. |
| `ArrayBlockingQueue<T>` | Cola de capacidad fija. | Se implementa mediante un array. |
| `LinkedBlockingQueue<T>` | Cola basada en nodos enlazados. | Puede tener capacidad limitada o prácticamente ilimitada si no se indica. |
| `put(elemento)` | Introduce un elemento en la cola. | Espera si la cola está llena. |
| `take()` | Extrae el siguiente elemento. | Espera si la cola está vacía. |
| `offer(elemento)` | Intenta introducir un elemento. | Devuelve `false` si no puede introducirlo inmediatamente. |
| `offer(elemento, tiempo, unidad)` | Intenta introducir un elemento durante un tiempo. | Puede esperar hasta el timeout. |
| `poll()` | Intenta extraer un elemento. | Devuelve `null` si está vacía. |
| `poll(tiempo, unidad)` | Intenta extraer durante un tiempo máximo. | Devuelve `null` si no consigue ningún elemento. |
| `size()` | Consulta el número de elementos. | Es una observación puntual, no una garantía para operaciones posteriores. |
| `ConcurrentHashMap<K,V>` | Mapa seguro para acceso concurrente. | Permite lecturas y actualizaciones desde varios hilos. |
| `put(clave, valor)` | Introduce o sustituye un valor. | Operación segura. |
| `get(clave)` | Obtiene un valor. | Lectura concurrente. |
| `putIfAbsent(clave, valor)` | Inserta solamente si no existe la clave. | Útil para evitar sobrescrituras. |
| `computeIfAbsent(clave, función)` | Calcula un valor si la clave no existe. | Muy útil para inicializaciones compartidas. |

## 4.2. Diferencias entre las colas

| Característica | `ArrayBlockingQueue` | `LinkedBlockingQueue` |
| --- | --- | --- |
| Implementación | Array | Nodos enlazados |
| Capacidad | Siempre fija | Configurable |
| Memoria | Array reservado | Nodos creados dinámicamente |
| Uso | Buffers de tamaño conocido | Colas de capacidad flexible |
| Bloqueo | Sí | Sí |

**Consejo:** para productor-consumidor, `BlockingQueue` suele ser más sencilla y segura que implementar manualmente `wait()` y `notify()`.

---

# 5\. POOLS DE HILOS Y EXECUTORS

## 5.1. `ExecutorService`

Permite gestionar un conjunto de hilos reutilizables sin crearlos manualmente para cada tarea.

| Método / Clase | Función | Información importante |
| --- | --- | --- |
| `Executor` | Interfaz básica para ejecutar tareas. | Define `execute()`. |
| `ExecutorService` | Interfaz para gestionar tareas y el ciclo de vida del executor. | Permite enviar tareas y cerrarlo. |
| `execute(Runnable)` | Envía una tarea sin devolver un resultado. | No devuelve `Future`. |
| `submit(Runnable)` | Envía una tarea. | Devuelve un `Future<?>`. |
| `submit(Callable<T>)` | Envía una tarea que devuelve resultado. | Devuelve `Future<T>`. |
| `shutdown()` | Deja de aceptar tareas nuevas. | Permite que las tareas enviadas terminen. |
| `shutdownNow()` | Intenta detener las tareas en ejecución. | Solicita interrupciones y devuelve tareas pendientes. |
| `isShutdown()` | Comprueba si se ha solicitado el cierre. | No significa que haya terminado. |
| `isTerminated()` | Comprueba si han terminado todas las tareas. | Requiere que se haya solicitado el cierre. |
| `awaitTermination(tiempo, unidad)` | Espera a que termine el executor. | Devuelve `true` si termina dentro del tiempo. |

## 5.2. Tipos de pools

| Método | Funcionamiento | Cuándo utilizarlo |
| --- | --- | --- |
| `newFixedThreadPool(n)` | Pool con un número fijo de hilos. | Cuando queremos controlar el máximo de hilos. |
| `newCachedThreadPool()` | Crea y reutiliza hilos según la demanda. | Tareas cortas y asíncronas, teniendo en cuenta que puede crecer mucho. |
| `newSingleThreadExecutor()` | Utiliza un único hilo. | Cuando queremos ejecutar tareas secuencialmente. |
| `newWorkStealingPool()` | Pool que distribuye tareas mediante work stealing. | Cargas paralelas que pueden dividirse en tareas. |

## 5.3. Ejemplo de pool

java

```java
ExecutorService executor =
        Executors.newFixedThreadPool(3);

for (int i = 1; i <= 10; i++) {

    int numero = i;

    executor.submit(() -> {
        System.out.println(
            "Tarea " + numero + " en "
            + Thread.currentThread().getName()
        );
    });
}

executor.shutdown();
```

**Importante:** el número de tareas no es el número de hilos. Podemos enviar 100 tareas a un pool de 3 hilos.

---

# 6\. FUTURE Y CALLABLE

## 6.1. `Callable`

`Callable<T>` representa una tarea que puede devolver un resultado y lanzar excepciones comprobadas.

java

```java
Callable<Integer> tarea = () -> {
    Thread.sleep(1000);
    return 42;
};
```

## 6.2. `Future<T>`

Representa el resultado futuro de una tarea.

| Método | Función | Información importante |
| --- | --- | --- |
| `get()` | Obtiene el resultado. | Bloquea hasta que esté disponible. |
| `get(tiempo, unidad)` | Espera un tiempo máximo. | Puede lanzar `TimeoutException`. |
| `isDone()` | Comprueba si terminó la tarea. | También puede ser `true` si terminó por error o cancelación. |
| `cancel(true)` | Intenta cancelar la tarea. | Permite solicitar interrupción del hilo. |
| `cancel(false)` | Intenta cancelar sin interrumpir. | Si ya se está ejecutando, no la detiene. |
| `isCancelled()` | Comprueba si fue cancelada. | Devuelve un booleano. |

## 6.3. Excepciones de `Future.get()`

| Excepción | Motivo |
| --- | --- |
| `InterruptedException` | El hilo que espera ha sido interrumpido. |
| `ExecutionException` | La tarea ha terminado lanzando una excepción. |
| `TimeoutException` | Se ha agotado el tiempo máximo de espera. |

## 6.4. Diferencia entre `Runnable` y `Callable`

| Característica | Runnable | Callable |
| --- | --- | --- |
| Método | `run()` | `call()` |
| Devuelve resultado | No | Sí, tipo `T` |
| Excepciones comprobadas | No puede declararlas | Puede declararlas |
| Uso habitual | `execute()` o `submit()` | `submit()` |

---

# 7\. COMPLETABLEFUTURE

## 7.1. ¿Qué es?

`CompletableFuture<T>` representa un resultado futuro y permite encadenar, combinar y gestionar tareas asíncronas sin tener que llamar a `get()` entre cada paso.

Permite construir flujos como:

text

```text
Tarea A
   ↓
Tarea B
   ↓
Tarea C
```

Importación:

java

```java
import java.util.concurrent.CompletableFuture;
```

## 7.2. Crear tareas asíncronas

| Método | Función | Resultado |
| --- | --- | --- |
| `runAsync()` | Ejecuta una tarea asíncrona sin resultado. | `CompletableFuture<Void>` |
| `supplyAsync()` | Ejecuta una tarea asíncrona con resultado. | `CompletableFuture<T>` |
| `supplyAsync(tarea, executor)` | Ejecuta utilizando un executor personalizado. | `CompletableFuture<T>` |
| `runAsync(tarea, executor)` | Ejecuta sin resultado utilizando un executor propio. | `CompletableFuture<Void>` |
| `completedFuture(valor)` | Crea un CompletableFuture ya completado. | Devuelve el valor indicado. |
| `failedFuture(error)` | Crea un CompletableFuture completado excepcionalmente. | Representa un error. |

### Diferencia fundamental

java

```java
// Sin resultado
CompletableFuture<Void> f1 =
        CompletableFuture.runAsync(() -> tarea());

// Con resultado
CompletableFuture<Integer> f2 =
        CompletableFuture.supplyAsync(() -> calcular());
```

**Importante:** `supplyAsync()` debe devolver un resultado. Si el lambda solamente lanza una excepción, puede ser necesario indicar el tipo explícitamente:

java

```java
CompletableFuture.<Integer>supplyAsync(() -> {
    throw new RuntimeException("Error");
});
```

## 7.3. Obtener resultados

| Método | Función | Diferencias |
| --- | --- | --- |
| `get()` | Espera y obtiene el resultado. | Puede lanzar excepciones comprobadas. |
| `join()` | Espera y obtiene el resultado. | Utiliza excepciones no comprobadas. |
| `getNow(valor)` | Obtiene el resultado si ya está disponible. | Si no ha terminado, devuelve el valor alternativo. |
| `isDone()` | Comprueba si ha terminado. | No bloquea. |
| `isCompletedExceptionally()` | Comprueba si ha terminado con error. | No bloquea. |
| `isCancelled()` | Comprueba si está cancelado. | Consulta su estado. |
| `cancel(true)` | Intenta cancelar el future. | No garantiza detener instantáneamente el código en ejecución. |

**Recuerda:** `join()` también bloquea. Que utilicemos `CompletableFuture` no significa que todas las operaciones sean automáticamente no bloqueantes.

---

# 8\. OPERACIONES DE ENCADENAMIENTO

Esta es una de las partes más importantes de `CompletableFuture`.

## 8.1. Tabla principal

| Método | Recibe resultado anterior | Devuelve resultado nuevo | Función |
| --- | --- | --- | --- |
| `thenApply()` | Sí | Sí | Transforma el resultado. |
| `thenAccept()` | Sí | No | Consume el resultado mediante una acción. |
| `thenRun()` | No | No | Ejecuta una acción cuando termina la anterior. |
| `thenCompose()` | Sí | Sí | Encadena otra operación asíncrona y aplana el resultado. |
| `thenCombine()` | Sí, de dos futures | Sí | Combina los resultados de dos tareas independientes. |
| `thenAcceptBoth()` | Sí, de dos futures | No | Consume conjuntamente los resultados de dos tareas. |
| `runAfterBoth()` | No | No | Ejecuta una acción cuando terminan dos futures. |
| `applyToEither()` | Sí | Sí | Utiliza el resultado de la primera de dos tareas que termina normalmente. |
| `acceptEither()` | Sí | No | Consume el resultado de la primera que termina normalmente. |
| `runAfterEither()` | No | No | Ejecuta una acción cuando termina cualquiera de los dos futures. |

## 8.2. `thenApply()`

Transforma el resultado.

java

```java
CompletableFuture<Integer> resultado =
        CompletableFuture
            .supplyAsync(() -> 10)
            .thenApply(n -> n * 2);
```

Resultado: `20`.

## 8.3. `thenAccept()`

Recibe el resultado, pero no devuelve otro.

java

```java
CompletableFuture
    .supplyAsync(() -> 10)
    .thenAccept(n ->
        System.out.println(n)
    );
```

## 8.4. `thenRun()`

No recibe el resultado anterior.

java

```java
CompletableFuture
    .supplyAsync(() -> 10)
    .thenRun(() ->
        System.out.println("Tarea terminada")
    );
```

## 8.5. `thenCompose()`

Se utiliza cuando la siguiente operación también devuelve un `CompletableFuture`.

java

```java
CompletableFuture<String> resultado =
        CompletableFuture
            .supplyAsync(() -> "Juan")
            .thenCompose(nombre ->
                CompletableFuture.supplyAsync(
                    () -> "Hola " + nombre
                )
            );
```

Evita obtener un resultado anidado:

text

```text
Con thenApply:

CompletableFuture<CompletableFuture<String>>

Con thenCompose:

CompletableFuture<String>
```

## 8.6. `thenCombine()`

Combina dos operaciones independientes.

java

```java
CompletableFuture<Integer> f1 =
        CompletableFuture.supplyAsync(() -> 10);

CompletableFuture<Integer> f2 =
        CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> resultado =
        f1.thenCombine(f2, (a, b) -> a + b);
```

Resultado: `30`.

### Diferencia entre los tres métodos principales

| Método | Cuándo utilizarlo |
| --- | --- |
| `thenApply()` | Tengo un resultado y quiero transformarlo. |
| `thenAccept()` | Tengo un resultado y quiero utilizarlo sin devolver otro. |
| `thenRun()` | Solo quiero ejecutar una acción después. |
| `thenCompose()` | La siguiente tarea devuelve otro CompletableFuture. |
| `thenCombine()` | Tengo dos tareas independientes y quiero combinar sus resultados. |

---

# 9\. COMBINACIÓN DE VARIAS TAREAS

## 9.1. Tabla principal

| Método | Función | Resultado |
| --- | --- | --- |
| `allOf(f1, f2, ...)` | Espera a que terminen todos los futures. | `CompletableFuture<Void>` |
| `anyOf(f1, f2, ...)` | Termina cuando termina cualquiera de los futures. | `CompletableFuture<Object>` |
| `thenCombine()` | Combina dos resultados. | Un nuevo CompletableFuture del tipo indicado. |
| `thenAcceptBoth()` | Consume los dos resultados. | `CompletableFuture<Void>` |

## 9.2. `allOf()`

Se utiliza cuando necesitamos esperar a que terminen todas las tareas.

java

```java
CompletableFuture<Integer> f1 =
        CompletableFuture.supplyAsync(() -> 10);

CompletableFuture<Integer> f2 =
        CompletableFuture.supplyAsync(() -> 20);

CompletableFuture<Integer> f3 =
        CompletableFuture.supplyAsync(() -> 30);

CompletableFuture.allOf(f1, f2, f3).join();

List<Integer> resultados = List.of(
        f1.join(),
        f2.join(),
        f3.join()
);
```

**Importante:** `allOf()` no devuelve automáticamente una lista de resultados.

## 9.3. `anyOf()`

Termina cuando termina cualquiera de las tareas.

java

```java
CompletableFuture<Object> primero =
        CompletableFuture.anyOf(f1, f2, f3);
```

Útil cuando nos interesa la primera respuesta disponible.

---

# 10\. MANEJO DE ERRORES

Las tareas asíncronas pueden terminar correctamente o excepcionalmente.

## 10.1. Tabla principal

| Método | Función | Información importante |
| --- | --- | --- |
| `exceptionally()` | Recupera un error proporcionando un valor alternativo. | Recibe la excepción y devuelve un resultado del mismo tipo. |
| `handle()` | Procesa tanto el resultado como el error. | Recibe `(resultado, error)` y devuelve un nuevo resultado. |
| `whenComplete()` | Ejecuta una acción cuando termina la tarea. | Recibe resultado y error. Normalmente no transforma el resultado. |
| `completeExceptionally()` | Completa manualmente el future con una excepción. | Útil para comunicar un error. |
| `complete(valor)` | Completa manualmente el future con un resultado. | Devuelve `true` si consigue completarlo. |

## 10.2. `exceptionally()`

Si ocurre un error, permite proporcionar un valor alternativo.

java

```java
CompletableFuture<Integer> future =
        CompletableFuture.<Integer>supplyAsync(() -> {

            throw new RuntimeException("Error");

        }).exceptionally(error -> {

            System.out.println("Ha ocurrido un error");

            return 0;
        });
```

Si la tarea falla, el resultado final será `0`.

## 10.3. `handle()`

Permite tratar tanto el resultado como el error.

java

```java
CompletableFuture<Integer> future =
        CompletableFuture
            .supplyAsync(() -> 10)
            .handle((resultado, error) -> {

                if (error != null) {
                    return 0;
                }

                return resultado * 2;
            });
```

## 10.4. `whenComplete()`

Ejecuta una acción al finalizar.

java

```java
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
```

## 10.5. Diferencias

| Método | Recupera el error | Transforma el resultado |
| --- | --- | --- |
| `exceptionally()` | Sí | Sí, proporcionando un valor alternativo. |
| `handle()` | Sí | Sí |
| `whenComplete()` | No, por sí solo | No, normalmente conserva el resultado original. |

---

# 11\. TIMEOUTS Y CANCELACIÓN

## 11.1. Tabla de métodos

| Método | Función | Información |
| --- | --- | --- |
| `orTimeout(tiempo, unidad)` | Establece un tiempo máximo para completar el future. | Si vence, termina excepcionalmente con `TimeoutException`. |
| `completeOnTimeout(valor, tiempo, unidad)` | Establece un valor alternativo si vence el tiempo. | Completa normalmente con el valor indicado. |
| `cancel(true)` | Intenta cancelar la tarea. | No garantiza detener inmediatamente el código que ya se ejecuta. |
| `isCancelled()` | Comprueba si está cancelada. | Devuelve un booleano. |

## 11.2. `orTimeout()`

java

```java
CompletableFuture<Integer> future =
        CompletableFuture
            .supplyAsync(() -> tareaLenta())
            .orTimeout(3, TimeUnit.SECONDS);
```

Si pasan 3 segundos sin completarse, se produce un timeout.

## 11.3. `completeOnTimeout()`

java

```java
CompletableFuture<Integer> future =
        CompletableFuture
            .supplyAsync(() -> tareaLenta())
            .completeOnTimeout(
                0,
                3,
                TimeUnit.SECONDS
            );
```

Si vence el tiempo, se devuelve `0`.

## 11.4. Diferencia

| Método | Si se supera el tiempo |
| --- | --- |
| `orTimeout()` | Termina excepcionalmente. |
| `completeOnTimeout()` | Devuelve el valor alternativo. |

---

# 12\. EXECUTORS PERSONALIZADOS

Por defecto, las operaciones `supplyAsync()` y `runAsync()` utilizan normalmente el executor común de `ForkJoinPool`.

Podemos proporcionar uno propio.

java

```java
ExecutorService executor =
        Executors.newFixedThreadPool(4);

CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(
            () -> calcular(),
            executor
        );
```

| Concepto | Explicación |
| --- | --- |
| `ForkJoinPool.commonPool()` | Pool común utilizado por defecto en muchas operaciones asíncronas. |
| `Executor` | Interfaz para ejecutar tareas. |
| `ExecutorService` | Gestiona ejecución y ciclo de vida de los hilos. |
| `newFixedThreadPool(n)` | Pool con un número fijo de hilos. |
| Executor de CPU | Adecuado para tareas que consumen procesador. |
| Executor de E/S | Puede utilizar más hilos para tareas que pasan tiempo esperando operaciones externas. |

**Importante:** no todas las operaciones de una cadena tienen por qué ejecutarse en el mismo executor. Las variantes asíncronas que aceptan un executor permiten especificarlo.

---

# 13\. SINCRONIZADORES DE ALTO NIVEL

## 13.1. Tabla principal

| Clase / Método | Función | Información importante |
| --- | --- | --- |
| `CountDownLatch(n)` | Espera a que un contador llegue a cero. | No se puede reiniciar. |
| `countDown()` | Reduce el contador en uno. | Cuando llega a cero, libera a los hilos que esperan. |
| `await()` | Espera a que el contador llegue a cero. | Bloquea el hilo que llama. |
| `CyclicBarrier(n)` | Hace que varios hilos esperen en un punto común. | Puede reutilizarse en varias fases. |
| `await()` | Espera a que lleguen todos los participantes. | Cuando llegan, continúan todos. |
| `Semaphore(n)` | Limita el número de accesos simultáneos. | Se basa en permisos. |
| `acquire()` | Solicita un permiso. | Puede bloquearse. |
| `release()` | Devuelve un permiso. | Permite continuar a otros hilos. |

## 13.2. Diferencias entre `CountDownLatch` y `CyclicBarrier`

| Característica | CountDownLatch | CyclicBarrier |
| --- | --- | --- |
| Objetivo | Esperar a que terminen varias tareas. | Esperar a que varios hilos lleguen al mismo punto. |
| Reiniciable | No | Sí |
| Participantes | No tienen que ser los mismos hilos que esperan. | Los participantes esperan en la barrera. |
| Uso habitual | Esperar a que carguen varios servicios. | Carrera de corredores o fases sincronizadas. |

---

# 14\. COMPARATIVA GENERAL: ¿QUÉ UTILIZAR?

| Necesidad | Herramienta recomendada |
| --- | --- |
| Crear un hilo sencillo | `Thread` |
| Definir una tarea independiente | `Runnable` |
| Ejecutar una tarea que devuelve un resultado | `Callable<T>` |
| Gestionar varios hilos | `ExecutorService` |
| Esperar a que termine un hilo | `join()` |
| Evitar acceso simultáneo a una sección crítica | `synchronized` o `ReentrantLock` |
| Limitar accesos simultáneos a un recurso | `Semaphore` |
| Proteger un contador sencillo | `AtomicInteger` |
| Comunicar productores y consumidores | `BlockingQueue` |
| Compartir un mapa entre hilos | `ConcurrentHashMap` |
| Esperar a varios trabajadores | `CountDownLatch` |
| Sincronizar varios hilos en un punto | `CyclicBarrier` |
| Representar el resultado futuro de una tarea | `Future<T>` |
| Encadenar operaciones asíncronas | `CompletableFuture` |
| Combinar tareas independientes | `thenCombine()` o `allOf()` |
| Recuperarse de errores asíncronos | `exceptionally()` o `handle()` |
| Establecer tiempos máximos | `get(timeout)`, `orTimeout()` |
| Delegar tareas asíncronas en Spring | `@Async` (fuera del temario de estos apuntes) |

---

# 15\. DIFERENCIAS FUNDAMENTALES QUE DEBES RECORDAR

| Comparación | Diferencia |
| --- | --- |
| `start()` vs `run()` | `start()` crea un hilo nuevo; `run()` ejecuta el método en el hilo actual si se llama directamente. |
| `Runnable` vs `Callable` | Runnable no devuelve resultado; Callable sí. |
| `sleep()` vs `wait()` | `sleep()` pausa sin liberar monitores; `wait()` libera el monitor correspondiente. |
| `synchronized` vs `ReentrantLock` | Ambos protegen secciones críticas; ReentrantLock ofrece operaciones adicionales. |
| `AtomicInteger` vs `synchronized` | AtomicInteger facilita operaciones atómicas individuales; synchronized protege bloques de código más generales. |
| `BlockingQueue` vs `wait()/notify()` | BlockingQueue simplifica la comunicación productor-consumidor. |
| `execute()` vs `submit()` | execute no devuelve Future; submit sí. |
| `shutdown()` vs `shutdownNow()` | shutdown permite finalizar tareas pendientes; shutdownNow intenta interrumpir las que se ejecutan. |
| `Future.get()` vs `CompletableFuture.join()` | Ambos esperan; get utiliza excepciones comprobadas y join excepciones no comprobadas. |
| `thenApply()` vs `thenCompose()` | thenApply transforma; thenCompose encadena otra operación asíncrona. |
| `thenCombine()` vs `allOf()` | thenCombine combina dos resultados; allOf espera varios futures sin devolver sus resultados. |
| `allOf()` vs `anyOf()` | allOf espera a todos; anyOf termina cuando termina cualquiera. |
| `exceptionally()` vs `handle()` | exceptionally recupera errores; handle procesa tanto resultados como errores. |
| `orTimeout()` vs `completeOnTimeout()` | El primero produce error; el segundo proporciona un valor alternativo. |
| Asincronía vs paralelismo | Asincronía permite continuar sin esperar inmediatamente; paralelismo implica ejecución simultánea real. |

---

# 16\. REGLAS PRÁCTICAS PARA LOS EXÁMENES

1. Utiliza `start()` para iniciar un hilo, no `run()`.
2. Recuerda que `join()` bloquea hasta que termina el hilo.
3. Utiliza `interrupt()` como mecanismo de interrupción cooperativa.
4. Protege los recursos compartidos frente a condiciones de carrera.
5. Utiliza `while` para comprobar las condiciones antes de `wait()`.
6. Libera siempre los `ReentrantLock` en un `finally`.
7. Devuelve los permisos de los semáforos en un `finally`.
8. Prefiere `BlockingQueue` antes que implementar manualmente un productor-consumidor cuando sea suficiente.
9. Cierra correctamente los `ExecutorService`.
10. Recuerda que `Future.get()` bloquea.
11. No confundas `runAsync()` con `supplyAsync()`.
12. No confundas `thenApply()` con `thenCompose()`.
13. `allOf()` no devuelve una lista de resultados.
14. `join()` también bloquea.
15. Gestiona los errores de las tareas asíncronas.
16. Utiliza timeouts cuando no quieras esperar indefinidamente.
17. Evita compartir estado mutable cuando sea posible.
18. Un executor controla los recursos de ejecución: no crea hilos infinitos.
19. Asíncrono no significa automáticamente más rápido.
20. No olvides que cancelar una tarea no garantiza detener instantáneamente su ejecución.

---

# 17\. CHULETA RÁPIDA DE CÓDIGO

## Hilos

java

```java
Thread hilo = new Thread(() -> tarea());
hilo.start();

hilo.join();

Thread.sleep(1000);

hilo.interrupt();
```

## Sincronización

java

```java
public synchronized void metodo() {
    contador++;
}
```

java

```java
synchronized (objeto) {
    // Sección crítica
}
```

## Locks

java

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();

try {
    // Sección crítica
} finally {
    lock.unlock();
}
```

## Semáforos

java

```java
Semaphore semaforo = new Semaphore(3);

semaforo.acquire();

try {
    // Utilizar recurso
} finally {
    semaforo.release();
}
```

## Atómicos

java

```java
AtomicInteger contador = new AtomicInteger(0);

contador.incrementAndGet();
contador.get();
```

## Colas concurrentes

java

```java
BlockingQueue<Integer> cola =
        new ArrayBlockingQueue<>(5);

cola.put(10);

Integer numero = cola.take();
```

## ExecutorService

java

```java
ExecutorService executor =
        Executors.newFixedThreadPool(3);

executor.submit(() -> tarea());

executor.shutdown();
```

## Future

java

```java
Future<Integer> future =
        executor.submit(() -> 10);

Integer resultado = future.get();
```

## CompletableFuture

java

```java
// Sin resultado
CompletableFuture.runAsync(() -> tarea());

// Con resultado
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> 10);

// Transformar
future.thenApply(n -> n * 2);

// Consumir
future.thenAccept(n -> System.out.println(n));

// Acción posterior
future.thenRun(() -> finalizar());

// Encadenar operaciones asíncronas
future.thenCompose(n -> otraTareaAsync(n));

// Combinar resultados
future1.thenCombine(
        future2,
        (a, b) -> a + b
);

// Esperar a todas
CompletableFuture.allOf(f1, f2, f3);

// Esperar a cualquiera
CompletableFuture.anyOf(f1, f2, f3);

// Recuperar errores
future.exceptionally(error -> 0);

// Procesar resultado o error
future.handle((resultado, error) -> {
    if (error != null) {
        return 0;
    }
    return resultado;
});

// Ejecutar acción al terminar
future.whenComplete((resultado, error) -> {
    System.out.println("Finalizado");
});

// Timeout con error
future.orTimeout(3, TimeUnit.SECONDS);

// Timeout con valor alternativo
future.completeOnTimeout(
    0,
    3,
    TimeUnit.SECONDS
);

// Obtener resultado bloqueando
future.join();
```

---

# 18\. IDEA FINAL

La programación concurrente consiste en saber gestionar varios hilos que comparten recursos.

La programación asíncrona permite organizar tareas y sus resultados sin tener que esperar inmediatamente a cada una.

La evolución de las herramientas de Java es:

text

```text
Thread / Runnable
       ↓
synchronized / wait / notify
       ↓
Locks / Semaphores / Atomics
       ↓
BlockingQueue / ConcurrentHashMap
       ↓
ExecutorService / Callable
       ↓
Future
       ↓
CompletableFuture
       ↓
Composición de tareas asíncronas
```

**Lo más importante para resolver ejercicios es identificar qué necesitas:**

- ¿Crear hilos? → `Thread` / `Runnable`.
- ¿Proteger datos? → `synchronized`, `ReentrantLock` o clases atómicas.
- ¿Limitar recursos? → `Semaphore`.
- ¿Comunicar hilos? → `BlockingQueue`.
- ¿Gestionar trabajadores? → `ExecutorService`.
- ¿Recoger resultados? → `Callable` / `Future`.
- ¿Encadenar tareas? → `CompletableFuture`.
- ¿Combinar tareas? → `thenCombine()` / `allOf()`.
- ¿Gestionar fallos? → `exceptionally()` / `handle()`.
- ¿Controlar esperas? → timeouts y cancelación.

**La clave no es memorizar todos los métodos, sino saber elegir la herramienta adecuada para cada problema de concurrencia.**