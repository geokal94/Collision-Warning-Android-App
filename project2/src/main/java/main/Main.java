package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threads.ComputationalServiceRunnable;

import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("project2javafx.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1280, 768));
        primaryStage.show();
    }


    public static void main(String[] args) {
        ComputationalServiceRunnable compserv = new ComputationalServiceRunnable();
        Thread thread = new Thread(compserv);
        thread.start();
        launch(args);

        new Scanner(System.in).nextLine();

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        compserv.unsubscribe();

    }
}
