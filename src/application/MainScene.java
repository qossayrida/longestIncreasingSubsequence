package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;         
import javafx.scene.control.*;
import javafx.scene.text.*;

import java.io.*;


// Main scene class that represents the initial user interface.
public class MainScene extends Scene {

    // Constructor for the MainScene class.
    public MainScene(Stage primaryStage){
        super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        StackPane layout = (StackPane) getRoot();
        layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		layout.getStyleClass().add("root");
        
        // Set up the main layout using a VBox.
        VBox vBox = new VBox(60);
        vBox.setPadding(new Insets(60));
        vBox.setAlignment(Pos.TOP_CENTER);
    	
        // Set up a welcome label.
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        // Array of button labels.
        String[] strings = {
            "Upload data from text file",
            "Enter data manually",
            "Random data"
        };
        
        // ArrayList to store buttons.
        Button [] buttons = new Button [strings.length];   
        
        // Method to set up buttons with labels and actions.
        setupButtons(strings, buttons, primaryStage);
        
        // Set up a VBox to arrange buttons vertically.
        VBox ArrangementButtons = new VBox(45);
        ArrangementButtons.setAlignment(Pos.TOP_CENTER);
        ArrangementButtons.getChildren().addAll(buttons);
        
        // Add components to the main layout.
        vBox.getChildren().addAll(welcomeLabel, ArrangementButtons);
        layout.getChildren().addAll(vBox);
    }
    
    // Method to set up buttons with labels and actions.
    public static void setupButtons(String[] strings,Button [] buttons, Stage primaryStage) {
        for (int i = 0; i < strings.length; i++) {
            Button button = new Button(strings[i]);
            buttons[i] =button;
            button.getStyleClass().add("custom-button");
            button.setPrefHeight(50);
            button.setPrefWidth(250);
        }

        // Action for the first button - Upload data from a text file.
        buttons[0].setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null && selectedFile.getName().endsWith(".txt")) {
                // Read data from the file and switch to the ResultScene.
                try {
                    int [] LEDs = readDataFromFile(selectedFile);
                    SceneManager.setScene(new ResultScene(LEDs));
                } catch (Exception e1) {
                    SceneManager.showAlert("Error", e1.getMessage());
                }
            } else if (selectedFile != null) {
                // Show an alert for an invalid file selection.
                SceneManager.showAlert("Error", "Invalid File, Please select a text file (.txt)");
            }
        });

        // Action for the second button - Enter data manually.
        buttons[1].setOnAction(e -> {
            SceneManager.setScene(new ManualDataScene());
        });

        // Action for the third button - Generate random data.
        buttons[2].setOnAction(e -> {
            SceneManager.setScene(new RandomDataScene());
        });
    }
    
    // Method to read data from a file and populate an ArrayList.
    public static int [] readDataFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Read the first line as the total number of LEDs.
        int N = Integer.parseInt(br.readLine());
        int [] array = new int [N];
        
        // Read the second line containing LED values separated by spaces.
        String[] values = br.readLine().split(" ");
        br.close();
        
        // Validate input: Number of LEDs should match the specified total.
        if (array.length != values.length || array==null) {
            throw new IllegalArgumentException("Number of LED's does not match the number of power sources");
        }

        
        
        // Populate the ArrayList with LED values, checking for validity.
        for (int i = 0; i < values.length; i++){
        	int num = Integer.parseInt(values[i]);    
            // Validate input: LEDs should be numbered from 1 to N.
            if (num < 1 || num > N || MyCollection.checkIfExists(array,num)) {
                throw new IllegalArgumentException("Invalid input: The LED's are not numbered from 1 to N");
            }
            array[i]=num;
        }
        
        return array;

    }
}
