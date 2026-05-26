package com.demo.vthreads;

/**
 * DEMO 5 — Virtual Threads in Spring Boot 3.2+
 *
 *   In your Spring Boot app from Day 1, every HTTP request gets its own thread.
 *   With ONE line of config, all those request threads become VIRTUAL threads.
 *   Your app can now handle way more requests without buying more servers!
 *
 * This demo shows WHAT to add to your Spring Boot project (Day 1).
 * No runnable code here — just the config snippets to copy.
 */
public class Demo5_SpringBootIntegration {

    public static void run() {
        System.out.println("\n====== DEMO 5: Virtual Threads in Spring Boot ======");

        System.out.println("""
                
                To enable Virtual Threads in your Spring Boot app (Day 1 project),
                add ONE line to application.properties:
                
                    spring.threads.virtual.enabled=true
                
                That's it! Spring Boot 3.2+ automatically uses virtual threads for:
                  - Every incoming HTTP request (Tomcat threads)
                  - @Async method calls
                  - Scheduled tasks (@Scheduled)
                
                -------------------------------------------------------
                You can also configure it in Java (optional):
                -------------------------------------------------------
                
                @Configuration
                public class VirtualThreadConfig {
                
                    @Bean
                    public TomcatProtocolHandlerCustomizer<?> virtualThreadsToTomcat() {
                        return handler ->
                            handler.setExecutor(
                                Executors.newVirtualThreadPerTaskExecutor()
                            );
                    }
                }
                
                -------------------------------------------------------
                Real impact on your Day 1 Spring AI app:
                -------------------------------------------------------
                
                Each request to POST /ask calls Groq's API (I/O wait).
                OLD: platform thread BLOCKS during the HTTP call to Groq.
                NEW: virtual thread PARKS during the call — frees the carrier.
                
                With 1000 concurrent users:
                  Platform threads: may exhaust thread pool, requests queue up
                  Virtual threads:  handles all 1000 with ease!
                """);
    }
}
