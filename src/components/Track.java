package components;

public class Track extends Component
{
    private Component train = null; // null when train is not on this track
    private boolean lock;

    void update()
    {
        justWait();
        lock = true;
        System.out.println(getName() + " received message and woke up.");
        processMessage();
    }

    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();

        if (train != null && destination.equals(getMsg().getTrainName()))
        {
            train.accept(getMsg());
        }
        else
        {
            neighbor.accept(getMsg());
        }
    }

    void setTrain(Component train) { this.train = train; }

    boolean isLock() { return lock; }
    void unlock() { this.lock = false; }
}
