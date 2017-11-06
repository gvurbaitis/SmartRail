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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
        double x = 30;
        double y = 30;

        for (Component c : components)
        {
            if (c instanceof Station)
            {
                c.setX(x);
                c.setY(y);
                drawStation(x, y);
                x += 60;
            }
            if (c instanceof Track)
            {
                c.setX(x);
                c.setY(y + 15);
                drawTrack(x, y + 15);
                x += 40;
            }
        }

        Animation animation = new Animation();
        animation.start();
    }

    private void drawStation(double x, double y)
    {
        Rectangle station = new Rectangle(x, y, 60, 60);
        Image img = new Image("file:../../resources/station.png", false);
        ImagePattern imagePattern = new ImagePattern(img);
        station.setFill(imagePattern);

        root.getChildren().add(station);
    }

    private void drawTrack(double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 40, 30);
        Image img = new Image("file:../../resources/track.png", false);
        ImagePattern imagePattern = new ImagePattern(img);
        track.setFill(imagePattern);

        root.getChildren().add(track);
    }

    private void drawTrain(double x, double y)
    {
        if (trainRect == null)
        {
            trainRect = new Rectangle(x, y, 50, 40);
            Image img = new Image("file:../../resources/train.png", false);
            ImagePattern imagePattern = new ImagePattern(img);
            trainRect.setFill(imagePattern);

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
            drawTrain(train.getCurrentTrack().getX(), train.getCurrentTrack().getY() - 15);
        }
    }
}