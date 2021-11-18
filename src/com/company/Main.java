package com.company;

import java.util.Random;

public class Main {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {

        ThreadSafeHashMap<Integer, Integer> map = new ThreadSafeHashMap<>();

        System.out.println(map.size());
        Runnable runnable = () -> {
            while (map.size() < 10) {
                map.put(RANDOM.nextInt(50), RANDOM.nextInt(10));
            }
        };

        new Thread(runnable, "Thread1").start();
        new Thread(runnable, "Thread2").start();
        new Thread(runnable, "Thread3").start();


        Thread.sleep(1000);

    }
}
