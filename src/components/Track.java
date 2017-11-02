package components;

public class Track extends Component
{
    private Component train = null; // null when train is not on this track

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up.");
        processMessage();
    }

    private void processMessage()
    {
        Component neighbor = getNeighbor(getMsg().getDirection());
        String destination = getMsg().getDestination();

        getMsg().push(getName()); // add current track to path

        if (train != null && destination.equals("Train"))
        {
            train.accept(getMsg());
        }
        else
        {
            neighbor.accept(getMsg());
        }
    }

    void setTrain(Component train) { this.train = train; }
}
