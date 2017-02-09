package com.company;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.*;

import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by keithmartin on 9/10/15.
 */
public class Train implements Runnable {

    static String text = "";

    //Blocking train queue of size 2
    BlockingQueue train = new ArrayBlockingQueue(2);

    @Override
    public void run() {

        if (!train.isEmpty()) {
            Platform.runLater(() -> {
                Gui.avg.setText("Cleared train");
                train.clear();
            });
        }

        try {
            Platform.runLater(() -> {
                Gui.avg.setText("Train is running...");
            });
                    Thread.sleep(5000);
            Platform.runLater(() -> {
                Gui.avg.setText("Train is at the station");
            });
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("Interrupted");
        }
        run();
    }
}
