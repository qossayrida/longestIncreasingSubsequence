package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;

public class RandomDataScene extends Scene {
    
	private int[] LEDs = null;
	Button scrollToEndButton = new Button("Scroll to End");

	public RandomDataScene() {
	    super(new ScrollPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
	    ScrollPane layout = (ScrollPane) getRoot();
	    // Apply CSS stylesheet for styling components.
	    layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		layout.getStyleClass().add("root");
		
	    // Create and style title label.
	    Label titleLabel = new Label("Generate random data");
	    titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
	    titleLabel.setStyle("-fx-text-fill: #000000;");

	    // Create and style label for number input.
	    Label nLabel = new Label("Enter number of power source: ");
	    nLabel.setFont(Font.font("Verdana", 15));
	    nLabel.setStyle("-fx-text-fill: #000000;");

	    // Create text field for number input.
	    TextField nField = new TextField();
	    nField.setPrefWidth(200);
	    nField.setPrefHeight(33);

	    // Set up horizontal box layout for label and text field.
	    HBox readNHBox = new HBox(40);
	    readNHBox.setAlignment(Pos.CENTER);
	    readNHBox.getChildren().addAll(nLabel, nField);

	    // Create a GridPane for arranging labels dynamically.
	    GridPane gridPaneFields = new GridPane();
	    gridPaneFields.setAlignment(Pos.CENTER);
	    gridPaneFields.setVgap(10);
	    gridPaneFields.setHgap(10);

	    // Add listener to handle input changes.
	    nField.textProperty().addListener((observable, oldValue, newValue) -> {
	        handleListener(gridPaneFields, newValue, layout, readNHBox);
	    });

	    // Set up vertical box layout for organizing elements.
	    VBox vBox = new VBox(60);
	    vBox.setPadding(new Insets(60));
	    vBox.setAlignment(Pos.TOP_CENTER);
	    vBox.getChildren().addAll(titleLabel, readNHBox, gridPaneFields, createControlSceneHBox());

	    // Create a StackPane for background and content.
	    StackPane stackPane = new StackPane();
	    Image backgroundImage = new Image(getClass().getResourceAsStream("/pictures/background.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stackPane.heightProperty());

        // Add the background image and VBox to the StackPane.
        stackPane.getChildren().addAll(backgroundImageView, vBox);
	    stackPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    stackPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    // Set StackPane as the content of the layout.
	    layout.setContent(stackPane);
	}

	private void handleListener(GridPane grid, String newValue, ScrollPane layout, HBox hBox) {
	    try {
	        // Clear grid if no input.
	        if (newValue.isEmpty()) {
	        	LEDs=null;
	            grid.getChildren().clear();
	            return;
	        }

	        // Adjust scroll maximum based on grid height.
	        if (grid.getHeight() > layout.getVmax()) 
	            layout.setVmax(grid.getHeight());

	        // Parse the number of power sources.
	        int numberPowerSource = Integer.parseInt(newValue);
	        grid.getChildren().clear();

	        // Add scrollToEndButton if number of power sources is large.
	        if (numberPowerSource > 15 && !hBox.getChildren().contains(scrollToEndButton)) {
	            scrollToEndButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF; -fx-font-family: 'Century Gothic'; -fx-font-size: 16;");
	            hBox.getChildren().add(scrollToEndButton);
	            scrollToEndButton.setOnAction(e -> layout.setVvalue(grid.getHeight()));
	        } 
	        // Remove scrollToEndButton for smaller number of power sources.
	        else if (numberPowerSource <= 15 && hBox.getChildren().contains(scrollToEndButton)) {
	            hBox.getChildren().remove(scrollToEndButton);
	        }

	        // Initialize and shuffle LED array based on the number of power sources.
	        LEDs = new int[numberPowerSource];
	        for (int i = 1; i <= numberPowerSource; i++) 
	            LEDs[i - 1] = i;
	        MyCollection.shuffle(LEDs);

	        // Add labels for each power source with shuffled LED values.
	        for (int i = 0; i < numberPowerSource; i++) {
	            Label label = new Label("The LED corresponding to power source No. " + (i + 1) + " is: " + LEDs[i]);
	            label.setFont(Font.font("Verdana", 13.5));
	            label.setStyle("-fx-text-fill: #000000;");
	            grid.addRow(i, label);
	        }
	    } catch (Exception e) {
	        // Show an alert for invalid input.
	        SceneManager.showAlert("Invalid Input", e.getMessage());
	    }
	}

	private HBox createControlSceneHBox() {
	    // Create submit button with image and styling.
	    Button submitButton = new Button("Submit");
	    submitButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/pictures/submit.png"))));
	    submitButton.setContentDisplay(ContentDisplay.RIGHT);
	    submitButton.getStyleClass().add("custom-button");
	    submitButton.setPrefWidth(150);
	    submitButton.setPrefHeight(45);
	    // Action for submit button.
	    submitButton.setOnAction(e -> {
	        if (LEDs == null)
	            SceneManager.showAlert("Error", "You must enter the values first");
	        else
	            SceneManager.setScene(new ResultScene(LEDs));
	    });

	    // Create return button with image and styling.
	    Button returnButton = new Button("Back");
	    returnButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/pictures/reply.png"))));
	    returnButton.getStyleClass().add("custom-button");
	    returnButton.setPrefWidth(150);
	    returnButton.setPrefHeight(50);
	    // Action for return button.
	    returnButton.setOnAction(e -> SceneManager.setMainScene());

	    // Set up horizontal box layout for control buttons.
	    HBox controlSceneHBox = new HBox(40);
	    controlSceneHBox.setAlignment(Pos.TOP_CENTER);
	    controlSceneHBox.getChildren().addAll(returnButton, submitButton);

	    return controlSceneHBox;
	}

}

