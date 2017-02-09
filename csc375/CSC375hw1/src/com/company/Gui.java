package com.company;


import com.sun.javafx.tk.Toolkit;
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

/**
 * Created by keithmartin on 9/15/15.
 */
public class Gui extends Application implements Runnable{

    static Stage window;
    static Label avg;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Train controller");

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
        avg = new Label("hello");
        GridPane.setConstraints(avg, 5, 3);

        //Train status text field
        TextField avgText = new TextField();
        GridPane.setConstraints(avgText, 6, 3);

       //Start train button
        Button startTrain = new Button("Start train");
        startTrain.setOnAction(event -> {
        });

        grid.getChildren().addAll(startTrain, trainStatus, trainStatusText, numTrain, numTrainText, numWaiting, numWaitingText, avg, avgText);

        Scene scene = new Scene(grid, 700, 300);

        window.setScene(scene);

        window.show();

    }

    public void setLabelText(String text) {
        //System.out.println("FXMLDocumentController.setLabelText(): Called");
        //trainStatus.setText(text);
    }

    Task task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    avg.setText("We made it son");
                }
            });
            return null;
        }
    };

    @Override
    public void run() {
        launch(Gui.class);
    }
}
