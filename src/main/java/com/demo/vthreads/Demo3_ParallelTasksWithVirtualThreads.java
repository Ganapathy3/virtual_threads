package com.demo.vthreads;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * DEMO 3 — Running Multiple Tasks in Parallel with Virtual Threads
 *
 *   Imagine you need to cook 3 dishes for a party.
 *   Old way: cook one dish, finish, then start the next → SLOW
 *   New way: put all 3 on the stove at the same time → FAST
 *
 *   Virtual threads let you do many "waiting" tasks at the same time
 *   without wasting resources.
 *
 * Real-world use case:
 *   Call 3 microservices in parallel, combine results.
 *   With virtual threads: total time = slowest single call (not sum of all).
 */
public class Demo3_ParallelTasksWithVirtualThreads {

    // Simulates calling a microservice (takes some time)
    private static String callUserService() throws InterruptedException {
        Thread.sleep(200); // pretend HTTP call
        return "User: Ganapathy";
    }

    private static String callOrderService() throws InterruptedException {
        Thread.sleep(300); // pretend DB query
        return "Orders: 5 pending";
    }

    private static String callInventoryService() throws InterruptedException {
        Thread.sleep(150); // pretend cache lookup
        return "Inventory: 42 items in stock";
    }

    public static void run() throws Exception {
        System.out.println("\n====== DEMO 3: Parallel Tasks with Virtual Threads ======");

        // OLD WAY — sequential (slow)
        long startSequential = System.currentTimeMillis();
        String user      = callUserService();
        String orders    = callOrderService();
        String inventory = callInventoryService();
        long sequentialTime = System.currentTimeMillis() - startSequential;

        System.out.println("\n[Sequential]");
        System.out.println("  " + user);
        System.out.println("  " + orders);
        System.out.println("  " + inventory);
        System.out.printf("  Time: %d ms (200 + 300 + 150 = 650ms expected)%n", sequentialTime);

        // NEW WAY — parallel with virtual threads (fast!)
        long startParallel = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // Submit all 3 tasks at the same time
            Future<String> userFuture      = executor.submit(() -> callUserService());
            Future<String> orderFuture     = executor.submit(() -> callOrderService());
            Future<String> inventoryFuture = executor.submit(() -> callInventoryService());

            // Collect results (each .get() waits for that task)
            String userResult      = userFuture.get();
            String orderResult     = orderFuture.get();
            String inventoryResult = inventoryFuture.get();

            long parallelTime = System.currentTimeMillis() - startParallel;

            System.out.println("\n[Virtual Threads - Parallel]");
            System.out.println("  " + userResult);
            System.out.println("  " + orderResult);
            System.out.println("  " + inventoryResult);
            System.out.printf("  Time: %d ms (only slowest = ~300ms expected)%n", parallelTime);

            System.out.printf("%n  Speedup: %.1fx faster!%n",
                    (double) sequentialTime / parallelTime);
        }

        System.out.println("\nThis is HUGE for microservices! Call 10 services in parallel,");
        System.out.println("total time = time of the SLOWEST service, not their sum.");
    }
}
