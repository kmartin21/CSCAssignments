package com.company;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sun.misc.Service;

import java.util.concurrent.CountDownLatch;

public class Main extends Application {


    Label avg;
    static Spawner spawn = new Spawner();
    static Train train = new Train();

    static Thread spawnThread = new Thread(spawn);
    static Thread trainThread = new Thread(train);

    public static void main(String[] args) {

        launch(args);
        //Gui gui = new Gui();


        //Thread guiThread = new Thread(gui);


        //guiThread.start();



        while(trainThread.isAlive()) {
            try {
                trainThread.join(1000);
                for(int i = 0; i < 2; i++) {
                    if (train.train.size() != 2) {
                        train.train.add(spawn.passengers.take());
                        System.out.println("Added passengers");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }





    @Override
    public void start(Stage primaryStage) throws Exception {

        Gui.window = primaryStage;
        Gui.window.setTitle("Train controller");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Train status label
        Label trainStatus = new Label("Train status:");
        GridPane.setConstraints(trainStatus, 5, 0);

        //Train status text field
        TextField trainStatusText = new TextField();
        GridPane.setConstraints(trainStatusText, 6, 0);

        //# on train label
        Label numTrain = new Label("Number of passengers on train:");
        GridPane.setConstraints(numTrain, 5, 1);

        //# on train text field
        TextField numTrainText = new TextField();
        GridPane.setConstraints(numTrainText, 6, 1);

        //# waiting label
        Label numWaiting = new Label("Number of passengers waiting:");
        GridPane.setConstraints(numWaiting, 5, 2);

        //# waiting text field
        TextField numWaitingText = new TextField();
        GridPane.setConstraints(numWaitingText, 6, 2);

        //Average throughput label
        Gui.avg = new Label("Average throughput");
        GridPane.setConstraints(Gui.avg, 5, 3);

        //Train status text field
        TextField avgText = new TextField();
        GridPane.setConstraints(avgText, 6, 3);

        //Start train button
        Button startTrain = new Button("Start train");
        startTrain.setOnAction(event -> {
            spawnThread.start();
            trainThread.start();
            t.start();
        });

        grid.getChildren().addAll(startTrain, trainStatus, trainStatusText, numTrain, numTrainText, numWaiting, numWaitingText, Gui.avg, avgText);

        Scene scene = new Scene(grid, 700, 300);

        Gui.window.setScene(scene);

        Gui.window.show();

    }


    Thread t = new Thread(() -> {
        while (trainThread.isAlive()) {
            try {
                Platform.runLater(() -> {
                    try {
                        trainThread.join(1000);
                        for (int i = 0; i < 2; i++) {
                            if (train.train.size() != 2) {
                                train.train.add(spawn.passengers.take());
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(300);
            }
            catch (Exception e) {}
        }

    });

    static Task task = new Task<String>() {
        @Override
        protected String call() throws Exception {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
    };
}
