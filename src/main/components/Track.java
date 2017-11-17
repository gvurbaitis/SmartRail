package main.components;

//***********************************
// Gabriel Urbaitis, Akash Patel
//
// The Track is the simplest of the
// components, consisting of message
// passing between other Tracks and
// the train. The Track passes the message
// to its neighbor in the direction of
// the message, locking the Track if
// it is on its way back to the train,
// and passing a message to the train
// if it is on the current track.
//***********************************
public class Track extends Component
{
    private Component train = null; // null when train is not on this track

    /**
     * Input: None
     * Returns void
     * As the update method is
     * within a while loop, the
     * thread waits until a message is
     * passed so as not to use resources.
     * Accepting a message notifies the
     * thread and allows the Track to
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
     * also locked. If there is a train on the
     * current track, then the message is passed
     * to the train.
     */
    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();

        // if the message is going back to the train
        if (destination.equals(getMsg().getTrainName()))
        {
            // if the message has reached the track that the train is on
            if (train != null)
            {
                train.accept(getMsg());
                lock(); // lock the track that the train is on
            }
            else if (!getMsg().isValidPath()) // if not valid path unlock, else if valid path lock
            {
                neighbor.accept(getMsg());
            }
            else
            {
                lock(); // lock on the way back, when valid path
                neighbor.accept(getMsg());
            }
        }
        else // if the message is going to the destination station
        {
            neighbor.accept(getMsg());
        }
    }

    /**
     * Input: Train train, the train
     * occupying the track
     * Returns void
     * Sets the train reference
     * for the current track.
     */
    void setTrain(Train train)
    {
        this.train = train;
    }
}
