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

public class MainMenu
{
    private Stage window;
    private Group root;

    public MainMenu(Stage window)
    {
        this.window = window;
    }

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

    private void drawBackground()
    {
        Rectangle background = new Rectangle(0, 0, 800, 700);
        ImagePattern imagePattern = new ImagePattern(new Image("background.png"));
        background.setFill(imagePattern);

        root.getChildren().add(background);
    }

    private void createButton()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "   Config1",
                        "   Config2",
                        "   Config3",
                        "  Config_TA"
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

    private void playAction(String configFile)
    {
        Coordinator coordinator = new Coordinator(window, configFile);
        coordinator.initSimulation();
    }

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
