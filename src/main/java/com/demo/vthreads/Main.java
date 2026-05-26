package com.demo.vthreads;

/**
 * Day 2 — Java 21 Virtual Threads (Project Loom)
 *
 * Run this class to see all 5 demos in order.
 *
 * What you'll learn:
 *   Demo 1 — How to CREATE virtual threads (3 ways)
 *   Demo 2 — PERFORMANCE: virtual vs platform threads
 *   Demo 3 — Running tasks IN PARALLEL (microservice pattern)
 *   Demo 4 — MISTAKES to avoid (synchronized, thread-locals)
 *   Demo 5 — How to enable in Spring Boot (1 line!)
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=================================================");
        System.out.println("  Day 2: Java 21 Virtual Threads (Project Loom)  ");
        System.out.println("=================================================");

//        Demo1_CreatingVirtualThreads.run();
//        Demo2_PerformanceComparison.run();
//        Demo3_ParallelTasksWithVirtualThreads.run();
        Demo4_CommonMistakes.run();
//        Demo5_SpringBootIntegration.run();

        System.out.println("\n=================================================");
        System.out.println("  All demos complete! Check the output above.    ");
        System.out.println("=================================================");
    }
}
