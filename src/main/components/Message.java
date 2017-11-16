package main.components;

import java.util.ArrayList;
import java.util.List;

class Message
{
    private int direction;
    private String destination;
    private String trainName;
    private boolean isValidPath;
    private String departureGroup;
    private List<String> takenSwitches = new ArrayList<>(); // switch that is taken to go to the current lane

    void addTakenSwitch(String sw) { this.takenSwitches.add(sw); }
    void removeLastTakenSwitch() { this.takenSwitches.remove(takenSwitches.size() - 1); }
    String getLastTakenSwitch() { return takenSwitches.get(takenSwitches.size() - 1); }
    List<String> getTakenSwitches() { return takenSwitches; }
    void setTakenSwitches(List<String> takenSwitches) { this.takenSwitches.addAll(takenSwitches); }

    String getTrainName() { return trainName; }
    void setTrainName(String trainName) { this.trainName = trainName; }

    boolean isValidPath() { return isValidPath; }
    void setValidPath(boolean validPath) { isValidPath = validPath; }

    int getDirection() { return direction; }
    void setDirection(int direction) { this.direction = direction; }

    String getDestination() { return destination; }
    void setDestination(String destination) { this.destination = destination; }

    String getDepartureGroup() { return departureGroup; }
    void setDepartureGroup(String departureGroup) { this.departureGroup = departureGroup; }
}
