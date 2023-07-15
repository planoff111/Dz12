package org.example;

import java.util.concurrent.*;

public class Main {
    private static final int NUM_ITERATIONS = 10;
    private static final String CHICKEN = "Chicken";
    private static final String EGG = "Egg";
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CustomThread thread1 = new  CustomThread();
        thread1.start();
        CustomRunnable customRunnable = new CustomRunnable();
        Thread thread = new Thread(customRunnable);
        thread.start();
        CustomCallable customCallable = new CustomCallable();
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future task = service.submit(customCallable);
        System.out.println(task.get());
        Thread newThread = new Thread(
                ()-> System.out.println("I am Thread from lamda" + Thread.currentThread().getName())
        );
        newThread.start();

        startThreadsForInc();
    }
// Воно не інкрементує далі а кожен поток робить + 1 до 0 і виходить 1 в кожному потоці
    static void startThreadsForInc(){
        for (int i = 0; i < 10 ; i++){
            Thread threadForInc = new Thread(
                    () -> {
                        int value = 0;
                        value++;
                        System.out.println("Incremented value: " + value + " " + Thread.currentThread().getName());
                    }
            );
            threadForInc.start();
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
            System.out.println(player);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

