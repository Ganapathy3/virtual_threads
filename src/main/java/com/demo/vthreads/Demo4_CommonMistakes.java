package com.demo.vthreads;

import java.util.concurrent.Executors;

/**
 * DEMO 4 — What NOT to do with Virtual Threads
 *
 *   Even magic waiters have rules!
 *   If a waiter carries a heavy tray (synchronized block),
 *   they CAN'T put it down and help others — they're "pinned".
 *
 * Two things that "pin" virtual threads (make them act like platform threads):
 *   1. synchronized blocks/methods
 *   2. Native code (JNI)
 *
 * Solution: replace synchronized with ReentrantLock
 */
public class Demo4_CommonMistakes {

    // BAD: synchronized pins the virtual thread to its carrier
    private static int badCounter = 0;
    private static synchronized void badIncrement() {
        badCounter++;
        try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        // During this sleep, the virtual thread is PINNED — can't release carrier!
    }

    // GOOD: ReentrantLock allows the virtual thread to park and release carrier
    private static int goodCounter = 0;
    private static final java.util.concurrent.locks.ReentrantLock lock =
            new java.util.concurrent.locks.ReentrantLock();

    private static void goodIncrement() {
        lock.lock();
        try {
            goodCounter++;
            try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            // During this sleep, virtual thread PARKS — carrier thread is free!
        } finally {
            lock.unlock(); // always unlock in finally!
        }
    }

    public static void run() throws InterruptedException {
        System.out.println("\n====== DEMO 4: Common Mistakes ======");

        System.out.println("\nRule 1: Avoid 'synchronized' in virtual threads.");
        System.out.println("        Use ReentrantLock instead.\n");

        // Quick demo to show both work correctly (correctness, not perf here)
        int tasks = 100;

        // BAD way
        try (var ex = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < tasks; i++) ex.submit(() -> badIncrement());
        }
        System.out.println("[BAD  - synchronized ] counter = " + badCounter
                + " (correct but pins carrier thread)");

        // GOOD way
        try (var ex = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < tasks; i++) ex.submit(() -> goodIncrement());
        }
        System.out.println("[GOOD - ReentrantLock] counter = " + goodCounter
                + " (correct AND carrier thread stays free)");

        System.out.println("""
                
                Rule 2: Don't use thread-local variables for caching in virtual threads.
                        You might have millions of virtual threads — each would get its
                        own copy, wasting huge amounts of memory.
                        Use ScopedValue (Java 21) instead for passing context.
                
                Rule 3: CPU-heavy tasks (like number crunching) don't benefit from virtual threads.
                        Virtual threads shine for I/O-bound work (DB calls, HTTP, file reads).
                        For CPU work, use parallel streams or ForkJoinPool.
                """);
    }
}
