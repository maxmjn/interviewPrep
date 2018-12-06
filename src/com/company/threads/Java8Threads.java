package com.company.threads;

import static java.lang.Thread.sleep;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

public class Java8Threads {
  //Concurrency is simulatenously task execution; 2 or more tasks overlap; they need not start/end at same time
  //due to CPU scheduling and pre-emption we get an illusion 2 or tasks are concurrent
  //Parallelism on other hand is made possible due to multi-core CPUs where truly each core executes tasks at the same time
  //Concurrency in Java is thru Threads - how are Threads created => Runnable - a functional interface defining a single void no-args method run()
  //http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
  //code to be executed by this thread, often called the task

  //Task(s) -- submit --> executorService -- uses --> ThreadPool
  //Executors --create--> ThreadPools [newFixedThreadPool(size), newScheduledThreadPool(size), newWorkStealingPool()]
  //newWorkStealingPool() no size specified because parallelism relies on number of CPU cores
  public static void main(String[] args) {

    Runnable task = () -> {
      String threadName = Thread.currentThread().getName();
      System.out.println("Hello " + threadName);
    };

    task.run();

    Thread thread = new Thread(task);
    thread.start();

    System.out.println("Done!");

    //java.util.concurrent Concurrency API introduces the concept of an ExecutorService as a higher level replacement for working with threads directly
    //ExecutorService has to shutdown explicitly
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.submit(task);

    shutdownExecutor(executor);

    //Any call to future.get() is a blocking call and wait until the underlying callable has been terminated.
    // In the worst case a callable runs forever - thus making your application unresponsive.
    // You can simply counteract those scenarios by passing a timeout
    Callable<Integer> callableTask = () -> {
      try {
        TimeUnit.SECONDS.sleep(2);
        return 123;
      }
      catch (InterruptedException e) {
        throw new IllegalStateException("task interrupted", e);
      }
    };
    executor = Executors.newFixedThreadPool(1);
    Future<Integer> future = executor.submit(callableTask);

    try {
      future.get(1, TimeUnit.SECONDS); //Exception because callable task sleeps 2secs
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
    shutdownExecutor(executor);

    //Parallelism - Instead of using a fixed size thread-pool ForkJoinPools are created for a given parallelism
    // size which per default is the number of available cores of the hosts CPU.
    executor = Executors.newWorkStealingPool(); //no size specified because parallelism relies on number of CPU cores
    List<Callable<String>> callables = Arrays.asList(
        () -> "task1",
        () -> "task2",
        () -> "task3");
    //Executors support batch submitting of multiple callables at once via invokeAll()
    try {
      executor.invokeAll(callables)
          .stream()
          .map(futures -> {
            try {
              return futures.get();
            }
            catch (Exception e) {
              throw new IllegalStateException(e);
            }
          })
          .forEach(System.out::println); //executor shutdown by stream's terminator; so no explicitly shutting down executor
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //Another way of batch-submitting callables is the method invokeAny().
    // Instead of returning future objects this method blocks until the first callable terminates and returns the result of that callable.
    executor = Executors.newWorkStealingPool();//no size specified because parallelism relies on number of CPU cores
    List<Callable<String>> callables2 = Arrays.asList(
        callable("task1", 2),
        callable("task2", 1),
        callable("task3", 3));

    String result = null;
    try {
      result = executor.invokeAny(callables2); //no need to explicitly shutdown executor
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    System.out.println(result);

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    Runnable task3 = () -> System.out.println("Scheduling scheduleAtFixedRate: " + System.nanoTime());
    int initialDelay = 0;
    int period = 1;
    scheduledExecutorService.scheduleAtFixedRate(task3, initialDelay, period, TimeUnit.SECONDS);
   //Please keep in mind that scheduleAtFixedRate() doesn't take into account the actual duration of the task.
   // So if you specify a period of one second but the task needs 2 seconds to be executed then the thread pool will keep working
    // until capacity is reached, finally ThreadPool rejected exceptions start showing up

    //scheduleWithFixedDelay() is handy if you cannot predict the duration of the scheduled tasks
    //initial delay is zero and the tasks duration is two seconds. So we end up with an execution interval of 0s, 3s, 6s, 9s and so on.
    Runnable task4 = () -> {
      try {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Scheduling scheduleWithFixedDelay: " + System.nanoTime());
      }
      catch (InterruptedException e) {
        System.err.println("task interrupted");
      }
    };
    scheduledExecutorService.scheduleWithFixedDelay(task4, 0, 1, TimeUnit.SECONDS);
    shutdownExecutor(scheduledExecutorService);//without shutdown executor keeps going on

    //Ok, now you have concurrency and parallelism; how to control access to a shared resource(s)
    //use "synchronized" keyword with methods or blocks
    //for synchronization to work each time a thread enters synchronized method/block a monitor lock is obtained by the thread and
    //no other thread can enter synchronized block/method until current thread gives-up/releases the lock
    //PESSIMISTIC locking - using synchronized you're always blocking other threads from entering; this hinders performance and throughput
    //OPTIMISTIC locking - try mutating/modifying a shared resource, if it was already changed by another Thread, try again. This can be
    // achieved using AtomicInteger/Long which use CAS[Compare And Swap] algorithm. You can also use StampedLock() for optimistic locking

    //All implicit monitors implement the reentrant characteristics.
    // Reentrant means that locks are bound to the current thread.
    // A thread can safely acquire the same lock multiple times without running into deadlocks (e.g. a synchronized method calls another synchronized method on the same object)
    //Instead of using implicit locking via the synchronized keyword the Concurrency API supports various explicit locks specified by the Lock interface.
    // Locks support various methods for finer grained lock control thus are more expressive than implicit monitors.
    executor = Executors.newFixedThreadPool(2);
    ReentrantLock lock = new ReentrantLock();

    executor.submit(() -> {
      lock.lock();
      try {
        sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock(); //It's important to wrap your code into a try/finally block to ensure unlocking in case of exceptions
      }
    });

    executor.submit(() -> {
      System.out.println("Locked: " + lock.isLocked());
      System.out.println("Held by me: " + lock.isHeldByCurrentThread());
      boolean locked = lock.tryLock();
      System.out.println("Lock acquired: " + locked);
    });

    shutdownExecutor(executor);

    //ReadWriteLock specifies another type of lock maintaining a pair of locks for read and write access.
    // The idea behind read-write locks is that it's usually safe to read mutable variables concurrently as long as
    // nobody is writing to this variable. So the read-lock can be held simultaneously by multiple threads as long as
    // no threads hold the write-lock. This can improve performance and throughput in case that reads are more frequent than writes.
    executor = Executors.newFixedThreadPool(2);
    Map<String, String> map = new HashMap<>();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    executor.submit(() -> {
      readWriteLock.writeLock().lock();
      try {
        sleep(1000);
        map.put("foo", "bar");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        readWriteLock.writeLock().unlock();
      }
    });
    Runnable readTask = () -> {
      readWriteLock.readLock().lock();
      try {
        System.out.println(map.get("foo"));
        sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        readWriteLock.readLock().unlock();
      }
    };
    //both read tasks have to wait the whole second until the write task has finished.
    // After the write lock has been released both read tasks are executed in parallel and print the result simultaneously to the console.
    // They don't have to wait for each other to finish because read-locks can safely be acquired concurrently as long as no write-lock is held by another thread
    executor.submit(readTask);
    executor.submit(readTask);
    shutdownExecutor(executor);

    //StampedLock return a stamp represented by a long value
    executor = Executors.newFixedThreadPool(2);
    Map<String, String> map2 = new HashMap<>();
    StampedLock stampedLock = new StampedLock();

    executor.submit(() -> {
      long stamp = stampedLock.writeLock();
      try {
        sleep(1);
        map2.put("foo", "bar");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        stampedLock.unlockWrite(stamp);
      }
    });

    Runnable readTask2= () -> {
      long stamp = stampedLock.readLock();
      try {
        System.out.println(map2.get("foo"));
        sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        stampedLock.unlockRead(stamp);
      }
    };
    //Obtaining a read or write lock via readLock() or writeLock() returns a stamp which is later
    // used for unlocking within the finally block.
    // KEEP IN MIND that stamped locks DON'T IMPLEMENT REENTRANT characteristics. Each call to lock returns a new stamp and blocks if no lock is available even if the same thread already holds a lock.
    // So you have to pay particular attention not to run into DEADLOCKS.
    //Just like in the previous ReadWriteLock example both read tasks have to wait until the write lock has been released
    executor.submit(readTask2);
    executor.submit(readTask2);
    shutdownExecutor(executor);

    //optimistic locking using StampedLock
    executor = Executors.newFixedThreadPool(2);

    executor.submit(() -> {
      long stamp = stampedLock.tryOptimisticRead();
      try {
        System.out.println("Optimistic Lock Valid: " + stampedLock.validate(stamp));
        sleep(1);
        System.out.println("Optimistic Lock Valid: " + stampedLock.validate(stamp));
        sleep(2);
        System.out.println("Optimistic Lock Valid: " + stampedLock.validate(stamp));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        stampedLock.unlock(stamp);
      }
    });
    executor.submit(() -> {
      long stamp = stampedLock.writeLock();
      try {
        System.out.println("Write Lock acquired");
        sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        stampedLock.unlock(stamp);
        System.out.println("Write done");
      }
    });
    shutdownExecutor(executor);

    //SEMAPHORE locks usually grant exclusive access to variables or resources, a semaphore is capable of maintaining whole sets of permits
    final ExecutorService executor1 = Executors.newFixedThreadPool(10);
    Semaphore semaphore = new Semaphore(5);
    Runnable longRunningTask = () -> {
      boolean permit = false;
      try {
        permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
        if (permit) {
          System.out.println("Semaphore acquired");
          sleep(5);
        } else {
          System.out.println("Could not acquire semaphore");
        }
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      } finally {
        if (permit) {
          semaphore.release();
        }
      }
    };
    IntStream.range(0, 10)
        .forEach(i -> executor1.submit(longRunningTask));//executor can potentially run 10 tasks concurrently but we use a semaphore of size 5, thus limiting concurrent access to 5
    shutdownExecutor(executor1);

  }

  static Callable<String> callable(String result, long sleepSeconds) {
    return () -> {
      TimeUnit.SECONDS.sleep(sleepSeconds);
      return result;
    };
  }

  private static void shutdownExecutor(ExecutorService executor) {
    try {
      System.out.println("attempt to shutdown executor");
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
    }
    catch (InterruptedException e) {
      System.err.println("tasks interrupted");
    }
    finally {
      if (!executor.isTerminated()) {
        System.err.println("cancel non-finished tasks");
      }
      executor.shutdownNow();
      System.out.println("shutdown finished");
    }
  }
}
