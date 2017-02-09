package com.company;

import javafx.concurrent.Task;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by keithmartin on 9/13/15.
 */
public class Spawner extends Task<Void> implements Runnable {

    BlockingQueue<String> passengers = new LinkedBlockingQueue<String>();
    int count = 0;
    String name;
    Random random = new Random();
    int randNum;
    String text = "";

    @Override
    public void run() {
        randNum = random.nextInt(3) + 1;
        for (int i = 0; i < randNum; i++) {
            name = "passenger" + count;
            count++;
            passengers.add(name);
            text = name;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        run();
    }


    @Override
    protected Void call() throws Exception {
        run();
        return null;
    }
}
