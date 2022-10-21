package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import Controller.SongLibController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Song;

public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// create FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/Controller/SongLib.fxml"));
		
		
		
		// load fmxl, root layout manager in fxml file is GridPane
		AnchorPane root = (AnchorPane)loader.load();

		SongLibController controller = loader.getController();
		controller.start(primaryStage);

		// set scene to root
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		//readSongs();
		//System.out.println("hello world");
		
	}
	
	

	public static void main(String[] args) {
		
		//readSongs();
		launch(args);
	}
	
	
}


