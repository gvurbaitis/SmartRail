package main.display;

import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import main.Coordinator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Creates the main menu gui screen. Allows user to select
 * a configuration file and start up the simulation.
 */
public class MainMenu
{
    private Stage window;
    private Group root;

    /**
     * Sets the global variables
     * @param window the main window
     */
    public MainMenu(Stage window)
    {
        this.window = window;
    }

    /**
     * Creates all the gui elements, adds them to a group,
     * and displays them on the window. Kills all threads on
     * exit of the gui.
     */
    public void initialize()
    {
        root = new Group();
        drawBackground();
        createButton();
        addTittle();

        Scene scene = new Scene(root, 800, 700);
        window.setTitle("Train Simulation - Main Menu");
        window.setResizable(false);
        window.sizeToScene();
        window.setScene(scene);
        window.setOnCloseRequest(e -> System.exit(0));
        window.show();
    }

    /**
     * Loads the background image and displays it
     */
    private void drawBackground()
    {
        Rectangle background = new Rectangle(0, 0, 800, 700);
        ImagePattern imagePattern = new ImagePattern(new Image("background.png"));
        background.setFill(imagePattern);

        root.getChildren().add(background);
    }

    /**
     * Creates a combo box button that allows user to choose from one of 4
     * pre-configured files.
     */
    private void createButton()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "   config1",
                        "   config2",
                        "   config3",
                        "  config_TA"
                );

        ComboBox<String> cb = new ComboBox<>(options);
        cb.setValue("  Start");
        cb.setStyle("-fx-font-family: monospace;-fx-font-weight: bold;-fx-font-size: 25px;");
        cb.setPrefSize(200, 75);
        cb.setLayoutX(300);
        cb.setLayoutY(300);
        cb.setVisibleRowCount(4);
        cb.setCursor(Cursor.HAND);
        cb.setOnAction(e ->
                playAction(cb.getSelectionModel().getSelectedItem().trim()));

        root.getChildren().add(cb);
    }

    /**
     * When a configuration file is selected, load and start the simulation.
     * Creates and initializes coordinator object.
     * @param configFile the name of the config file to be loaded
     */
    private void playAction(String configFile)
    {
        Coordinator coordinator = new Coordinator(window, configFile);
        coordinator.initSimulation();
    }

    /**
     * Adds the title image to the gui.
     */
    private void addTittle()
    {
        Image img = new Image("title.png");
        ImageView title = new ImageView(img);
        title.autosize();
        title.setLayoutX(100);
        title.setLayoutY(100);

        root.getChildren().add(title);
    }
}
