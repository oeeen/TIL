package dev.smjeon.til.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 1 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 2000L)
    public void repeatTask2() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 2 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask3() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 3 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask4() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 4 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask5() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 5 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask6() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 6 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }
}
