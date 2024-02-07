package application;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;

public class ManualDataScene extends Scene {
	
    private TextField [] arrayFields = null;
    
    public ManualDataScene() {
        super(new ScrollPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        
        ScrollPane layout = (ScrollPane) getRoot();
        // Add CSS stylesheet for styling the components.
        layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Create a label for the title with specific font settings.
        Label titelLabel = new Label("Manual data entry");
        titelLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        titelLabel.setStyle("-fx-text-fill: #000000;");
        
        // Create a label for the input prompt with specific font settings.
        Label nLabel = new Label("Enter number of power source: ");
        nLabel.setFont(Font.font("Verdana", 15));
        nLabel.setStyle("-fx-text-fill: #000000;");
        
        // Create a text field for user input.
        TextField nField = new TextField();
        nField.setPrefWidth(200);
        nField.setPrefHeight(33);
        
        // Set up an HBox layout for the label and text field.
        HBox readNHBox = new HBox(40);
        readNHBox.setAlignment(Pos.CENTER);
        readNHBox.getChildren().addAll(nLabel, nField);

        // Create a GridPane for additional UI components (UI to enter data be user).
        GridPane gridPaneFields = new GridPane();
        gridPaneFields.setAlignment(Pos.CENTER);
        gridPaneFields.setVgap(10);
        gridPaneFields.setHgap(10);
        
        // Add listener to the text field to handle changes in input.
        nField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleListener(gridPaneFields, newValue);
        });
        
        // Set up a VBox layout for organizing all the elements vertically.
        VBox vBox = new VBox(60);
        vBox.setPadding(new Insets(60));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(titelLabel, readNHBox, gridPaneFields, createControlSceneHBox());
        
        // Create a StackPane to layer the background image and VBox.
        StackPane stackPane = new StackPane();
        Image backgroundImage = new Image("file:4.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stackPane.heightProperty());

        // Add the background image and VBox to the StackPane.
        stackPane.getChildren().addAll(backgroundImageView, vBox);
        stackPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stackPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
        
        // Set the StackPane as the content of the ScrollPane.
        layout.setContent(stackPane);
    }

    private void handleListener(GridPane grid, String newValue) {
        try {
            // Clear the grid if the new value is empty.
            if (newValue.isEmpty()) {
                grid.getChildren().clear();
                return;
            }

            // Parse the input to get the number of power sources.
            int numberPowerSource = Integer.parseInt(newValue);
            arrayFields = new TextField[numberPowerSource];
            grid.getChildren().clear();

            // For each power source, create a text field and label, and add them to the grid.
            for (int i = 0; i < numberPowerSource; i++) {
                TextField textField = new TextField();
                arrayFields[i] = textField;
                Label label = new Label("The LED corresponding to power source No. " + (i + 1) + " is:");
                label.setFont(Font.font("Verdana", 13.5));
                label.setStyle("-fx-text-fill: #000000;");
                grid.addRow(i, label, textField);
            }
        } catch (Exception e) {
            // Show an alert if there is an exception (like invalid input).
            SceneManager.showAlert("Invalid Input", e.getMessage());
        }
    }

    private HBox createControlSceneHBox() {
        // Create a submit button with an image and styling.
        Button submitButton = new Button("Submit");
        submitButton.setGraphic(new ImageView(new Image("file:submit.png")));
        submitButton.setContentDisplay(ContentDisplay.RIGHT);
        submitButton.getStyleClass().add("custom-button");
        submitButton.setPrefWidth(150);
        submitButton.setPrefHeight(45);
        submitButton.setOnAction(e -> handleSubmitButton());

        // Create a return button with an image and styling.
        Button returnButton = new Button("Back");
        returnButton.setGraphic(new ImageView(new Image("file:reply.png")));
        returnButton.getStyleClass().add("custom-button");
        returnButton.setPrefWidth(150);
        returnButton.setPrefHeight(45);
        returnButton.setOnAction(e -> SceneManager.setMainScene());

        // Create an HBox to hold the buttons.
        HBox controlSceneHBox = new HBox(40);
        controlSceneHBox.setAlignment(Pos.TOP_CENTER);
        controlSceneHBox.getChildren().addAll(returnButton, submitButton);

        return controlSceneHBox;
    }

    private void handleSubmitButton() {
        try {
            // Check if the input fields are initialized, if not, show an error.
            if (arrayFields == null) {
                SceneManager.showAlert("Error", "You must enter the values first");
                return;
            }

            // Parse the input from the fields and validate them.
            int[] LEDs = new int[arrayFields.length];
            for (int i = 0; i < arrayFields.length; i++) {
            	int num = Integer.parseInt(arrayFields[i].getText());
                // Validate that LED numbers are in the correct range and unique.
                if (num < 1 || num > arrayFields.length || MyCollection.checkIfExists(LEDs,num)) {
                    throw new IllegalArgumentException("Invalid input: The LED's are not numbered from 1 to N");
                }
                LEDs[i] = num;
            }

            // Change the scene to display the results.
            SceneManager.setScene(new ResultScene(LEDs));
        } catch (Exception e) {
            // Show an alert if there is any exception.
            SceneManager.showAlert("Error", e.getMessage());
        }
    }

}