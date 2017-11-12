package components;

public class Track extends Component
{
    private Component train = null; // null when train is not on this track

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up." + " (" + isLock() + ")");
        processMessage();
    }

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

    void setTrain(Component train) { this.train = train; }
}
