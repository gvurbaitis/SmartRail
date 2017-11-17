package main.components;

import java.util.ArrayList;
import java.util.List;

//***********************************
// Gabriel Urbaitis Akash Patel
//
// The Message class forms the core
// of interaction between the component
// threads, with Strings for destination,
// departure group and direction
// used in path finding a list of strings
// takenSwitches for following the correct path,
// and train name for movement along tracks.
// Messages are created within components and
// passed via the synchronized acceptMsg method.
//***********************************
class Message
{
    private int direction;
    private String destination;
    private String trainName;
    private boolean isValidPath;
    private String departureGroup;
    private List<String> takenSwitches = new ArrayList<>(); // switch that is taken to go to the current lane

    /**
     * Input: String sw, a switch
     * along the train's path
     * Returns void
     * Adds the switch to the taken
     * switches list, for the the train
     * to use along its path
     */
    void addTakenSwitch(String sw)
    {
        this.takenSwitches.add(sw);
    }

    /**
     * Input: none
     * Returns void
     * Removes the switch at the end
     * of the taken switches list
     */
    void removeLastTakenSwitch()
    {
        this.takenSwitches.remove(takenSwitches.size() - 1);
    }

    /**
     * Input: none
     * Returns String, the last
     * taken switch
     * Gets the switch at the end
     * of the taken switches list
     */
    String getLastTakenSwitch()
    {
        return takenSwitches.get(takenSwitches.size() - 1);
    }

    /**
     * Input: none
     * Returns List<String> takenSwitches
     * the switches in the path of the
     * train
     * Gets the list of switches in the
     * path of the train
     */
    List<String> getTakenSwitches()
    {
        return takenSwitches;
    }

    /**
     * Input: List<String> takenSwitches
     * the switches in the path of the
     * train
     * Returns void
     * Sets the list of switches in the
     * path of the train
     */
    void setTakenSwitches(List<String> takenSwitches)
    {
        this.takenSwitches.addAll(takenSwitches);
    }

    /**
     * Input: none
     * Returns String trainName, the
     * name of the train
     * Gets the String identifier for
     * the train.
     */
    String getTrainName()
    {
        return trainName;
    }

    /**
     * Input: String trainName, the
     * name of the train
     * Returns void
     * Sets the String identifier for
     * the train.
     */
    void setTrainName(String trainName)
    {
        this.trainName = trainName;
    }

    /**
     * Input: none
     * Returns boolean isValidPath,
     * the validity of the chosen path
     * Gets whether or not the chosen
     * path is valid
     */
    boolean isValidPath()
    {
        return isValidPath;
    }

    /**
     * Input: boolean isValidPath,
     * the validity of the chosen path
     * Returns void
     * Sets whether or not the chosen
     * path is valid
     */
    void setValidPath(boolean validPath)
    {
        isValidPath = validPath;
    }

    /**
     * Input: none
     * Returns int direction, left or right
     * Gets the direction of the message,
     * -1 for Left, 1 for Right
     */
    int getDirection()
    {
        return direction;
    }

    /**
     * Input: int direction, left or right
     * Returns void
     * Sets the direction of the message,
     * -1 for Left, 1 for Right
     */
    void setDirection(int direction)
    {
        this.direction = direction;
    }

    /**
     * Input: none
     * Returns String destination, any
     * of the station names
     * Gets the destination of the train,
     * attempted to be reached by
     * the message
     */
    String getDestination()
    {
        return destination;
    }

    /**
     * Input: String destination, any
     * of the station names
     * Returns void
     * Sets the destination of the train,
     * attempted to be reached by
     * the message
     */
    void setDestination(String destination)
    {
        this.destination = destination;
    }

    /**
     * Input: none
     * Returns String departureGroup, any
     * of the station names
     * Gets the departure group of the message,
     * the station that the message must return to
     */
    String getDepartureGroup()
    {
        return departureGroup;
    }

    /**
     * Input: String departureGroup, any
     * of the station names
     * Returns void
     * Sets the departure group of the message,
     * the station that the message must return to
     */
    void setDepartureGroup(String departureGroup)
    {
        this.departureGroup = departureGroup;
    }
}
