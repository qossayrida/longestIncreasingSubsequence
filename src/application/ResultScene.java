package application;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.stage.*;

public class ResultScene extends Scene {
	
	private int[] LEDs;
    private int numberPowerSource;
    private int[][] DP;
    private byte[][] source;
    private int[] connections;

    public ResultScene(int[] LEDs) {
        super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        StackPane layout = (StackPane) getRoot();
        // Add a CSS stylesheet to the layout
        layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        // Initialize instance variables
        this.LEDs = LEDs;
        this.numberPowerSource = LEDs.length;
        this.DP = new int[numberPowerSource + 1][numberPowerSource + 1];
        this.source = new byte[numberPowerSource + 1][numberPowerSource / 4 + 1];

        // Initialize the values for DP and connections
        initializeValues();

        // Create and configure background image view
        Image backgroundImage = new Image("file:4.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(layout.widthProperty());
        backgroundImageView.fitHeightProperty().bind(layout.heightProperty());

        // Create and configure welcome label
        Label welcomeLabel = new Label("Results");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 30));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        // Create and configure label displaying the number of connected LEDs
        Label doneCorrectlyLabel = new Label("The number of LEDs that can be connected equal " + DP[numberPowerSource][numberPowerSource] + "  ");
        doneCorrectlyLabel.setFont(Font.font("Century Gothic", 20));
        doneCorrectlyLabel.setStyle("-fx-text-fill: #000000;");
        doneCorrectlyLabel.setGraphic(new ImageView(new Image("file:verification.png")));
        doneCorrectlyLabel.setContentDisplay(ContentDisplay.RIGHT);

        // Create an array of button labels
        String[] strings = {
            "Show Data",
            "Show Result",
            "Show DP table",
            "Show Simulation",
            "Another Solution",
            "Back to main page"
        };

        // Create an array of buttons
        Button[] buttons = new Button[strings.length];
        
        // Set up the buttons
        setupButtons(strings, buttons);

        // Create a VBox to arrange the buttons
        VBox ArrangementButtons = new VBox(45);
        ArrangementButtons.setAlignment(Pos.TOP_CENTER);
        ArrangementButtons.getChildren().addAll(buttons);

        // Create a VBox to arrange labels and buttons vertically
        VBox vBox = new VBox(60);
        vBox.setPadding(new Insets(60));
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(welcomeLabel, doneCorrectlyLabel, ArrangementButtons);

        // Add background image and VBox to the layout
        layout.getChildren().addAll(backgroundImageView, vBox);
    }

    // Initialize values for DP and connections
    private void initializeValues() {
        MyCollection.CreateDPTable(LEDs, DP, source);
        this.connections = new int[DP[numberPowerSource][numberPowerSource]];
        MyCollection.findLEDs(connections, LEDs, DP, source);
    }

  
 // Set up buttons with labels and actions
    public void setupButtons(String[] strings, Button[] buttons) {
        for (int i = 0; i < strings.length; i++) {
            Button button = new Button(strings[i]);
            buttons[i] = button;
            
            // Add a CSS style class to the button
            button.getStyleClass().add("custom-button");
            
            // Set the preferred width of the button
            button.setPrefWidth(250);
        }
        
        // Set actions for each button
        buttons[0].setOnAction(e -> handleShowDataButton());
        buttons[1].setOnAction(e -> handleShowResultButton());
        
        // Create a tooltip for the third button and show it when clicked
        buttons[2].setOnAction(e -> {
            Tooltip tooltip = new Tooltip("If you click on any number in the table, its location will appear");
            tooltip.show(buttons[2], 70, 70);
            
            // Create a timeline to hide the tooltip after a delay
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e1 -> tooltip.hide()));
            timeline.play();
            
            // Handle the action associated with the third button
            handleShowDPTableButton();
        });
        
        buttons[3].setOnAction(e -> handleSimulationButton());
        
        buttons[4].setOnAction(e -> handleShowAnotherSolution());
        
        // Set a graphic (an image) for the fifth button and an action to switch scenes
        buttons[5].setGraphic(new ImageView(new Image("file:reply.png")));
        buttons[5].setOnAction(e -> SceneManager.setMainScene());
    }

    // Handle the action when the "Show Data" button is clicked
    private void handleShowDataButton() {
        // Create a scroll pane to hold content
        ScrollPane subScenePane = new ScrollPane();
        VBox vBox = new VBox(30);
        vBox.setPadding(new Insets(60));
        
        int numberPowerSource = LEDs.length;
        
        // Create labels for power source information
        Label numberPowerSourceLabel = new Label("The number of power sources in circuit S: " + numberPowerSource);
        numberPowerSourceLabel.setFont(Font.font("Verdana", 15.5));
        Label arrangementLabel = new Label("The arrangement of the LEDs on circuit L is: ");
        arrangementLabel.setFont(Font.font("Verdana", 15.5));
        vBox.getChildren().addAll(numberPowerSourceLabel, arrangementLabel);
        
        // Create labels for LED information
        for (int i = 0; i < numberPowerSource; i++) {
            Label label = new Label("The LED corresponding to power source No. " + (i + 1) + " is: " + LEDs[i]);
            label.setFont(Font.font("Verdana", 13.5));
            label.setStyle("-fx-text-fill: #000000;");
            vBox.getChildren().add(label);
        }

        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: #A4D4DD;");
        subScenePane.setContent(vBox);

        // Create a new scene to display the content
        Scene scene = new Scene(subScenePane, 500, 400);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        vBox.prefWidthProperty().bind(newStage.widthProperty());
        vBox.prefHeightProperty().bind(newStage.heightProperty());
        newStage.show();
    }
    
 // Handle the action when the "Show Result" button is clicked
    private void handleShowResultButton() {
        // Create a scroll pane to hold content
        ScrollPane subScenePane = new ScrollPane();
        VBox vBox = new VBox(30);
        vBox.setPadding(new Insets(60));
        
        int numberPowerSource = DP.length - 1;
        
        // Create a label to display the number of connected LEDs
        Label numberOfLEDSLabel = new Label("Number of LEDs that can be connected is " + DP[numberPowerSource][numberPowerSource]);
        numberOfLEDSLabel.setFont(Font.font("Verdana", 15.5));
        
        // Create a label for the arrangement information
        Label arrangementLabel = new Label("What LED's should you connect: ");
        arrangementLabel.setFont(Font.font("Verdana", 15.5));
        
        // Add labels to the VBox
        vBox.getChildren().addAll(numberOfLEDSLabel, arrangementLabel);

        // Create labels for connecting power sources to LEDs
        for (int i = connections.length - 1; i >= 0; i--) {
            Label label = new Label("Connect power source No. " + connections[i] + " with LED No. " + connections[i]);
            label.setFont(Font.font("Verdana", 13.5));
            label.setStyle("-fx-text-fill: #000000;");
            vBox.getChildren().add(label);
        }

        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: #A4D4DD;");
        subScenePane.setContent(vBox);

        // Create a new scene to display the content
        Scene scene = new Scene(subScenePane, 500, 400);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        vBox.prefWidthProperty().bind(newStage.widthProperty());
        vBox.prefHeightProperty().bind(newStage.heightProperty());
        newStage.show();
    }

    // Handle the action when the "Show DP Table" button is clicked
    private void handleShowDPTableButton() {
        // Create a scroll pane to hold content
        ScrollPane subScenePane = new ScrollPane();

        // Create a grid pane to display the DP table
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true); // Show grid lines
        gridPane.setAlignment(Pos.CENTER); // Center the entire grid

        // Create and add labels for the first row (LED numbers)
        Label label1 = new Label("  0  ");
        label1.setFont(Font.font("Verdana", FontWeight.BOLD, 13.5));
        gridPane.add(label1, 1, 0); 
        GridPane.setHalignment(label1, HPos.CENTER);
        
        // Add labels for LED numbers
        for (int j = 0; j < numberPowerSource; j++) {
            Label label = new Label("  " + LEDs[j] + "  ");
            label.setFont(Font.font("Verdana", FontWeight.BOLD, 13.5));
            gridPane.add(label, j + 2, 0); // Column, Row
            GridPane.setHalignment(label, HPos.CENTER); // Center the label horizontally
        }

        // Add row indices and array elements to the grid
        for (int i = 0; i < DP.length; i++) {
            // Add row index label
            Label rowIndexLabel = new Label("  " + i + "  ");
            rowIndexLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13.5));
            gridPane.add(rowIndexLabel, 0, i + 1); // Column, Row
            GridPane.setHalignment(rowIndexLabel, HPos.CENTER); // Center the label horizontally

            // Add array elements
            for (int j = 0; j < DP[i].length; j++) {
                Label label = new Label(String.valueOf(DP[i][j]));
                label.setContentDisplay(ContentDisplay.TOP);
                
                if (j == 0 || i == 0) {
                    label.setStyle("-fx-text-fill: #FF0000;");
                } else {
                    int value = (int) MyCollection.getTwoBits(source[i], j);
                    if (value == 2)
                        label.setGraphic(new ImageView(new Image("file:diagonal.png")));
                    else if (value == 1)
                        label.setGraphic(new ImageView(new Image("file:up.png")));
                    else 
                        label.setGraphic(new ImageView(new Image("file:right.png")));
                    
                    // Create a tooltip to show the location when the label is clicked
                    Tooltip tooltip = new Tooltip("(" + i + "," + LEDs[j - 1] + ")");
                    label.setOnMouseClicked(event -> {
                        tooltip.show(label, 70, 70);
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tooltip.hide()));
                        timeline.play();
                    });
                }
                gridPane.add(label, j + 1, i + 1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }

        // Set the alignment and background color for the grid pane
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #A4D4DD;");
        subScenePane.setContent(gridPane);
        Scene scene = new Scene(subScenePane, 500, 400);
        
        // Create a new stage to display the scene
        Stage newStage = new Stage();
        newStage.setScene(scene);

        // If the number of LEDs is less than 41, bind the grid pane's dimensions to the stage's dimensions
        if (LEDs.length < 41) {
            gridPane.prefWidthProperty().bind(newStage.widthProperty());
            gridPane.prefHeightProperty().bind(newStage.heightProperty());
        }
        
        // Show the new stage with the scene
        newStage.show();
    }

    
    private void handleSimulationButton () {
        // Create a scroll pane for the simulation
        ScrollPane subScenePane = new ScrollPane();
        // Create an HBox to arrange elements horizontally
        HBox hBox = new HBox(80);
        // Set the alignment and padding for the HBox
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50));
        // Set the background color for the HBox
        hBox.setStyle("-fx-background-color: #A4D4DD;");

        // Get the number of power sources in the circuit
        int numberPowerSource = DP.length - 1;
        // Create arrays to store labels for power sources and LEDs
        Label[] powerSourcesOn = new Label[connections.length];
        Label[] LEDsOn = new Label[connections.length];

        // Create a VBox for Circuit S (power sources)
        VBox circuitS = new VBox(30);
        // Create a label for Circuit S
        Label circuitSLabel = new Label("Circuit S - Power Sources -");
        circuitSLabel.setFont(Font.font("Verdana", 15.5));
        circuitSLabel.setStyle("-fx-text-fill: #FFFFFF;");
        circuitS.getChildren().add(circuitSLabel);

        // Count the number of power sources that are on and create labels accordingly
        int count = 0;
        for (int i = 1; i <= numberPowerSource; i++) {
            Label label = new Label("Power source No. " + i);
            label.setContentDisplay(ContentDisplay.RIGHT);
            if (MyCollection.checkIfExists(connections, i)) {
                label.setGraphic(new ImageView(new Image("file:PowerSourceOn.png")));
                powerSourcesOn[count++] = label;
            } else {
                label.setGraphic(new ImageView(new Image("file:PowerSourceOff.png")));
            }
            label.setFont(Font.font("Verdana", 13.5));
            label.setStyle("-fx-text-fill: #FFFFFF;");
            circuitS.getChildren().add(label);
        }

        // Set alignment, styles, and dimensions for Circuit S VBox
        circuitS.setAlignment(Pos.TOP_CENTER);
        circuitS.setStyle("-fx-background-color: #2b2b2b; " +
                "-fx-border-color: #555555; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 10px;");

        // Create a VBox for Circuit L (LEDs)
        VBox circuitL = new VBox(30);
        // Create a label for Circuit L
        Label circuitLLabel = new Label("Circuit L - LEDs -");
        circuitLLabel.setFont(Font.font("Verdana", 15.5));
        circuitLLabel.setStyle("-fx-text-fill: #FFFFFF;");
        circuitL.getChildren().add(circuitLLabel);

        // Reset the count and create labels for LEDs based on their status
        count = 0;
        for (int i = 0; i < numberPowerSource; i++) {
            Label label = new Label("LED No. " + LEDs[i]);
            if (MyCollection.checkIfExists(connections, LEDs[i])) {
                label.setGraphic(new ImageView(new Image("file:ledOn.png")));
                LEDsOn[count++] = label;
            } else {
                label.setGraphic(new ImageView(new Image("file:ledOff.png")));
            }
            label.setFont(Font.font("Verdana", 13.5));
            label.setStyle("-fx-text-fill: #FFFFFF;");
            circuitL.getChildren().add(label);
        }

        // Set alignment, styles, and dimensions for Circuit L VBox
        circuitL.setAlignment(Pos.TOP_CENTER);
        circuitL.setStyle("-fx-background-color: #2b2b2b; " +
                "-fx-border-color: #555555; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 10px;");
        
        // Set preferred width for Circuit S and Circuit L VBoxes
        circuitS.setPrefWidth(250);
        circuitL.setPrefWidth(250);
        
        // Add Circuit S and Circuit L VBoxes to the HBox
        hBox.getChildren().addAll(circuitS, circuitL);
        
        // Set the content of the scroll pane to the HBox
        subScenePane.setContent(hBox);

        // Create a new scene with the scroll pane content
        Scene scene = new Scene(subScenePane, 800, 500);
        
        // Create a new stage to display the scene
        Stage newStage = new Stage();
        newStage.setScene(scene);

        // Bind the dimensions of the HBox to the stage's dimensions
        hBox.prefWidthProperty().bind(newStage.widthProperty());
        hBox.prefHeightProperty().bind(newStage.heightProperty());
        
        // Show the new stage with the scene
        newStage.show();
        
        // Create a Pane for connection lines
        Pane connectionPane = new Pane();
        
        // Draw connection lines between LEDs and power sources
        drawConnectionLines(LEDsOn, powerSourcesOn, connectionPane);
        
        // Set spacing and update the children of the HBox
        hBox.setSpacing(0);
        hBox.getChildren().setAll(circuitS, connectionPane, circuitL);
    }

    private void drawConnectionLines(Label[] leds, Label[] powerSources, Pane connectionPane) {
        // Loop through the labels representing LEDs and power sources
        for (int i = 0; i < leds.length; i++) {
            Label ledLabel = leds[i];
            Label powerSourceLabel = powerSources[i];

            // Calculate start and end Y positions for the connection line
            double startY = powerSourceLabel.getLayoutY() + powerSourceLabel.getHeight() / 2;
            double endY = ledLabel.getLayoutY() + ledLabel.getHeight() / 2;

            // Create a line to represent the connection
            Line line = new Line(20, startY, 120, endY);
            line.setStyle("-fx-stroke: #D9AD2B ; -fx-stroke-width: 2;");

            // Create and position images to indicate the connection
            Image image = new Image("file:q.png");
            ImageView imageView = new ImageView(image);
            imageView.setLayoutX(120);
            imageView.setLayoutY(endY - 10);

            Image image1 = new Image("file:w.png");
            ImageView imageView1 = new ImageView(image1);
            imageView1.setLayoutX(0);
            imageView1.setLayoutY(startY - 10);

            // Add the line and images to the connectionPane
            connectionPane.getChildren().addAll(line, imageView, imageView1);
        }
    }
    
    private void handleShowAnotherSolution() {
        // Create a VBox to hold the title and the main GridPane
        VBox vBox = new VBox(10); // 10 is the spacing between elements in the VBox
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20)); // Padding around the VBox
        vBox.setStyle("-fx-background-color: #A4D4DD;");

        int[] lengths = new int[LEDs.length];
        int[] sequences = new int[LEDs.length];
        int[] connectionsUseLIS = MyCollection.longestIncreasingSubsequence(LEDs, lengths, sequences);
        
        // Title label for the VBox
        Label titleLabel = new Label("Number of LEDs that can be connected is " + connectionsUseLIS.length);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        vBox.getChildren().add(titleLabel); // Add title label to the VBox

        // Create a large GridPane to hold titles and array GridPanes
        GridPane mainGridPane = new GridPane();
        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setVgap(15); // Increase vertical gap between rows

        // Add each array with its title to the mainGridPane
        addToMainGridPane(mainGridPane, LEDs, "LEDs", 0);
        addToMainGridPane(mainGridPane, lengths, "Lengths", 1);
        addToMainGridPane(mainGridPane, sequences, "Sequences", 2);
        addToMainGridPane(mainGridPane, connectionsUseLIS, "Conuctins", 3);

        vBox.getChildren().add(mainGridPane); // Add mainGridPane to the VBox

        // Create a scroll pane to hold the VBox
        ScrollPane subScenePane = new ScrollPane();
        subScenePane.setContent(vBox);

        Scene scene = new Scene(subScenePane, 800, 400); // Adjust size as needed
        Stage newStage = new Stage();
        newStage.setScene(scene);

        if (LEDs.length < 31) {
            vBox.prefWidthProperty().bind(newStage.widthProperty());
            vBox.prefHeightProperty().bind(newStage.heightProperty());
        } else {
            vBox.prefHeightProperty().bind(newStage.heightProperty());
        }
        newStage.show();
    }

    // Helper method to add a title, a GridPane for an array, and numbers from 0 to array.length to the main GridPane
    private void addToMainGridPane(GridPane mainGrid, int[] array, String title, int rowIndex) {
        // Create title label with increased size
        Label titleLabel = new Label(title + ":   ");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Create GridPane with array elements and indices
        GridPane arrayGridPane = new GridPane();
        arrayGridPane.setGridLinesVisible(true);

        for (int i = 0; i < array.length; i++) {
            // Add array elements to the first row
        	Label arrayLabel;
        	if (array[i]!=-1)
        		arrayLabel = new Label(array[i] + "");
        	else 
        		arrayLabel = new Label("start");
			
        	
            arrayLabel.setPrefWidth(50);
            arrayLabel.setAlignment(Pos.CENTER);
            arrayLabel.setStyle("-fx-font-size: 14px;");
            arrayGridPane.add(arrayLabel, i, 0);

            // Add index numbers to the second row
            Label indexLabel = new Label("  " + i + "  ");
            indexLabel.setStyle("-fx-font-size: 8px;");
            indexLabel.setPrefWidth(50);
            indexLabel.setAlignment(Pos.CENTER);
            arrayGridPane.add(indexLabel, i, 1);
        }

        // Add the title label and array GridPane to the main GridPane
        mainGrid.add(titleLabel, 0, rowIndex);
        mainGrid.add(arrayGridPane, 1, rowIndex);
    }


}
