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
    private List<List<Component>> components;
    private List<Component> tracksSwitchesLights;
    private Train train;
    private List<Thread> threads;

    Coordinator(Stage window)
    {
        this.window = window;
        config = new ArrayList<>();
        tracksSwitchesLights = new ArrayList<>();
        components = new ArrayList<>();
        threads = new ArrayList<>();
    }

    void initSimulation()
    {
        readConfigFile();
        processConfigFile();
        initConfig();
        startThreads();

        Train train = new Train((Track) components.get(0).get(1));
        train.setDeparture("Station 1");
        train.setDestination("bottom station");
        train.setDir(1);
        (new Thread(train, "Train 1")).start();
        //initTracksSwitchesLights();

        //initDisplay(); // initialize the display config (everything except the train :D)
    }

    private void readConfigFile()
    {
        try
        {
            File file = new File("resources/config");
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
        int groupCount = 0;
        int trackCount = 0;
        int stationCount = 0;
        int lightCount = 0;
        int switchCount = 0;

        for (String s : config)
        {
            List<Component> lane = new ArrayList<>();
            ThreadGroup group = new ThreadGroup(String.valueOf(groupCount));


            for (char c : s.toCharArray())
            {
                if (c == 'S')
                {
                    stationCount++;
                    Station station = new Station();
                    lane.add(station);
                    threads.add(new Thread(group, station, "Station " + String.valueOf(stationCount)));
                }

                if (c == '=')
                {
                    trackCount++;
                    Track track = new Track();
                    lane.add(track);
                    threads.add(new Thread(group, track, "Track " + String.valueOf(trackCount)));
                }

                if (c == 'l')
                {
                    lightCount++;
                    Light light = new Light();
                    lane.add(light);
                    threads.add(new Thread(group, light, "Light " + String.valueOf(lightCount)));
                }

                if (c == 't')
                {
                    switchCount++;
                    SwitchTop sw = new SwitchTop();
                    lane.add(sw);
                    //tracksSwitchesLights.add(sw);
                    threads.add(new Thread(sw, "Switch " + String.valueOf(switchCount)));
                }

                if (c == 'b')
                {
                    switchCount++;
                    SwitchBottom sw = new SwitchBottom();
                    lane.add(sw);
                    //tracksSwitchesLights.add(sw);
                    threads.add(new Thread(sw, "Switch " + String.valueOf(switchCount)));
                }
            }
            components.add(lane);
            groupCount++;
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

                if(current instanceof SwitchTop)
                {
                    SwitchBottom sb = new SwitchBottom();
                    Track bottom1 = new Track();
                    Track bottom2 = new Track();
                    Station bottomRightStation = new Station();
                    threads.add(new Thread(bottom1, "bottom 1"));
                    threads.add(new Thread(bottom2, "bottom 2"));
                    threads.add(new Thread(bottomRightStation, "bottom station"));
                    threads.add(new Thread(sb, "bottom switch"));

                    current.setRight(right);
                    current.setLeft(left);
                    ((SwitchTop) current).setUp(false);
                    ((SwitchTop) current).setDownRight(sb);

                    bottom1.setRight(bottom2);
                    bottom1.setLeft(sb);
                    bottom2.setLeft(bottom1);
                    bottom2.setRight(bottomRightStation);

                    bottomRightStation.setOriginator(bottom2);
                    bottomRightStation.setLeft(bottom2);

                    sb.setRight(bottom1);
                    sb.setUpLeft(current);
                    sb.setUp(true);
                }

                if (current instanceof SwitchBottom)
                {
                    // TBD
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
        } else
        {
            current.setLeft(left);
            ((Station) current).setOriginator((Track) left);
        }
    }
    private void initTracksSwitchesLights()
    {
        for (int i = 0; i < tracksSwitchesLights.size(); i++)
        {
            Component component = tracksSwitchesLights.get(i);

            if(component instanceof SwitchTop)
            {
                SwitchBottom sb = new SwitchBottom();
                Track track1 = new Track();
                Track track2 = new Track();
                Station station3 = new Station();

                ((SwitchTop)component).setDownRight(sb);
                sb.setUpLeft(component);
                sb.setRight(track1);
                sb.setUp(true);
                track1.setLeft(sb);
                track1.setRight(track2);
                track2.setLeft(track1);
                track2.setRight(station3);
                station3.setOriginator(track2);
                threads.add(new Thread(sb, "SwitchBottom " + 1));
                threads.add(new Thread(track1, "Track " + 13));
                threads.add(new Thread(track2, "Track " + 666));
                threads.add(new Thread(station3, "Atlantis"));
                System.out.println("Nik Rocks!");
            }
            if (component.getLeft() == null) component.setLeft(tracksSwitchesLights.get(i - 1));
            if (component.getRight() == null) component.setRight(tracksSwitchesLights.get(i + 1));
        }
    }

    private void initDisplay()
    {
        Display display = new Display(window, components);
        display.initialize();
        display.drawConfig();
    }

    private void startThreads()
    {
        for (Thread t : threads)
        {
            t.start();
        }
    }
}