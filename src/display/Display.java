package display;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Display
{
    private String laneConfig;
    private Stage window;
    private AnchorPane root;

    public Display(Stage window, String laneConfig)
    {
        this.window = window;
        this.laneConfig = laneConfig;
    }

    public void initialize()
    {
        root = new AnchorPane();
        root.setPrefSize(850, 850);

        Scene scene = new Scene(root);
        window.setTitle("Train Simulation");
        window.setResizable(false);
        window.sizeToScene();
        window.setScene(scene);
        window.show();
    }

    public void drawConfig()
    {

    }

    private void drawStation()
    {
    }

    private void drawTrack()
    {

    }
}
