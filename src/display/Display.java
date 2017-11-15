package display;

import components.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Display
{
    private Stage window;
    private Group root;
    private List<List<Component>> components;
    private List<Switch> drawableSwitches; // only draw one part of switch
    private List<Circle> lights; // circle objects representing lights
    private List<Train> trains;
    private Train train; // current train (only one train in existence at a time)
    private Rectangle trainRect;
    private int trainCount = 0;
    private int stationClickCount = 0;

    public Display(Stage window, List<List<Component>> components,
                   List<Switch> drawableSwitches)
    {
        this.window = window;
        this.components = components;
        this.drawableSwitches = drawableSwitches;
    }

    public void initialize()
    {
        Canvas canvas = new Canvas(800, 700);
        GraphicsContext gtx = canvas.getGraphicsContext2D();
        gtx.setFill(Color.WHITE);
        gtx.fillRect(0, 0, 800, 700);

        trains = new ArrayList<>();
        lights = new ArrayList<>();

        root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 800, 700);
        window.setTitle("Train Simulation");
        window.setResizable(false);
        window.sizeToScene();
        window.setScene(scene);
        window.setOnCloseRequest(e -> System.exit(0));
        window.show();
    }


    public void drawConfig()
    {
        double x = 30;
        double y = 30;

        for (List<Component> lane : components)
        {
            for (Component c : lane)
            {
                if (c instanceof Station)
                {
                    c.setX(x);
                    c.setY(y);
                    drawStation(x, y, c.getGroup(), c.getName());
                    x += 60;
                }

                if (c instanceof Track)
                {
                    c.setX(x);
                    c.setY(y + 15);
                    drawTrack(x, y + 15);
                    x += 40;
                }

                if (c instanceof Light)
                {
                    c.setX(x);
                    c.setY(y + 5);
                    drawLight((Light) c, x, y + 5);
                }

                if (c instanceof Switch)
                {
                    if (drawableSwitches.contains(c))
                    {
                        c.setX(x);
                        c.setY(y + 48);
                        drawSwitch(((Switch) c).getType(), x, y + 48);
                    }
                }
            }

            x = 30;
            y += 70;
        }

        Animation animation = new Animation();
        animation.start();
    }

    private void drawStation(double x, double y, String group, String name)
    {
        Rectangle station = new Rectangle(x, y, 60, 60);
        station.setId(name);
        station.setUserData(group);

        Image img = new Image("station.png", false);
        ImagePattern imagePattern = new ImagePattern(img);
        station.setFill(imagePattern);
        station.setOnMouseClicked(this::initTrain);

        root.getChildren().add(station);
    }

    private void drawTrack(double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 40, 30);
        Image img = new Image("track.png", false);
        ImagePattern imagePattern = new ImagePattern(img);
        track.setFill(imagePattern);

        root.getChildren().add(track);
    }

    private void drawLight(Light l, double x, double y)
    {
        Circle light = new Circle(x, y, 6);
        light.setUserData(l);
        light.setStrokeWidth(3);
        light.setStroke(Color.BLACK);
        light.setFill(Color.RED);
        lights.add(light);

        root.getChildren().add(light);
    }

    private void drawSwitch(int type, double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 40, 30);
        Image img;

        if (type == 0) img = new Image("rswitch.png", false);
        else img = new Image("lswitch.png", false);

        ImagePattern imagePattern = new ImagePattern(img);
        track.setFill(imagePattern);

        root.getChildren().add(track);
    }

    private void initTrain(MouseEvent e)
    {
        Rectangle r = (Rectangle) e.getSource();

        if (stationClickCount == 0)
        {
            int lane = Integer.parseInt((String) r.getUserData());
            int laneSize = components.get(lane).size();
            Track track;

            if (r.getX() > window.getWidth()/5) track = (Track) components.get(lane).get(laneSize - 2);
            else track = (Track) components.get(lane).get(1);

            train = new Train(track);
            train.setDeparture(r.getId());

            stationClickCount++;
        }
        else
        {
            trainCount++;

            if (r.getX() > window.getWidth()/5) train.setDir(1);
            else train.setDir(-1);

            ThreadGroup g = new ThreadGroup(train.getCurrentTrack().getGroup());
            train.setDestination(r.getId());
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
            Image img = new Image("train.png", false);
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
            for (Circle light: lights)
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