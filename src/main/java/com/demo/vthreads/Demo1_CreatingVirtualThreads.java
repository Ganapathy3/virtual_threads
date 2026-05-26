package com.demo.vthreads;

/**
 * DEMO 1 — How to CREATE Virtual Threads
 *
 * 
 *   Normal thread  = hiring a full-time employee (expensive, limited)
 *   Virtual thread = calling a freelancer when needed (cheap, unlimited)
 *
 * Java gives you 3 ways to create virtual threads.
 * All 3 shown here.
 */
public class Demo1_CreatingVirtualThreads {

    public static void run() throws InterruptedException {
        System.out.println("\n====== DEMO 1: Creating Virtual Threads ======");

        // -----------------------------------------------
        // WAY 1: Thread.ofVirtual() — simplest way
        // -----------------------------------------------
        Thread vt1 = Thread.ofVirtual()
                .name("my-virtual-thread")
                .start(() -> {
                    System.out.println("[Way 1] Running in: " + Thread.currentThread());
                    System.out.println("[Way 1] Is virtual? " + Thread.currentThread().isVirtual());
                });

        // -----------------------------------------------
        // WAY 2: Thread.startVirtualThread() — one-liner
        // -----------------------------------------------
        Thread vt2 = Thread.startVirtualThread(() -> {
            System.out.println("[Way 2] Running in: " + Thread.currentThread());
            System.out.println("[Way 2] Is virtual? " + Thread.currentThread().isVirtual());
        });

        // -----------------------------------------------
        // WAY 3: Executors.newVirtualThreadPerTaskExecutor()
        // This is the most useful in real apps!
        // Creates a new virtual thread for EVERY task automatically.
        // -----------------------------------------------
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                System.out.println("[Way 3] Running in: " + Thread.currentThread());
                System.out.println("[Way 3] Is virtual? " + Thread.currentThread().isVirtual());
            });
        } // executor auto-closes and waits for tasks to finish

        // Wait for vt1 and vt2 to finish
        vt1.join();
        vt2.join();

        // -----------------------------------------------
        // Compare: platform thread vs virtual thread
        // -----------------------------------------------
        Thread platformThread = Thread.ofPlatform()
                .name("platform-thread")
                .start(() ->
                    System.out.println("\n[Platform] Is virtual? " + Thread.currentThread().isVirtual())
                );

        Thread virtualThread = Thread.ofVirtual()
                .name("virtual-thread")
                .start(() ->
                    System.out.println("[Virtual ] Is virtual? " + Thread.currentThread().isVirtual())
                );

        platformThread.join();
        virtualThread.join();

        System.out.println("Demo 1 done!");
    }
}
