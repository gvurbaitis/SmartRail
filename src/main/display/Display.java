package main.display;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import main.components.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.components.Component;

import java.util.ArrayList;
import java.util.List;

public class Display
{
    private Group root;
    private Stage window;
    private List<List<Component>> components;
    private List<Switch> drawableSwitches; // only draw one part of switch
    private List<Circle> lights; // circle objects representing lights
    private List<Train> trains;
    private Train train; // current train (only one train in existence at a time)
    private Rectangle trainRect;
    private int trainCount = 0;
    private int stationClickCount = 0;

    // load all images only once
    private Image stationImg = new Image("station.png", false);
    private Image deadEndImg = new Image("dead_end.png", false);
    private Image trackImg = new Image("track.png", false);
    private Image leftSwitchImg = new Image("lswitch.png", false);
    private Image rightSwitchImg = new Image("rswitch.png", false);
    private Image trainImg = new Image("train.png", false);

    public Display(Stage window, List<List<Component>> components,
                   List<Switch> drawableSwitches)
    {
        this.window = window;
        this.components = components;
        this.drawableSwitches = drawableSwitches;
    }

    public void initialize()
    {
        root = new Group();
        trains = new ArrayList<>();
        lights = new ArrayList<>();

        drawBackground();
        drawConfig();

        Scene scene = new Scene(root, 800, 700);
        window.setTitle("Train Simulation - Simulation started...");
        window.setScene(scene);
        window.setOnCloseRequest(e -> System.exit(0));
        window.show();
    }

    private void drawBackground()
    {
        Canvas canvas = new Canvas(800, 700);
        GraphicsContext gtx = canvas.getGraphicsContext2D();
        ImagePattern imagePattern = new ImagePattern(new Image("background.png"));
        gtx.setFill(imagePattern);
        gtx.fillRect(0, 0, 800, 700);

        root.getChildren().add(canvas);
    }

    private void drawConfig()
    {
        double x = 30;
        double y = 30;

        for (List<Component> lane : components)
        {
            for (Component c : lane)
            {
                if (c instanceof Station)
                {
                    drawStation((Station) c, x, y);
                    x += 70;
                }

                if (c instanceof Track)
                {
                    c.setX(x);
                    c.setY(y + 40);
                    drawTrack(x, y + 40);
                    x += 40;
                }

                if (c instanceof Light)
                {
                    drawLight((Light) c, x, y + 55);
                }

                if (c instanceof Switch)
                {
                    if (drawableSwitches.contains(c))
                    {
                        drawSwitch(((Switch) c).getType(), x, y);
                    }
                }
            }

            x = 30;
            y += 80;
        }

        Animation animation = new Animation();
        animation.start();
    }

    private void drawStation(Station s, double x, double y)
    {
        Rectangle station = new Rectangle(x, y, 70, 70);
        ImagePattern imagePattern;
        station.setUserData(s);

        if (s.getType() == 0)
        {
            imagePattern = new ImagePattern(stationImg);
            station.setOnMouseClicked(this::initTrain);
        }
        else imagePattern = new ImagePattern(deadEndImg);

        station.setFill(imagePattern);
        station.setCursor(Cursor.HAND);

        root.getChildren().add(station);
    }

    private void drawTrack(double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 40, 30);
        ImagePattern imagePattern = new ImagePattern(trackImg);
        track.setFill(imagePattern);

        root.getChildren().add(track);
    }

    private void drawLight(Light l, double x, double y)
    {
        Circle light = new Circle(x, y, 5);
        light.setUserData(l);
        light.setStrokeWidth(3);
        light.setStroke(Color.BLACK);
        light.setFill(Color.RED);
        lights.add(light);

        root.getChildren().add(light);
    }

    private void drawSwitch(int type, double x, double y)
    {
        Rectangle sw = new Rectangle(x, y + 70, 40, 50);
        ImagePattern imagePattern;

        if (type == 0) imagePattern = new ImagePattern(rightSwitchImg);
        else imagePattern = new ImagePattern(leftSwitchImg);

        sw.setFill(imagePattern);

        root.getChildren().add(sw);
    }

    private void initTrain(MouseEvent e)
    {
        Rectangle r = (Rectangle) e.getSource();
        Station s = ((Station) r.getUserData());

        if (stationClickCount == 0)
        {
            train = new Train(s.getOriginator());
            train.setDeparture(s.getName());

            if (s.getLeft() == null) train.setDir(1);
            else train.setDir(-1);

            stationClickCount++;
        }
        else
        {
            trainCount++;

            ThreadGroup g = new ThreadGroup(train.getCurrentTrack().getGroup());
            train.setDestination(s.getName());
            trains.add(train);
            (new Thread(g, train, "Train " + String.valueOf(trainCount))).start();

            stationClickCount = 0;
        }
    }

    private void updateTrain(double x, double y, int dir)
    {
        if (!root.getChildren().contains(trainRect))
        {
            trainRect = new Rectangle(x, y, 50, 40);
            ImagePattern imagePattern = new ImagePattern(trainImg);
            trainRect.setFill(imagePattern);

            root.getChildren().add(trainRect);
        }
        else
        {
            if (dir == 1) trainRect.setScaleX(1);
            else trainRect.setScaleX(-1);

            trainRect.setX(x);
            trainRect.setY(y);
        }
    }

    class Animation extends AnimationTimer
    {
        @Override
        public void handle(long now)
        {
            updateTrains();
            updateLights();
            removeShutdownTrains();
        }

        private void updateTrains()
        {
            for (Train t : trains)
            {
                if (t != null)
                {
                    if (!t.isShutdown())
                    {
                        if (t.isValidPath())
                        {
                            double x = t.getCurrentTrack().getX();
                            double y = t.getCurrentTrack().getY() - 15;
                            updateTrain(x, y, t.getDir());
                        }
                    }
                    else root.getChildren().remove(trainRect);
                }
            }
        }

        private void removeShutdownTrains()
        {
            for (int i = 0; i < trains.size(); i++)
            {
                if (trains.get(i).isShutdown()) trains.remove(trains.get(i));
            }
        }

        private void updateLights()
        {
            for (Circle light : lights)
            {
                if (light != null)
                {
                    if (((Light) light.getUserData()).isOn())
                    {
                        light.setFill(Color.GREEN);
                    }
                    else light.setFill(Color.RED);
                }
            }
        }
    }
}