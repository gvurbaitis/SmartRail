package main.components;

//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Light Class performs much like
// a track with similar message passing,
// however it has an on field which is
// invoked when a message has found a
// secured path and is on its way back
// to the Train. It may also be set back
// to red with the public SetOn method
// to allow the train to alter its state
// when moving past it.
//***********************************
public class Light extends Component
{
    private boolean on; // true == green light else red light

    /**
     * Input: None
     * Returns void
     * As the update method is
     * within a while loop, the
     * thread waits until a message is
     * passed so as not to use resources.
     * Accepting a message notifies the
     * thread and allows the Light to
     * process whatever was passed.
     */
    void update()
    {
        justWait();
        processMessage();
    }

    /**
     * Input: None
     * Returns void
     * Process Message is fairly simple if
     * the destination hasn't been found yet, or
     * if the path was found to be invalid,
     * it simply passes it on to the next neighbor.
     * If the message is returning to the station
     * the message is passed, but the component is
     * also locked, and the light is turned on.
     */
    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();

        // if the message is going back to the train
        if (destination.equals(getMsg().getTrainName()))
        {
            // if not valid path unlock, else if valid path lock
            if (!getMsg().isValidPath())
            {
                neighbor.accept(getMsg());
            }
            else
            {
                lock(); // lock on the way back, when valid path
                on = true;
                neighbor.accept(getMsg());
            }
        }
        else // if the message is going to the destination station
        {
            neighbor.accept(getMsg());
        }
    }

    /**
     * Input: none
     * Returns boolean on, the status
     * of the light.
     * Returns true if the light
     * is green and false if the
     * light is red.
     */
    public boolean isOn()
    {
        return on;
    }

    /**
     * Input: boolean on, the
     * status of the light.
     * Returns void
     * Sets on to the inputted
     * value, true for green,
     * false for red.
     */
    public void setOn(boolean on)
    {
        this.on = on;
    }
}
