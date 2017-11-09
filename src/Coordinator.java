import components.*;
import display.Display;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Coordinator
{
    private Stage window;
    private List<String> config;
    private List<Station> stations;
    private List<Track> tracks;
    private List<List<Component>> components;
    private Train train;
    private List<Thread> threads;

    Coordinator(Stage window)
    {
        this.window = window;
        config = new ArrayList<>();
        stations = new ArrayList<>();
        tracks = new ArrayList<>();
        components = new ArrayList<>();
        threads = new ArrayList<>();
    }

    void initSimulation()
    {
        readConfigFile();
        processConfigFile();
        initConfig();

        //initStations();
        //initTracks();
        initTestTrain(); // test train for first version :D
        //initDisplay(); // initialize the display config (everything except the train :D)

        startThreads();
    }

    private void readConfigFile()
    {
        try
        {
            File file = new File("file:../../resources/config");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine())
            {
                String s = sc.nextLine();
                config.add(s);
                System.out.println(s);
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void processConfigFile()
    {
        int trackCount = 0;
        int stationCount = 0;
        int lightCount = 0;

        for (String s : config)
        {
            List<Component> lane = new ArrayList<>();

            for (char c : s.toCharArray())
            {
                if (c == 'S')
                {
                    stationCount++;
                    Station station = new Station();
                    stations.add(station);
                    lane.add(station);
                    threads.add(new Thread(station, "Station " + String.valueOf(stationCount)));
                }

                if (c == '=')
                {
                    trackCount++;
                    Track track = new Track();
                    tracks.add(track);
                    lane.add(track);
                    threads.add(new Thread(track, "Track " + String.valueOf(trackCount)));
                }

                if (c == 'l')
                {
                    lightCount++;
                    Light light = new Light();
                    lane.add(light);
                    threads.add(new Thread(light, "Light " + String.valueOf(lightCount)));
                }
            }
            components.add(lane);
        }
    }

    private void initConfig()
    {
        Component current, left, right;
        int stationCount, laneSize;
        left = right = null;

        for (int i = 0; i < components.size(); i++)
        {
            stationCount = 0;
            laneSize = components.get(i).size();

            for (int j = 0; j < laneSize; j++)
            {
                current = components.get(i).get(j);

                if (j - 1 >= 0) left = components.get(i).get(j - 1);
                if (j + 1 < laneSize) right = components.get(i).get(j + 1);

                if (current instanceof Station)
                {
                    initStation(stationCount, current, left, right);
                    stationCount++;
                }

                if (current instanceof Track)
                {
                    current.setLeft(left);
                    current.setRight(right);
                }
            }
        }
    }

    private void initStation(int stationCount, Component current, Component left, Component right)
    {
        if (stationCount == 0)
        {
            current.setRight(right);
            ((Station) current).setOriginator((Track) right);
        }
        else
        {
            current.setLeft(left);
            ((Station) current).setOriginator((Track) left);
        }
    }

    /**
    private void initStations()
    {
        for (int i = 0; i < stations.size(); i++)
        {
            Station station = stations.get(i);

            if (i == 0) // if even or 0, set right
            {
                Track track = tracks.get(0);
                station.setOriginator(track);
                station.setRight(track);
                track.setLeft(station);
            }
            else
            {
                Track track = tracks.get(tracks.size() - 1);
                station.setOriginator(track);
                station.setLeft(track);
                track.setRight(station);
            }
        }
    }

    private void initTracks()
    {
        for (int i = 0; i < tracks.size(); i++)
        {
            Track track = tracks.get(i);

            if (track.getLeft() == null) track.setLeft(tracks.get(i - 1));
            if (track.getRight() == null) track.setRight(tracks.get(i + 1));
        }
    }*/

    private void initTestTrain()
    {
        train = new Train(tracks.get(0));
        train.setDeparture("Station 1");
        train.setDestination("Station 2");
        train.setDir(1);
        threads.add(new Thread(train, "Train"));
    }

    private void initDisplay()
    {
        //Display display = new Display(window, components, train);
        //display.initialize();
        //display.drawConfig();
    }

    private void startThreads()
    {
        for (Thread t : threads)
        {
            t.start();
        }
    }
}