package display;

import components.Component;
import components.Station;
import components.Track;
import components.Train;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Display
{
    private Stage window;
    private Group root;
    private List<Component> components;
    private Train train;
    private Rectangle trainRect;

    public Display(Stage window, List<Component> components, Train train)
    {
        this.window = window;
        this.components = components;
        this.train = train;
    }

    public void initialize()
    {
        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gtx = canvas.getGraphicsContext2D();
        gtx.setFill(Color.WHITE);
        gtx.fillRect(0, 0, 600, 500);

        root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 600, 500);
        window.setTitle("Train Simulation");
        window.setResizable(false);
        window.sizeToScene();
        window.setScene(scene);
        window.show();
    }


    public void drawConfig()
    {
        double x = 50;
        double y = 50;

        for (Component c : components)
        {
            if (c instanceof Station)
            {
                c.setX(x);
                c.setY(y);
                drawStation(x, y);
                x += 40;
            }
            if (c instanceof Track)
            {
                c.setX(x);
                c.setY(y);
                drawTrack(x, y);
                x += 40;
            }
        }

        Animation animation = new Animation();
        animation.start();
    }

    private void drawStation(double x, double y)
    {
        Rectangle station = new Rectangle(x, y, 30, 20);
        station.setFill(Color.BLUE);
        root.getChildren().add(station);
    }

    private void drawTrack(double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 30, 20);
        track.setFill(Color.BLACK);
        root.getChildren().add(track);
    }

    private void drawTrain(double x, double y)
    {
        if (trainRect == null)
        {
            trainRect = new Rectangle(x, y, 30, 20);
            trainRect.setFill(Color.RED);
            root.getChildren().add(trainRect);
        }
        else
        {
            trainRect.setX(x);
            trainRect.setY(y);
        }
    }

    class Animation extends AnimationTimer
    {
        @Override
        public void handle(long now)
        {
            drawTrain(train.getCurrentTrack().getX(), train.getCurrentTrack().getY());
        }
    }
}