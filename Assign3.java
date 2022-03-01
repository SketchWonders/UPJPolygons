//Assign3

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import javafx.scene.control.*;
//import javafx.scene.paint.*;
//import javafx.collections.*;

public class Assign3 extends Application {
   @Override
   public void start(Stage stage) throws Exception {
	  
	  Parent root = 
         FXMLLoader.load(getClass().getResource("Assign3.fxml"));

      Scene scene = new Scene(root); // attach scene graph to scene
      stage.setTitle("Assign3"); // displayed in window's title bar
      stage.setScene(scene); // attach scene to stage
      stage.show(); // display the stage
   }

   public static void main(String[] args) {
      launch(args); 
   }
}
