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
    private List<Switch> drawableSwitches; // since switches have two parts only draw for one of them
    private List<Thread> threads;

    Coordinator(Stage window)
    {
        this.window = window;
        config = new ArrayList<>();
        components = new ArrayList<>();
        drawableSwitches = new ArrayList<>();
        threads = new ArrayList<>();
    }

    void initSimulation()
    {
        readConfigFile();
        processConfigFile();
        initConfig();
        startThreads();

        initDisplay(); // initialize the display config (everything except the train :D)
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
        } catch (FileNotFoundException e)
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
                if (c == 'S' || c == 'D')
                {
                    stationCount++;
                    Station station = new Station();

                    // if type 0 then normal station else dead end
                    if (c == 'S') station.setType(0);
                    else station.setType(1);

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

                if (c == 'R')
                {
                    switchCount++;
                    Switch sw = new Switch();
                    sw.setType(0);
                    lane.add(sw);
                    threads.add(new Thread(group, sw, "Switch " + String.valueOf(switchCount)));
                }

                if (c == 'L')
                {
                    switchCount++;
                    Switch sw = new Switch();
                    sw.setType(1);
                    lane.add(sw);
                    threads.add(new Thread(group, sw, "SwitchLeft " + String.valueOf(switchCount)));
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

                if (current instanceof Light)
                {
                    current.setLeft(left);
                    current.setRight(right);
                    ((Light) current).setOn(false);
                }

                if (current instanceof Switch)
                {
                    initSwitch(current, left, right, i, j);
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

    private void initSwitch(Component current, Component l, Component r, int i, int j)
    {
        Switch sw = (Switch) current;
        sw.setFlipped(false);
        sw.setLeft(l);
        sw.setRight(r);

        if (sw.getFlippedNeighbor() == null) // if the switched neighbor is not set yet
        {
            if (i == components.size() - 1) i--;
            else i++;

            // 0 type means switch connects to the RIGHT on a lane above or below
            if (sw.getType() == 0) j++;
            else j--;

            // both switches give each other their references
            Switch connection = (Switch) components.get(i).get(j);
            sw.setFlippedNeighbor(connection);
            connection.setFlippedNeighbor(sw);

            drawableSwitches.add(sw);
        }
    }

    private void initDisplay()
    {
        Display display = new Display(window, components, drawableSwitches);
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