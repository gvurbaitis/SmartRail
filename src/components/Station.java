package components;

public class Station extends Component
{
    private Track originator; // the initial track connected to the station

    void update()
    {
        justWait();
        System.out.println(getName() + " received message and woke up.");

        // if true then the correct station is found so, send confirmation to the train
        if (processMessage())
        {
            sendConfirmation();
        }
    }

    private boolean processMessage()
    {
        String destination = getMsg().getDestination();

        if (getName().equals(destination))
        {
            System.out.println("Found " + destination + "!");
            System.out.println();
            getMsg().setValidPath(true);
            return true;
        }
        else
        {
            getMsg().setValidPath(false);
        }

        return false;
    }

    private void sendConfirmation()
    {
        getMsg().setDestination(getMsg().getTrainName());
        getMsg().setDirection(getMsg().getDirection() * -1);
        originator.accept(getMsg());
    }

    public void setOriginator(Track originator) { this.originator = originator; }
}
