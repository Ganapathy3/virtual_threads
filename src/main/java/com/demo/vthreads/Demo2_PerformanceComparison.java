package com.demo.vthreads;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DEMO 2 — Performance: Platform Threads vs Virtual Threads
 *
 *   We simulate 1000 waiters (tasks).
 *   Each waiter has to "wait" for food (Thread.sleep = I/O wait).
 *
 *   Platform threads: 1000 full-time waiters — your restaurant runs out of space!
 *   Virtual threads:  1000 magic waiters   — they park while waiting, no problem!
 *
 * Watch the TIME difference!
 */
public class Demo2_PerformanceComparison {

    // Simulates a slow task (like a DB query or HTTP call) that takes 100ms
    private static void simulateSlowTask(int taskId) {
        try {
            Thread.sleep(100); // pretend we're waiting for a DB/API response
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void run() throws InterruptedException {
        System.out.println("\n====== DEMO 2: Performance Comparison ======");

        int taskCount = 1_000; // run 1000 tasks

        // -----------------------------------------------
        // PLATFORM THREADS — fixed thread pool
        // This will queue tasks and process them slowly
        // because the pool size is limited (e.g. 200)
        // -----------------------------------------------
        long startPlatform = System.currentTimeMillis();

        try (var executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                executor.submit(() -> simulateSlowTask(taskId));
            }
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }

        long platformTime = System.currentTimeMillis() - startPlatform;
        System.out.printf("Platform threads (pool=200): %d tasks in %d ms%n",
                taskCount, platformTime);

        // -----------------------------------------------
        // VIRTUAL THREADS — one virtual thread per task
        // All 1000 run "at the same time" — each parks
        // during sleep, freeing the carrier thread.
        // Result: dramatically faster!
        // -----------------------------------------------
        long startVirtual = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                executor.submit(() -> simulateSlowTask(taskId));
            }
            // executor.close() waits for all tasks — it's auto-called here
        }

        long virtualTime = System.currentTimeMillis() - startVirtual;
        System.out.printf("Virtual threads (unlimited): %d tasks in %d ms%n",
                taskCount, virtualTime);

        System.out.printf("%nVirtual threads were %.1fx faster!%n",
                (double) platformTime / virtualTime);

        System.out.println("\nWhy? Platform threads QUEUE tasks (5 batches of 200).");
        System.out.println("Virtual threads run ALL 1000 concurrently — each parks during sleep.");
    }
}
