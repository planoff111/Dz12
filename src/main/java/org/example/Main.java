package org.example;

import java.util.concurrent.*;

public class Main {
    private static final int NUM_ITERATIONS = 10;
    private static final String CHICKEN = "Chicken";
    private static final String EGG = "Egg";
    private static final Object lock = new Object();
    private static String currentPlayer = CHICKEN;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CustomThread thread1 = new CustomThread();
        thread1.start();
        CustomRunnable customRunnable = new CustomRunnable();
        Thread thread = new Thread(customRunnable);
        thread.start();
        CustomCallable customCallable = new CustomCallable();
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future task = service.submit(customCallable);
        System.out.println(task.get());
        Thread newThread = new Thread(
                () -> System.out.println("I am Thread from lamda" + Thread.currentThread().getName())
        );
        newThread.start();
        startThreadsForInc();
    }

    static int value = 0;

    // так виніс за межі методу змінну результат не консистентний кожен раз при додаванні join то інкрементує по черзі
    static void startThreadsForInc() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread threadForInc = new Thread(
                    () -> {
                        value++;
                        System.out.println("Incremented value: " + value + " " + Thread.currentThread().getName());
                    }
            );
            threadForInc.start();
            //threadForInc.join();
        }

        Thread chickenThread = new Thread(() ->
                playGame(CHICKEN)
        );
        Thread eggThread = new Thread(() ->
                playGame(EGG)
        );

        chickenThread.start();
        eggThread.start();

        try {
            chickenThread.join();
            eggThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Game over!");
    }


    private static void playGame(String player) {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            synchronized (lock) {
                while (!player.equals(currentPlayer)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(player);

                if (player.equals(CHICKEN)) {
                    currentPlayer = EGG;
                } else {
                    currentPlayer = CHICKEN;
                }

                lock.notifyAll();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

