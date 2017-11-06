import components.*;
import display.Display;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Coordinator
{
    private Stage window;
    private String laneConfig;
    private List<Station> stations;
    private List<Track> tracks;
    private List<Component> components;
    private Train train;
    private List<Thread> threads;

    Coordinator(Stage window)
    {
        this.window = window;
        stations = new ArrayList<>();
        tracks = new ArrayList<>();
        components = new ArrayList<>();
        threads = new ArrayList<>();
    }

    void initSimulation()
    {
        readConfigFile();
        processConfigFile();

        initStations();
        initTracks();
        initTestTrain(); // test train for first version :D
        initDisplay(); // initialize the display config (everything except the train :D)

        startThreads();
    }

    private void readConfigFile()
    {
        try
        {
            File file = new File(this.getClass().getClassLoader().getResource("config1").toURI());
            Scanner sc = new Scanner(file);
            this.laneConfig = sc.nextLine();
            System.out.println(laneConfig);
            sc.close();
        }
        catch (URISyntaxException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void initDisplay()
    {
        Display display = new Display(window, components, train);
        display.initialize();
        display.drawConfig();
    }

    private void processConfigFile()
    {
        int trackCount = 0;
        int stationCount = 0;

        for (char c : laneConfig.toCharArray())
        {
            if (c == 'S')
            {
                String city;
                if (stationCount == 0) city = "Albuquerque";
                else city = "Atlantis";
                stationCount++;

                Station station = new Station();
                stations.add(station);
                components.add(station);
                threads.add(new Thread(station, city));
            }

            if (c == '=')
            {
                trackCount++;
                Track track = new Track();
                tracks.add(track);
                components.add(track);
                threads.add(new Thread(track, "Track " + String.valueOf(trackCount)));
            }
        }
    }

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
    }

    private void initTestTrain()
    {
        this.train = new Train(tracks.get(0));
        threads.add(new Thread(train, "Train"));
    }

    private void startThreads()
    {
        for (Thread t : threads)
        {
            t.start();
        }
    }
}