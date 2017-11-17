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

/**
 * Main simulation display. Creates all the gui elements and
 * implements an animation timer to update the lights and the train.
 */
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

    /**
     * Constructor to set global variables
     *
     * @param window           main window object
     * @param components       list of all components to be displayed
     * @param drawableSwitches list of a subset of switches to be displayed
     */
    public Display(Stage window, List<List<Component>> components,
                   List<Switch> drawableSwitches)
    {
        this.window = window;
        this.components = components;
        this.drawableSwitches = drawableSwitches;
    }

    /**
     * Creates a new scene and draws the initial configuration to the gui.
     */
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

    /**
     * Loads the background once and displays it
     */
    private void drawBackground()
    {
        Canvas canvas = new Canvas(800, 700);
        GraphicsContext gtx = canvas.getGraphicsContext2D();
        ImagePattern imagePattern = new ImagePattern(new Image("background.png"));
        gtx.setFill(imagePattern);
        gtx.fillRect(0, 0, 800, 700);

        root.getChildren().add(canvas);
    }

    /**
     * Iterates through the components list and draws the appropriate
     * images based on the type of component. Starts the animation timer
     * once everything is displayed.
     */
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

    /**
     * Draws the station image to the display
     *
     * @param s reference to the station object
     * @param x the x location
     * @param y the y location
     */
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

    /**
     * Draws track image to the display
     *
     * @param x the x location
     * @param y the y location
     */
    private void drawTrack(double x, double y)
    {
        Rectangle track = new Rectangle(x, y, 40, 30);
        ImagePattern imagePattern = new ImagePattern(trackImg);
        track.setFill(imagePattern);

        root.getChildren().add(track);
    }

    /**
     * Draws a circle (default red light) the the display
     *
     * @param l a reference to the light object
     * @param x the x location
     * @param y the y location
     */
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

    /**
     * Draws the appropriate switch based on its type.
     * Type 0 is a right switch else left switch
     *
     * @param type the type of switch to draw
     * @param x    the x location
     * @param y    the y location
     */
    private void drawSwitch(int type, double x, double y)
    {
        Rectangle sw = new Rectangle(x, y + 70, 40, 50);
        ImagePattern imagePattern;

        if (type == 0) imagePattern = new ImagePattern(rightSwitchImg);
        else imagePattern = new ImagePattern(leftSwitchImg);

        sw.setFill(imagePattern);

        root.getChildren().add(sw);
    }

    /**
     * Creates a new train and starts a new thread for the train.
     * First, click determines departure station, second click
     * determines destination station.
     *
     * @param e reference to the mouse event
     */
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

    /**
     * Called by the animation timer at 60Hz, updates the location trainRect
     * object which represents our train.
     *
     * @param x   the x location to update to
     * @param y   the y location to update to
     * @param dir the direction to point the head of the train
     */
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

    /**
     * Inner class that starts an Animation timer which updates at a
     * rate of 60Hz.
     */
    class Animation extends AnimationTimer
    {
        /**
         * Called by the animation timer to update at 60Hz. Updates the
         * location of the train, updates the state of the lights, and
         * removes all trains that have shutdown.
         *
         * @param now current time
         */
        @Override
        public void handle(long now)
        {
            updateTrains();
            updateLights();
            removeShutdownTrains();
        }

        /**
         * Updates every train (only needs to work with one currently).
         * Removes train image when train is shutdown.
         */
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

        /**
         * Removes all trains that have shutdown.
         */
        private void removeShutdownTrains()
        {
            for (int i = 0; i < trains.size(); i++)
            {
                if (trains.get(i).isShutdown()) trains.remove(trains.get(i));
            }
        }

        /**
         * Changes the color of the light ot green or red based on it's
         * current state.
         */
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